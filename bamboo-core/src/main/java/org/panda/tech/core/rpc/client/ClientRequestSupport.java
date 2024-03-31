package org.panda.tech.core.rpc.client;

import org.panda.bamboo.common.constant.basic.Strings;
import org.panda.tech.core.util.http.HttpClientUtil;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

/**
 * 客户端请求支持（HTTP，gRPC）
 */
public class ClientRequestSupport {

    protected static final String RPC_KEY_HEADERS = "headers";

    protected static final String RPC_KEY_PARAMS = "params";

    /**
     * 获取指定URI的响应结果
     *
     * @param method
     *            请求方法
     * @return 响应状态码-响应体内容
     * @throws Exception
     *             如果请求过程中有错误
     */
    @SuppressWarnings("unchecked")
    public String request(RequestMethod method, String url, Map<String, Object> rpcParams)
            throws Exception {
        Map<String, String> headers = (Map<String, String>) rpcParams.get(RPC_KEY_HEADERS);
        Object params = rpcParams.get(RPC_KEY_PARAMS);
        return HttpClientUtil.commonRequest(method, url, params, headers, Strings.ENCODING_UTF8);
    }

}
