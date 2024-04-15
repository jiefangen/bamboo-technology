package org.panda.tech.core.rpc.client;

import org.panda.tech.core.rpc.constant.enums.CommMode;
import org.panda.tech.core.rpc.serializer.RpcSerializer;
import org.springframework.http.HttpMethod;

import java.lang.reflect.Parameter;

/**
 * RPC客户端
 */
public interface RpcClientReq {

    /**
     * 执行指定RPC方法
     *
     * @param path
     *            资源路径
     * @param parameters
     *            参数方法属性
     * @param args
     *            参数集
     * @param resultType
     *            期望的结果类型
     * @return 执行结果
     * @throws Exception
     *             如果执行过程中出现错误
     */
    <T> T invoke(HttpMethod method, String path, Parameter[] parameters, Object[] args, Class<T> resultType,
                 Class<?>[] subTypes) throws Exception;

    /**
     * 设置通信模式
     */
    void setCommMode(CommMode commMode);
    /**
     * 设置RPC序列化器
     */
    void setSerializer(RpcSerializer serializer);
    /**
     * 设置URL根路径
     */
    void setServerUrlRoot(String serverUrlRoot);

}
