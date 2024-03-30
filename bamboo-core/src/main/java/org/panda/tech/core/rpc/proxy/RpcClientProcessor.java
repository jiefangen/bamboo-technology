package org.panda.tech.core.rpc.proxy;

import org.panda.tech.core.rpc.annotation.RpcClient;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * RPC接口代理初始化加载
 *
 * @author fangen
 **/
@Component
public class RpcClientProcessor implements BeanPostProcessor {

    private final Map<String, Object> rpcClientBeans = new HashMap<>();

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean.getClass().isAnnotationPresent(RpcClient.class)) {
            // 在这里执行你想要的逻辑，例如注册到 RPC 客户端
            // 例如：
            // RpcClient rpcClientAnnotation = bean.getClass().getAnnotation(RpcClient.class);
            // String serviceName = rpcClientAnnotation.value();
            // RpcClientRegistry.register(bean, serviceName);
            System.out.println("-----" + beanName);
            // Object targetProxy = BeanUtil.createProxy(target, new RpcInvocationHandler(rpcClientInvoker, target));
        }
        return bean;
    }

}
