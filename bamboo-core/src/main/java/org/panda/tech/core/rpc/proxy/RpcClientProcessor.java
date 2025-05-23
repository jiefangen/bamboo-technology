package org.panda.tech.core.rpc.proxy;

import org.apache.commons.lang3.StringUtils;
import org.panda.bamboo.common.constant.basic.Strings;
import org.panda.bamboo.common.util.clazz.BeanUtil;
import org.panda.bamboo.common.util.lang.StringUtil;
import org.panda.tech.core.config.CommonProperties;
import org.panda.tech.core.config.app.AppConstants;
import org.panda.tech.core.rpc.annotation.RpcClient;
import org.panda.tech.core.rpc.annotation.RpcEnv;
import org.panda.tech.core.rpc.aop.RpcClientAspect;
import org.panda.tech.core.rpc.client.RpcClientInvoker;
import org.panda.tech.core.rpc.client.RpcClientReq;
import org.panda.tech.core.rpc.filter.RpcInvokeInterceptor;
import org.panda.tech.core.rpc.serializer.JsonRpcSerializer;
import org.panda.tech.core.rpc.serializer.RpcSerializer;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    @Value(AppConstants.EL_SPRING_PROFILES_ACTIVE)
    private String env;

    private final CommonProperties commonProperties;
    private final RpcSerializer rpcSerializer;

    public RpcClientProcessor(RpcSerializer rpcSerializer, CommonProperties commonProperties) {
        this.rpcSerializer = rpcSerializer;
        this.commonProperties = commonProperties;
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
            // 配置调用服务根路径
            RpcEnv[] rpcEnvArr = rpcClient.values();
            String urlRoot = null;
            if (rpcEnvArr.length == 0 && beanId.startsWith("auth")) { // RPC客户端注解未配置默认走认证授权服务
                Map<String, String> authEnvs = commonProperties.getAuthEnvs();
                urlRoot = authEnvs.get(env);
            } else {
                for (RpcEnv rpcEnv : rpcEnvArr) {
                    if (env.equals(rpcEnv.active())) {
                        urlRoot = rpcEnv.value();
                        break;
                    }
                }
            }
            if (StringUtils.isNotEmpty(rpcClient.serviceName())) { // 目标服务名称缓存到容器中
                Map<String, String> appServiceNames = commonProperties.getAppServiceNames();
                appServiceNames.put(beanId, rpcClient.serviceName() + Strings.MINUS + env);
            }
            if (StringUtils.isNotEmpty(urlRoot)) { // 目标根路径缓存到容器中
                Map<String, String> serviceRoots = commonProperties.getServiceRoots();
                serviceRoots.put(beanId, urlRoot);
                rpcClientInvoker.setServerUrlRoot(urlRoot);
            }
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
