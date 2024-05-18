package org.panda.tech.core.rpc.aop;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.panda.bamboo.common.util.lang.StringUtil;
import org.panda.bamboo.core.context.SpringContextHolder;
import org.panda.tech.core.config.CommonProperties;
import org.panda.tech.core.rpc.annotation.RpcClient;
import org.panda.tech.core.rpc.annotation.RpcEnv;
import org.panda.tech.core.rpc.annotation.RpcMethod;
import org.panda.tech.core.rpc.client.RpcClientReq;
import org.panda.tech.core.rpc.constant.RpcConstants;
import org.panda.tech.core.rpc.constant.exception.RpcInvokerException;
import org.panda.tech.core.rpc.proxy.RpcClientProcessor;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Map;

/**
 * RPC客户端切面
 *
 * @author fangen
 **/
@Aspect
public class RpcClientAspect {

    private final CommonProperties commonProperties;
    private final RpcClientProcessor rpcClientProcessor;

    public RpcClientAspect(RpcClientProcessor rpcClientProcessor, CommonProperties commonProperties) {
        this.rpcClientProcessor = rpcClientProcessor;
        this.commonProperties = commonProperties;
    }

    @Pointcut("@within(rpcClient) || @annotation(rpcClient)")
    public void rpcClientPointCut(RpcClient rpcClient) {
    }

    @Around("rpcClientPointCut(org.panda.tech.core.rpc.annotation.RpcClient)")
    public Object aroundRpcClientMethods(ProceedingJoinPoint joinPoint) throws Throwable {
        Class<?> targetClass = joinPoint.getTarget().getClass();
        // RPC客户端解析
        RpcClient rpcClient = targetClass.getAnnotation(RpcClient.class);
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        RpcMethod rpcMethod = method.getAnnotation(RpcMethod.class);
        if (rpcClient == null || rpcMethod == null) {
            throw new RpcInvokerException(RpcConstants.EXC_RPC_NOT_CONFIG);
        }

        String beanId = StringUtils.isEmpty(rpcClient.beanId()) ?
                StringUtil.firstToLowerCase(targetClass.getSimpleName()) : rpcClient.beanId();
        String env = SpringContextHolder.getActiveProfile();
        RpcEnv[] rpcEnvArr = rpcClient.values();
        String urlRoot = null;
        if (rpcEnvArr.length == 0) { // RPC客户端注解未配置默认走认证授权服务
            Map<String, String> serviceRoot = commonProperties.getServiceRoot();
            urlRoot = serviceRoot.get(beanId);
            if (StringUtils.isEmpty(urlRoot) && beanId.startsWith("auth")) {
                Map<String, String> authEnvs = commonProperties.getAuthEnvs();
                urlRoot = authEnvs.get(env);
            }
        } else {
            for (RpcEnv rpcEnv : rpcEnvArr) {
                if (env.equals(rpcEnv.active())) {
                    urlRoot = rpcEnv.value();
                    break;
                }
            }
        }
        if (StringUtils.isEmpty(urlRoot)) {
            throw new RpcInvokerException(RpcConstants.EXC_RPC_ILLEGAL_ROOT);
        }

        // 获取PRC动态代理
        RpcClientReq targetProxy = rpcClientProcessor.getRpcProxyClients().get(beanId);
        if (targetProxy == null) {
            throw new RpcInvokerException(RpcConstants.EXC_RPC_ILLEGAL_BEAN);
        }
        targetProxy.setServerUrlRoot(urlRoot);
        // 获取方法的参数列表
        Parameter[] parameters = method.getParameters();
        // 获取方法参数值
        Object[] args = joinPoint.getArgs();
        Class<?> returnType = methodSignature.getReturnType();
        return targetProxy.invoke(rpcMethod.method(), rpcMethod.value(), parameters, args, returnType, rpcMethod.subTypes());
    }

}
