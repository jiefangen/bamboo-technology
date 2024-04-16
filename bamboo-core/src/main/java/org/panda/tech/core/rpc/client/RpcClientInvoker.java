package org.panda.tech.core.rpc.client;

import org.panda.bamboo.common.constant.basic.Strings;
import org.panda.bamboo.common.util.jackson.JsonUtil;
import org.panda.tech.core.crypto.aes.AesEncryptor;
import org.panda.tech.core.crypto.sha.ShaEncryptor;
import org.panda.tech.core.rpc.constant.RpcConstants;
import org.panda.tech.core.web.restful.RestfulResult;
import org.panda.tech.data.model.query.Paged;
import org.panda.tech.data.model.query.QueryResult;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;

/**
 * RPC客户端调用器
 */
public class RpcClientInvoker extends ClientRequestSupport implements RpcClientReq {

    private static final String RPC_KEY_HEADERS = "headers";
    private static final String RPC_KEY_PARAMS = "params";
    private static final String RPC_KEY_BODY_PARAMS = "bodyParams";

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
    private final String beanId;

    public RpcClientInvoker(String beanId, boolean internal) {
        this.beanId = beanId;
        this.internal = internal;
    }

    public void setServerUrlRoot(String serverUrlRoot){
        this.serverUrlRoot = serverUrlRoot;
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
                if (parameter.isAnnotationPresent(RequestBody.class)) {
                    rpcParams.put(RPC_KEY_BODY_PARAMS, args[i]);
                }
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

    @SuppressWarnings("unchecked")
    @Override
    public <T> T invoke(HttpMethod method, String path, Parameter[] parameters, Object[] args, Class<T> resultType,
                        Class<?>[] subTypes) throws Exception {
        Map<String, Object> rpcParams = getInvokeParams(parameters, args);
        Map<String, Object> params = (Map<String, Object>) rpcParams.get(RPC_KEY_PARAMS);
        Map<String, String> headers = (Map<String, String>) rpcParams.get(RPC_KEY_HEADERS);
        String response = request(method, getInvokeUrl(path), params, rpcParams.get(RPC_KEY_BODY_PARAMS), headers);
        if (subTypes.length > 0) { // 多层嵌套数据类型解析
            if (RestfulResult.class.isAssignableFrom(resultType)) { // 内部规范返回，可反序列化内层数据
                RestfulResult<?> restfulResult = this.serializer.deserialize(response, RestfulResult.class);
                if (restfulResult.isSuccess() && restfulResult.getData() != null) {
                    String dataStr = JsonUtil.toJson(restfulResult.getData());
                    return (T) RestfulResult.success(this.serializer.deserializeBean(dataStr, subTypes[0],
                            subTypes.length > 1 ? subTypes[1] : null));
                }
            } else if (QueryResult.class.isAssignableFrom(resultType)) {
                QueryResult<?> queryResult = this.serializer.deserialize(response, QueryResult.class);
                Paged paged = queryResult.getPaged();
                if (paged.getTotal() > 0L) {
                    String records = JsonUtil.toJson(queryResult.getRecords());
                    return (T) new QueryResult<>(this.serializer.deserializeList(records, subTypes[0]), paged);
                }
            }
        }
        return (T) this.serializer.deserializeBean(response, resultType, subTypes.length > 0 ? subTypes[0] : null);
    }

}
