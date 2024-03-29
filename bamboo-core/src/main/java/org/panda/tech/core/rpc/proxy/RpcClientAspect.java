package org.panda.tech.core.rpc.proxy;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.panda.tech.core.rpc.annotation.RpcClient;
import org.panda.tech.core.rpc.annotation.RpcMethod;
import org.panda.tech.core.rpc.client.RpcClientInvoker;
import org.panda.tech.core.rpc.serializer.RpcSerializer;
import org.panda.tech.core.web.restful.RestfulResult;
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
        Class<?> targetClass = joinPoint.getTarget().getClass();
        RpcClient rpcClient = targetClass.getAnnotation(RpcClient.class);
        String serverRoot = rpcClient.serverRoot();
        RpcClientInvoker rpcClientInvoker = new RpcClientInvoker(serverRoot);
        rpcClientInvoker.setSerializer(rpcSerializer);

        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        RpcMethod rpcMethod = method.getAnnotation(RpcMethod.class);


//        Object targetProxy = BeanUtil.createProxy(target, new RpcInvocationHandler(rpcClientInvoker, target));

        return RestfulResult.success("AOP");
    }

}
