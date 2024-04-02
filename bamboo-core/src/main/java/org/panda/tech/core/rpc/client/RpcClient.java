package org.panda.tech.core.rpc.client;

import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.reflect.Parameter;
import java.util.List;

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
    <T> T invoke(RequestMethod method, String path, Parameter[] parameters, Object[] args, Class<T> resultType)
            throws Exception;

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
    <T> List<T> invoke4List(RequestMethod method, String path, Parameter[] parameters,  Object[] args, Class<T> resultElementType)
            throws Exception;

}
