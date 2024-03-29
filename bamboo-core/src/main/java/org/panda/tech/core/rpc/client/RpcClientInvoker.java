package org.panda.tech.core.rpc.client;

import org.panda.bamboo.common.constant.basic.Strings;
import org.panda.bamboo.common.model.tuple.Binate;
import org.panda.tech.core.exception.business.BusinessException;
import org.panda.tech.core.rpc.serializer.RpcSerializer;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * RPC客户端调用器
 */
public class RpcClientInvoker extends ClientRequestSupport implements RpcClient {
    /**
     * 服务端URL根路径
     */
    private String serverUrlRoot;
    /**
     * RPC序列化器
     */
    private RpcSerializer serializer;

    public RpcClientInvoker(String serverUrlRoot) {
        this.serverUrlRoot = serverUrlRoot;
    }

    public void setSerializer(RpcSerializer serializer) {
        this.serializer = serializer;
    }

    private String getInvokeUrl(String beanId, String methodName) {
        if (this.serverUrlRoot.endsWith(Strings.SLASH)) { // 兼容以斜杠结尾的服务根路径
            this.serverUrlRoot = this.serverUrlRoot.substring(0, this.serverUrlRoot.length() - Strings.SLASH.length());
        }
        return this.serverUrlRoot + "/rpc/invoke/" + beanId + "/" + methodName;
    }

    private Map<String, Object> getInvokeParams(Object[] args) throws Exception {
        final Map<String, Object> params = new HashMap<>();
        if (args.length > 0) {
            params.put("args", this.serializer.serialize(args));
        }
        return params;
    }

    /**
     * 获取指定URI的响应内容
     *
     * @param url
     *            请求
     * @return 响应内容
     * @throws Exception
     *             如果响应中有错误
     */
    private String requestContent(String url, Map<String, Object> params)
            throws Exception {
        Binate<Integer, String> response = request(url, params);
        int statusCode = response.getLeft();
        String content = response.getRight();
        if (statusCode == HttpServletResponse.SC_OK) {
            return content;
        }
        throw new BusinessException(content);
    }

    @Override
    public <T> T invoke(String beanId, String methodName, Object[] args, Class<T> resultType) throws Exception {
        String url = getInvokeUrl(beanId, methodName);
        Map<String, Object> params = getInvokeParams(args);
        String response = requestContent(url, params);
        return this.serializer.deserialize(response, resultType);
    }

    @Override
    public <T> List<T> invoke4List(String beanId, String methodName, Object[] args, Class<T> resultElementType)
            throws Exception {
        String url = getInvokeUrl(beanId, methodName);
        Map<String, Object> params = getInvokeParams(args);
        String response = requestContent(url, params);
        return this.serializer.deserializeList(response, resultElementType);
    }

    private Map<String, Object> getInvokeParams(Map<String, Object> args) throws Exception {
        Map<String, Object> params = new HashMap<>();
        for (Entry<String, Object> entry : args.entrySet()) {
            params.put(entry.getKey(), this.serializer.serialize(entry.getValue()));
        }
        return params;
    }

    @Override
    public <T> T invoke(String beanId, String methodName, Map<String, Object> args, Class<T> resultType) throws Exception {
        String url = getInvokeUrl(beanId, methodName);
        Map<String, Object> params = getInvokeParams(args);
        String response = requestContent(url, params);
        return this.serializer.deserialize(response, resultType);
    }

    @Override
    public <T> List<T> invoke4List(String beanId, String methodName, Map<String, Object> args, Class<T> resultElementType)
            throws Exception {
        String url = getInvokeUrl(beanId, methodName);
        Map<String, Object> params = getInvokeParams(args);
        String response = requestContent(url, params);
        return this.serializer.deserializeList(response, resultElementType);
    }
}
