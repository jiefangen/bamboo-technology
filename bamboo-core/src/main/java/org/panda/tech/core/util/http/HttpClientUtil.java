package org.panda.tech.core.util.http;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.*;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.panda.bamboo.common.constant.basic.Strings;
import org.panda.bamboo.common.model.tuple.Binary;
import org.panda.bamboo.common.util.LogUtil;
import org.panda.bamboo.common.util.jackson.JsonUtil;
import org.panda.tech.core.web.util.NetUtil;
import org.springframework.http.HttpMethod;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * HttpClient客户端工具类
 * 建议场景：具有稳定可靠的生态，提供身份验证、Cookie 管理、重定向管理等
 */
public class HttpClientUtil {

    public static final CloseableHttpClient HTTP_CLIENT = HttpClientBuilder.create().build();

    private HttpClientUtil() {
    }

    @SuppressWarnings("unchecked")
    private static CloseableHttpResponse execute(HttpMethod method, String url, Map<String, Object> params, Object bodyParams,
                                                 Map<String, String> headers, String encoding) throws Exception {
        if (params != null && !params.isEmpty()) {
            url = NetUtil.mergeParams(url, params, Strings.ENCODING_UTF8);
        }
        HttpRequestBase request = null;
        switch (method) {
            case GET:
                request = new HttpGet(url);
                break;
            case POST:
                HttpPost httpPost = new HttpPost(url);
                if (bodyParams != null) {
                    httpPost.setEntity(new StringEntity(JsonUtil.toJson(bodyParams),
                            ContentType.create(ContentType.APPLICATION_JSON.getMimeType(), encoding)));
                }
                request = httpPost;
                break;
            case PUT:
                HttpPut httpPut = new HttpPut(url);
                if (bodyParams != null) {
                    httpPut.setEntity(new StringEntity(JsonUtil.toJson(bodyParams),
                            ContentType.create(ContentType.APPLICATION_JSON.getMimeType(), encoding)));
                }
                request = httpPut;
                break;
            case DELETE:
                request = new HttpDelete(url);
                break;
        }
        if (request != null) {
            if (headers != null) {
                for (Map.Entry<String, String> header : headers.entrySet()) {
                    request.setHeader(header.getKey(), header.getValue());
                }
            }
            // 设置httpclient请求超时时间
            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectionRequestTimeout(3000) // 连接请求超时时间
                    .setConnectTimeout(6000) // 连接超时时间
                    .setSocketTimeout(15000) // 读取超时时间
                    .build();
            request.setConfig(requestConfig);
            return HTTP_CLIENT.execute(request);
        }
        return null;
    }

    public static Binary<Integer, String> request(HttpMethod method, String url, Map<String, Object> params, Object bodyParams,
                                                  Map<String, String> headers, String encoding) throws Exception {
        CloseableHttpResponse response = null;
        try {
            response = execute(method, url, params, bodyParams, headers, encoding);
            if (response != null) {
                int statusCode = response.getStatusLine().getStatusCode();
                String content = EntityUtils.toString(response.getEntity(), encoding);
                if (statusCode != HttpStatus.SC_OK) {
                    LogUtil.error(HttpClientUtil.class, content);
                }
                return new Binary<>(statusCode, content);
            }
        } finally { // 确保关闭请求连接
            if (response != null) {
                response.close();
            }
        }
        return null;
    }

    public static void download(String url, Map<String, Object> params, Map<String, String> headers,
            BiConsumer<HttpEntity, Map<String, String>> consumer) throws IOException {
        try (CloseableHttpResponse response = execute(HttpMethod.GET, url, params, null, headers, Strings.ENCODING_UTF8)) {
            if (response != null) {
                StatusLine statusLine = response.getStatusLine();
                if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                    Map<String, String> responseHeaders = new HashMap<>();
                    for (Header header : response.getAllHeaders()) {
                        responseHeaders.put(header.getName(), header.getValue());
                    }
                    consumer.accept(response.getEntity(), responseHeaders);
                } else {
                    LogUtil.error(HttpClientUtil.class,
                            "====== " + statusLine + HttpMethod.GET.name() + Strings.SPACE + url);
                }
            }
        } catch (Exception e) {
            if (e instanceof IOException) {
                throw (IOException) e;
            }
            Throwable cause = e.getCause();
            if (cause instanceof IOException) {
                throw (IOException) cause;
            }
            LogUtil.error(HttpClientUtil.class, e);
        }
    }

    public static String commonRequest(HttpMethod method, String url, Map<String, Object> params, Object bodyParams,
                                       Map<String, String> headers) throws Exception {
        Binary<Integer, String> resBinary = request(method, url, params, bodyParams, headers, Strings.ENCODING_UTF8);
        return resBinary == null ? null : resBinary.getRight();
    }

    public static String requestByGet(String url, Map<String, Object> params, Map<String, String> headers)
            throws Exception {
        return commonRequest(HttpMethod.GET, url, params,null, headers);
    }

    public static String requestByPost(String url, Object bodyParams, Map<String, String> headers)
            throws Exception {
        return commonRequest(HttpMethod.POST, url, null, bodyParams, headers);
    }
}
