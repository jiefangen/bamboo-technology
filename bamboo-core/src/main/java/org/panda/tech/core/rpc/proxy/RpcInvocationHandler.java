package org.panda.tech.core.rpc.proxy;

import org.panda.bamboo.common.tools.DelegateInvocationHandler;

import java.lang.reflect.Method;

public class RpcInvocationHandler extends DelegateInvocationHandler {

    public RpcInvocationHandler(Object target) {
        super(target);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("Before method invocation");
        Object result = super.invoke(proxy, method, args);
        System.out.println("After method invocation");
        return result;
    }

}
