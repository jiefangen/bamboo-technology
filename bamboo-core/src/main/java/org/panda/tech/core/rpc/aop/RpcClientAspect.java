package org.panda.tech.core.rpc.aop;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.panda.bamboo.common.util.lang.StringUtil;
import org.panda.tech.core.rpc.constant.RpcConstants;
import org.panda.tech.core.rpc.annotation.RpcClient;
import org.panda.tech.core.rpc.annotation.RpcMethod;
import org.panda.tech.core.rpc.client.RpcClientReq;
import org.panda.tech.core.rpc.constant.exception.RpcInvokerException;
import org.panda.tech.core.rpc.proxy.RpcClientProcessor;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * RPC客户端切面
 *
 * @author fangen
 **/
@Aspect
public class RpcClientAspect {

    private final RpcClientProcessor rpcClientProcessor;

    public RpcClientAspect(RpcClientProcessor rpcClientProcessor) {
        this.rpcClientProcessor = rpcClientProcessor;
    }

    @Pointcut("@within(rpcClient) || @annotation(rpcClient)")
    public void rpcClientPointCut(RpcClient rpcClient) {
    }

    @Around("rpcClientPointCut(org.panda.tech.core.rpc.annotation.RpcClient)")
    public Object aroundRpcClientMethods(ProceedingJoinPoint joinPoint) throws Throwable {
        Class<?> targetClass = joinPoint.getTarget().getClass();
        // RPC客户端解析
        RpcClient rpcClient = targetClass.getAnnotation(RpcClient.class);
        String beanId = StringUtils.isEmpty(rpcClient.beanId()) ?
                StringUtil.firstToLowerCase(targetClass.getSimpleName()) : rpcClient.beanId();
        RpcClientReq targetProxy = rpcClientProcessor.getRpcProxyClients().get(beanId);
        if (targetProxy == null) {
            throw new RpcInvokerException(RpcConstants.EXC_RPC_ILLEGAL_BEAN);
        }

        // RPC方法解析
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        RpcMethod rpcMethod = method.getAnnotation(RpcMethod.class);
        // 获取方法的参数列表
        Parameter[] parameters = method.getParameters();
        // 获取方法参数值
        Object[] args = joinPoint.getArgs();
        Class<?> returnType = methodSignature.getReturnType();

        return targetProxy.invoke(rpcMethod.method(), rpcMethod.value(), parameters, args, returnType);
    }

}