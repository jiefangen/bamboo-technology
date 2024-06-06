package org.panda.tech.core.util.http.client;


import org.panda.bamboo.common.constant.basic.Strings;
import org.panda.bamboo.common.util.LogUtil;
import org.panda.bamboo.core.context.SpringContextHolder;
import org.panda.tech.core.web.util.NetUtil;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * RestTemplate客户端
 */
public class RestTemplateClient {

    public static RestTemplate restTemplate;

    static {
        restTemplate = SpringContextHolder.getBean(RestTemplate.class);
    }

    private static ResponseEntity<String> exchange(HttpMethod method, String url, Map<String, Object> params,
                                                   Object bodyParams, Map<String, String> headers) {
        if (params != null && !params.isEmpty()) {
            url = NetUtil.mergeParams(url, params, Strings.ENCODING_UTF8);
        }
        // 设置请求头
        HttpHeaders httpHeaders = new HttpHeaders();
        if (headers != null) {
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                httpHeaders.add(entry.getKey(), entry.getValue());
            }
        }
        HttpEntity<Object> requestEntity = new HttpEntity<>(bodyParams, httpHeaders);
        return restTemplate.exchange(url, method, requestEntity, String.class);
    }

    public static String request(HttpMethod method, String url, Map<String, Object> params, Object bodyParams,
                                                  Map<String, String> headers) throws Exception {
        ResponseEntity<String> response = exchange(method, url, params, bodyParams, headers);
        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        } else {
            LogUtil.error(RestTemplateClient.class, "Remote call failure，response: {}", response);
        }
        return null;
    }

    public static String requestByGet(String url, Map<String, Object> params, Map<String, String> headers) throws Exception {
        return request(HttpMethod.GET, url, params, null,  headers);
    }

    public static String requestByPost(String url, Object bodyParams, Map<String, String> headers) throws Exception {
        return request(HttpMethod.POST, url, null, bodyParams, headers);
    }

}
