package org.panda.tech.core.util.http;

import okhttp3.*;
import org.panda.bamboo.common.constant.basic.Strings;
import org.panda.bamboo.common.util.LogUtil;
import org.panda.bamboo.common.util.jackson.JsonUtil;
import org.panda.tech.core.web.util.NetUtil;
import org.springframework.http.HttpMethod;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * OKHttp3客户端工具类
 * 建议场景：具备高性能和并发，异步请求支持、自定义拦截机制
 */
public class OKHttpUtil {

    public static final OkHttpClient OK_HTTP_CLIENT = new OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .build();
    private static final MediaType TYPE_JSON = MediaType.parse("application/json; charset=utf-8");

    private OKHttpUtil() {
    }

    @SuppressWarnings("unchecked")
    private static Response execute(HttpMethod method, String url, Object params,
                                    Map<String, String> headerMap) throws Exception {
        Request.Builder requestBuilder = new Request.Builder();
        if (headerMap != null && !headerMap.isEmpty()) {
            for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                requestBuilder.addHeader(entry.getKey(), entry.getValue());
            }
        }
        Request request = null;
        switch (method) {
            case GET:
                if (params instanceof Map) {
                    Map<String, Object> paramsMap = (Map<String, Object>) params;
                    if (!paramsMap.isEmpty()) {
                        url = NetUtil.mergeParams(url, paramsMap, Strings.ENCODING_UTF8);
                    }
                }
                request = requestBuilder.url(url).get().build();
                break;
            case POST:
                RequestBody requestBody = RequestBody.create(JsonUtil.toJson(params), TYPE_JSON);
                request = requestBuilder.url(url).post(requestBody).build();;
                break;
        }
        if (request != null) {
            return OK_HTTP_CLIENT.newCall(request).execute();
        }
        return null;
    }

    public static String request(HttpMethod method, String url, Object params, Map<String, String> headerMap)
            throws Exception {
        Response response = execute(method, url, params, headerMap);
        if (response != null) {
            try {
                String responseBody = Objects.requireNonNull(response.body()).string();
                if (!response.isSuccessful()) {
                    LogUtil.error(OKHttpUtil.class, responseBody);
                }
                return responseBody;
            } finally {
                // 确保关闭请求连接
                response.close();
            }
        }
        return Strings.STR_NULL;
    }


    public static String requestByGet(String url, Map<String, Object> params, Map<String, String> headers) throws Exception {
        return request(HttpMethod.GET, url, params, headers);
    }

    public static String requestByPost(String url, Object params, Map<String, String> headers) throws Exception {
        return request(HttpMethod.POST, url, params, headers);
    }

}
