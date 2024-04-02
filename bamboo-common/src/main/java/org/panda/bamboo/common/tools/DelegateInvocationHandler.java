package org.panda.bamboo.common.tools;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class DelegateInvocationHandler implements InvocationHandler {

    private final Object delegate;

    public DelegateInvocationHandler(Object delegate) {
        this.delegate = delegate;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return method.invoke(this.delegate, args);
    }

}
