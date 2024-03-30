package org.panda.tech.core.rpc.client;

import java.util.List;
import java.util.Map;

/**
 * RPC客户端
 */
public interface RpcClient {

    /**
     * 执行指定RPC方法
     *
     * @param beanId
     *            Bean Id
     * @param path
     *            资源路径
     * @param args
     *            参数集
     * @param resultType
     *            期望的结果类型
     * @return 执行结果
     * @throws Exception
     *             如果执行过程中出现错误
     */
    <T> T invoke(String beanId, String path, Object[] args, Class<T> resultType) throws Exception;

    /**
     * 执行指定RPC方法得到清单型结果
     *
     * @param beanId
     *            Bean Id
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
    <T> List<T> invoke4List(String beanId, String path, Object[] args, Class<T> resultElementType)
            throws Exception;

    /**
     * 执行指定RPC方法
     *
     * @param beanId
     *            Bean Id
     * @param path
     *            资源路径
     * @param args
     *            有名称的参数映射集
     * @param resultType
     *            期望的结果类型
     * @return 执行结果
     * @throws Exception
     *             如果执行过程中出现错误
     */
    <T> T invoke(String beanId, String path, Map<String, Object> args, Class<T> resultType) throws Exception;

    /**
     * 执行指定RPC方法得到清单型结果
     * 
     * @param beanId
     *            Bean Id
     * @param path
     *            资源路径
     * @param args
     *            有名称的参数映射集
     * @param resultElementType
     *            期望的结果清单中的元素类型
     * @return 执行结果
     * @throws Exception
     *             如果执行过程中出现错误
     */
    <T> List<T> invoke4List(String beanId, String path, Map<String, Object> args, Class<T> resultElementType)
            throws Exception;

}
