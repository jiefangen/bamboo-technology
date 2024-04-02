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
import java.lang.reflect.Parameter;

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
        Class<?> targetClass = joinPoint.getTarget().getClass();
        // RPC客户端解析
        RpcClient rpcClient = targetClass.getAnnotation(RpcClient.class);
        RpcClientInvoker rpcClientInvoker = new RpcClientInvoker(rpcClient.value(), rpcClient.internal());
        rpcClientInvoker.setSerializer(rpcSerializer);
        String beanId = rpcClient.beanId();
        if (StringUtils.isEmpty(beanId)) {
            // 后续用作RPC调用器代理缓存
            beanId = StringUtil.firstToLowerCase(targetClass.getSimpleName());
        }
        rpcClientInvoker.setBeanId(beanId);

        // RPC方法解析
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        RpcMethod rpcMethod = method.getAnnotation(RpcMethod.class);

        // 获取方法的参数列表
        Parameter[] parameters = method.getParameters();
        // 获取方法参数值
        Object[] args = joinPoint.getArgs();
        Class<?> returnType = methodSignature.getReturnType();
        return rpcClientInvoker.invoke(rpcMethod.method(), rpcMethod.value(), parameters, args, returnType);
    }

}
