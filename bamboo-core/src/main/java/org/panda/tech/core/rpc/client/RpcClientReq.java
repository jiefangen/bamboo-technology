package org.panda.tech.core.rpc.client;

import org.panda.tech.core.rpc.constant.enums.CommMode;
import org.panda.tech.core.rpc.serializer.RpcSerializer;
import org.springframework.http.HttpMethod;

import java.lang.reflect.Parameter;
import java.util.List;

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
    <T> T invoke(HttpMethod method, String path, Parameter[] parameters, Object[] args, Class<T> resultType)
            throws Exception;

    /**
     * 执行指定RPC方法得到清单型结果
     *
     * @param parameters
     *            参数方法属性
     * @param path
     *            资源路径
     * @param args
     *            参数集
     * @param resultElementType
     *            期望的结果清单中的元素类型
     * @return 清单型执行结果
     * @throws Exception
     *             如果执行过程中出现错误
     */
    <T> List<T> invoke4List(HttpMethod method, String path, Parameter[] parameters,  Object[] args, Class<T> resultElementType)
            throws Exception;

    /**
     * 设置通信模式
     */
    void setCommMode(CommMode commMode);

    /**
     * 设置RPC序列化器
     */
    void setSerializer(RpcSerializer serializer);

}
