package org.panda.tech.core.rpc.proxy;

import org.apache.commons.lang3.StringUtils;
import org.panda.bamboo.common.util.clazz.BeanUtil;
import org.panda.tech.core.rpc.annotation.RpcClient;
import org.panda.tech.core.rpc.client.RpcClientInvoker;
import org.panda.tech.core.rpc.client.RpcClientReq;
import org.panda.tech.core.rpc.serializer.RpcSerializer;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private RpcSerializer rpcSerializer;

    private final Map<String, Object> rpcClientBeans = new HashMap<>();

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean.getClass().isAnnotationPresent(RpcClient.class)) {
            Class<?> targetClass = bean.getClass();
            // RPC客户端解析
            RpcClient rpcClient = targetClass.getAnnotation(RpcClient.class);
            RpcClientInvoker rpcClientInvoker = new RpcClientInvoker(rpcClient.value(), rpcClient.internal());
            rpcClientInvoker.setSerializer(rpcSerializer);
            String beanId = rpcClient.beanId();
            if (StringUtils.isEmpty(beanId)) { // RPC调用器代理缓存key
                beanId = beanName;
            }
            rpcClientInvoker.setBeanId(beanId);

            RpcClientReq rpcClientReq = new RpcClientInvoker(rpcClient.value(), rpcClient.internal());
            RpcClientReq targetProxy = BeanUtil.createProxy(rpcClientReq, new RpcInvocationHandler(rpcClientInvoker));
            rpcClientBeans.put(beanId, targetProxy);
        }
        return bean;
    }

}
