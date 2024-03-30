package org.panda.tech.core.rpc.aop;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.panda.bamboo.common.util.lang.StringUtil;
import org.panda.tech.core.rpc.annotation.RpcClient;
import org.panda.tech.core.rpc.annotation.RpcMethod;
import org.panda.tech.core.rpc.client.RpcClientInvoker;
import org.panda.tech.core.rpc.serializer.RpcSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * RPC客户端切面
 *
 * @author fangen
 **/
@Component
@Aspect
public class RpcClientAspect {

    @Autowired
    private RpcSerializer rpcSerializer;

    @Pointcut("@within(rpcClient) || @annotation(rpcClient)")
    public void rpcClientPointCut(RpcClient rpcClient) {
    }

    @Around("rpcClientPointCut(org.panda.tech.core.rpc.annotation.RpcClient)")
    public Object aroundRpcClientMethods(ProceedingJoinPoint joinPoint) throws Throwable {
        Object target = joinPoint.getTarget();
        Class<?> targetClass = target.getClass();
        // RPC客户端解析
        RpcClient rpcClient = targetClass.getAnnotation(RpcClient.class);
        RpcClientInvoker rpcClientInvoker = new RpcClientInvoker(rpcClient.serverRoot());
        rpcClientInvoker.setSerializer(rpcSerializer);
        // RPC方法解析
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        RpcMethod rpcMethod = method.getAnnotation(RpcMethod.class);
        Object[] args = joinPoint.getArgs();
        Class<?> returnType = methodSignature.getReturnType();

        String beanId = rpcClient.beanId();
        if (StringUtils.isEmpty(beanId)) {
            beanId = StringUtil.firstToLowerCase(targetClass.getSimpleName());
        }
        return rpcClientInvoker.invoke(beanId, rpcMethod.value(), args, returnType);
    }

}
