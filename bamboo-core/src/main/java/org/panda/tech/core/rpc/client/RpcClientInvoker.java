package org.panda.tech.core.rpc.client;

import org.panda.bamboo.common.constant.basic.Strings;
import org.panda.tech.core.crypto.aes.AesEncryptor;
import org.panda.tech.core.crypto.sha.ShaEncryptor;
import org.panda.tech.core.rpc.RpcConstants;
import org.panda.tech.core.rpc.serializer.RpcSerializer;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * RPC客户端调用器
 */
public class RpcClientInvoker extends ClientRequestSupport implements RpcClientReq {
    /**
     * 服务端URL根路径
     */
    private String serverUrlRoot;
    /**
     * 内部调用
     */
    private final boolean internal;
    /**
     * bean Id
     */
    private String beanId;
    /**
     * RPC序列化器
     */
    private RpcSerializer serializer;

    public RpcClientInvoker(String serverUrlRoot, boolean internal) {
        this.serverUrlRoot = serverUrlRoot;
        this.internal = internal;
    }

    public void setBeanId(String beanId) {
        this.beanId = beanId;
    }

    public void setSerializer(RpcSerializer serializer) {
        this.serializer = serializer;
    }

    private String getInvokeUrl(String uri) {
        if (this.serverUrlRoot.endsWith(Strings.SLASH)) { // 兼容以斜杠结尾的服务根路径
            this.serverUrlRoot = this.serverUrlRoot.substring(0, this.serverUrlRoot.length() - Strings.SLASH.length());
        }
        if (!uri.startsWith(Strings.SLASH)) {
            uri = Strings.SLASH + uri;
        }
        return this.serverUrlRoot + RpcConstants.URL_RPC_PREFIX + uri;
    }

    private Map<String, Object> getInvokeParams(Parameter[] parameters, Object[] args) throws Exception {
        Map<String, Object> rpcParams = new HashMap<>();
        Map<String, Object> headers = new HashMap<>();
        if (this.internal) { // RPC内部通信凭证
            AesEncryptor aesEncryptor = new AesEncryptor();
            String secretKey = aesEncryptor.encrypt(this.beanId, RpcConstants.CREDENTIALS_KEY);
            ShaEncryptor shaEncryptor = new ShaEncryptor();
            headers.put(RpcConstants.HEADER_RPC_TYPE, this.beanId);
            headers.put(RpcConstants.HEADER_RPC_CREDENTIALS, shaEncryptor.encrypt(secretKey));
        }
        if (parameters.length > 0 && args.length > 0) {
            Map<String, Object> paramMap = new HashMap<>();
            for (int i = 0; i < parameters.length; i++) {
                Parameter parameter = parameters[i];
                if (parameter.isAnnotationPresent(RequestHeader.class)) {
                    RequestHeader requestHeaderAnnotation = parameter.getAnnotation(RequestHeader.class);
                    String headerName = requestHeaderAnnotation.value();
                    headers.put(headerName, args[i]);
                }
                // 针对POST请求
                if (parameter.isAnnotationPresent(RequestBody.class)) {
                    rpcParams.put(RPC_KEY_PARAMS, args[i]);
                }
                // 针对GET请求参数
                if (parameter.isAnnotationPresent(RequestParam.class)) {
                    RequestParam requestHeaderAnnotation = parameter.getAnnotation(RequestParam.class);
                    String paramName = requestHeaderAnnotation.value();
                    paramMap.put(paramName, args[i]);
                }
            }
            if (!paramMap.isEmpty()) {
                rpcParams.put(RPC_KEY_PARAMS, paramMap);
            }
        }
        rpcParams.put(RPC_KEY_HEADERS, headers);
        return rpcParams;
    }

    @Override
    public <T> T invoke(RequestMethod method, String path, Parameter[] parameters, Object[] args, Class<T> resultType)
            throws Exception {
        Map<String, Object> rpcParams = getInvokeParams(parameters, args);
        String response = request(method, getInvokeUrl(path), rpcParams);
        return this.serializer.deserialize(response, resultType);
    }

    @Override
    public <T> List<T> invoke4List(RequestMethod method, String path, Parameter[] parameters,  Object[] args,
                                   Class<T> resultElementType) throws Exception {
        String url = getInvokeUrl(path);
        Map<String, Object> params = getInvokeParams(parameters, args);
        String response = request(method, url, params);
        return this.serializer.deserializeList(response, resultElementType);
    }

    private Map<String, Object> getInvokeParams(Map<String, Object> args) throws Exception {
        Map<String, Object> params = new HashMap<>();
        for (Entry<String, Object> entry : args.entrySet()) {
            params.put(entry.getKey(), this.serializer.serialize(entry.getValue()));
        }
        return params;
    }

}
