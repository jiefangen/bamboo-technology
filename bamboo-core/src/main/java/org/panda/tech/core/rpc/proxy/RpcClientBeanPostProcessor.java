package org.panda.tech.core.rpc.proxy;

import org.apache.commons.lang3.StringUtils;
import org.panda.bamboo.common.util.LogUtil;
import org.panda.bamboo.common.util.lang.StringUtil;
import org.panda.tech.core.rpc.annotation.RpcClient;
import org.panda.tech.core.rpc.client.RpcClientInvoker;
import org.panda.tech.core.rpc.client.RpcClientReq;
import org.panda.tech.core.rpc.filter.RpcInvokeInterceptor;
import org.panda.tech.core.rpc.serializer.RpcSerializer;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * RPC接口动态代理创建
 *
 * @author fangen
 **/
@Deprecated
public class RpcClientBeanPostProcessor implements BeanDefinitionRegistryPostProcessor {

    // RPC代理客户端容器
    private final Map<String, RpcClientReq> rpcProxyClients = new HashMap<>();

    @Autowired(required = false)
    private RpcInvokeInterceptor interceptor;

    @Autowired(required = false)
    private RpcSerializer rpcSerializer;

//    public RpcClientBeanPostProcessor(RpcSerializer rpcSerializer) {
//        this.rpcSerializer = rpcSerializer;
//    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        // 扫描带有@RpcClient注解的接口，并动态注册代理对象
        ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false) {
            @Override
            protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
                return beanDefinition.getMetadata().isInterface(); // 仅扫描接口
            }
        };
        scanner.addIncludeFilter(new AnnotationTypeFilter(RpcClient.class));
        String basePackage = "org.panda.tech.core.config.security.executor.strategy.client";
        Set<BeanDefinition> beanDefinitions = scanner.findCandidateComponents(basePackage);
        for (BeanDefinition bean : beanDefinitions) {
            try {
                Class<?> targetClass = Class.forName(bean.getBeanClassName());
                RpcClient rpcClient = targetClass.getAnnotation(RpcClient.class);
                String beanId = StringUtils.isEmpty(rpcClient.beanId()) ?
                        StringUtil.firstToLowerCase(targetClass.getSimpleName()) : rpcClient.beanId();
                RpcClientReq rpcClientInvoker = new RpcClientInvoker(rpcClient.value(), beanId, rpcClient.internal());
                rpcClientInvoker.setSerializer(rpcSerializer);
                RpcInvocationHandler rpcInvocationHandler = new RpcInvocationHandler(rpcClientInvoker);
                rpcInvocationHandler.setInterceptor(this.interceptor);
                rpcInvocationHandler.setBeanId(beanId);

//                RpcClientReq targetProxy = BeanUtil.createProxy(rpcClientInvoker, rpcInvocationHandler);
//                if (!rpcProxyClients.containsKey(beanId)) {
//                    rpcProxyClients.put(beanId, targetProxy);
//                }

                ProxyFactoryBean proxyFactoryBean = new ProxyFactoryBean();
                proxyFactoryBean.setInterfaces(targetClass);
                proxyFactoryBean.setTarget(rpcClientInvoker);
                proxyFactoryBean.setInterceptorNames("rpcInvocationHandler");
                proxyFactoryBean.setSingleton(true);

                registry.registerBeanDefinition(beanId, BeanDefinitionBuilder.genericBeanDefinition(ProxyFactoryBean.class)
                        .addPropertyValue("target", rpcClientInvoker)
                        .addPropertyValue("interceptorNames", "rpcInvocationHandler")
                        .getBeanDefinition());
            } catch (ClassNotFoundException e) {
                LogUtil.error(getClass(), e);
            }

        }
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
    }

    public Map<String, RpcClientReq> getRpcProxyClients() {
        return rpcProxyClients;
    }

}
