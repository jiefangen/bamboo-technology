package org.panda.tech.core.rpc.proxy;

import org.apache.commons.lang3.StringUtils;
import org.panda.bamboo.common.util.LogUtil;
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
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.Import;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * RPC接口动态代理创建
 *
 * @author fangen
 **/
@Import({JsonRpcSerializer.class, RpcClientAspect.class})
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
                // RPC客户端解析
                RpcClient rpcClient = targetClass.getAnnotation(RpcClient.class);
                String beanId = rpcClient.beanId();
                if (StringUtils.isEmpty(beanId)) { // RPC调用器代理缓存key
                    beanId = StringUtil.firstToLowerCase(targetClass.getSimpleName());
                }
                RpcClientReq rpcClientInvoker = new RpcClientInvoker(rpcClient.value(), beanId, rpcClient.internal());
                rpcClientInvoker.setSerializer(rpcSerializer);
                RpcInvocationHandler rpcInvocationHandler = new RpcInvocationHandler(rpcClientInvoker);
                rpcInvocationHandler.setInterceptor(this.interceptor);
                rpcInvocationHandler.setBeanId(beanId);
                RpcClientReq targetProxy = BeanUtil.createProxy(rpcClientInvoker, rpcInvocationHandler);
                if (!rpcProxyClients.containsKey(beanId)) {
                    rpcProxyClients.put(beanId, targetProxy);
                }
//                BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(targetClass, () -> {
//                    return Proxy.newProxyInstance(
//                            targetClass.getClassLoader(),
//                            new Class[]{targetClass},
//                            new RpcClientInvoker()
//                    );
//                });
//                registry.registerBeanDefinition(beanId, builder.getRawBeanDefinition());
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
