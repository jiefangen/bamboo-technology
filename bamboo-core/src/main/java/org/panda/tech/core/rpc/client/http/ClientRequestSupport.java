package org.panda.tech.core.rpc.client.http;

import org.panda.bamboo.common.constant.basic.Strings;
import org.panda.bamboo.common.model.tuple.Binate;
import org.panda.tech.core.util.http.HttpClientUtil;
import org.springframework.http.HttpMethod;

import java.util.Map;

/**
 * 客户端请求支持（HTTP，gRPC）
 */
public class ClientRequestSupport {

    private String httpMethod = "POST";

    public void setMethod(String method) {
        this.httpMethod = method;
    }

    /**
     * 获取指定URI的响应结果
     *
     * @param request
     *            请求
     * @return 响应状态码-响应体内容
     * @throws Exception
     *             如果请求过程中有错误
     */
    public Binate<Integer, String> request(String url, Map<String, Object> params)
            throws Exception {
        return HttpClientUtil.request(HttpMethod.resolve(this.httpMethod), url, params, null,
                Strings.ENCODING_UTF8);
    }

}
