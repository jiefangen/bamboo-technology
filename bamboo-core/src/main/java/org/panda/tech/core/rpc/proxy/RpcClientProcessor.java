package org.panda.tech.core.rpc.proxy;

import org.apache.commons.lang3.StringUtils;
import org.panda.bamboo.common.util.clazz.BeanUtil;
import org.panda.bamboo.common.util.lang.StringUtil;
import org.panda.tech.core.rpc.annotation.RpcClient;
import org.panda.tech.core.rpc.aop.RpcClientAspect;
import org.panda.tech.core.rpc.client.RpcClientInvoker;
import org.panda.tech.core.rpc.client.RpcClientReq;
import org.panda.tech.core.rpc.filter.RpcInvokeInterceptor;
import org.panda.tech.core.rpc.serializer.JsonRpcSerializer;
import org.panda.tech.core.rpc.serializer.RpcSerializer;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Import;

import java.util.HashMap;
import java.util.Map;

/**
 * RPC接口代理预加载
 *
 * @author fangen
 **/
@Import({JsonRpcSerializer.class, RpcClientAspect.class})
public class RpcClientProcessor implements BeanPostProcessor {

    // RPC代理客户端容器
    private final Map<String, RpcClientReq> rpcProxyClients = new HashMap<>();

    @Autowired(required = false)
    private RpcInvokeInterceptor interceptor;

    private final RpcSerializer rpcSerializer;

    public RpcClientProcessor(RpcSerializer rpcSerializer) {
        this.rpcSerializer = rpcSerializer;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean.getClass().isAnnotationPresent(RpcClient.class)) {
            Class<?> targetClass = bean.getClass();
            // RPC客户端解析
            RpcClient rpcClient = targetClass.getAnnotation(RpcClient.class);
            String beanId = StringUtils.isEmpty(rpcClient.beanId()) ?
                    StringUtil.firstToLowerCase(targetClass.getSimpleName()) : rpcClient.beanId();
            RpcClientReq rpcClientInvoker = new RpcClientInvoker(beanId, rpcClient.internal());
            rpcClientInvoker.setCommMode(rpcClient.mode());
            rpcClientInvoker.setSerializer(rpcSerializer);
            RpcInvocationHandler rpcInvocationHandler = new RpcInvocationHandler(rpcClientInvoker);
            rpcInvocationHandler.setInterceptor(this.interceptor);
            rpcInvocationHandler.setBeanId(beanId);
            RpcClientReq targetProxy = BeanUtil.createProxy(rpcClientInvoker, rpcInvocationHandler);
            if (!rpcProxyClients.containsKey(beanId)) {
                rpcProxyClients.put(beanId, targetProxy);
            }
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    public Map<String, RpcClientReq> getRpcProxyClients() {
        return rpcProxyClients;
    }

}
