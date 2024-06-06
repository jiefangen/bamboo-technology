package org.panda.tech.core.rpc.client;

import org.panda.tech.core.util.http.client.RestTemplateClient;
import org.panda.tech.core.rpc.constant.enums.CommMode;
import org.panda.tech.core.rpc.constant.exception.RpcInvokerException;
import org.panda.tech.core.rpc.serializer.RpcSerializer;
import org.panda.tech.core.util.http.HttpClientUtil;
import org.springframework.http.HttpMethod;

import java.util.Map;

/**
 * 客户端请求支持（HTTP，gRPC）
 */
public class ClientRequestSupport {

    protected RpcSerializer serializer;

    // 通信模式：1-原生HttpClient；2-RestTemplate
    private CommMode commMode;

    public void setCommMode(CommMode commMode) {
        this.commMode = commMode;
    }

    public void setSerializer(RpcSerializer serializer) {
        this.serializer = serializer;
    }

    /**
     * 获取指定URI的响应结果
     *
     * @param method 请求方法
     * @param url 请求URL
     * @param params 参数
     * @param bodyParams body参数
     * @param headers 请求头
     * @return 请求结果
     * @throws Exception 如果请求过程中有错误
     */
    public String request(HttpMethod method, String url, Map<String, Object> params, Object bodyParams,
                          Map<String, String> headers) throws Exception {
        switch (this.commMode) {
            case HTTP_CLIENT:
                return HttpClientUtil.commonRequest(method, url, params, bodyParams, headers);
            case REST_TEMPLATE:
                return RestTemplateClient.request(method, url, params, serializer.serialize(bodyParams), headers);
        }
        throw new RpcInvokerException();
    }

}
