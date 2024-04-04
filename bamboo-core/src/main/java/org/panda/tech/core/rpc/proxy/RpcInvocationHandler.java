package org.panda.tech.core.rpc.proxy;

import org.panda.bamboo.common.tools.DelegateInvocationHandler;
import org.panda.tech.core.rpc.filter.RpcInvokeInterceptor;

import java.lang.reflect.Method;

public class RpcInvocationHandler extends DelegateInvocationHandler {

    private RpcInvokeInterceptor interceptor;

    private String beanId;

    public RpcInvocationHandler(Object target) {
        super(target);
    }

    public void setInterceptor(RpcInvokeInterceptor interceptor) {
        this.interceptor = interceptor;
    }

    public void setBeanId(String beanId) {
        this.beanId = beanId;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (interceptor != null) {
            this.interceptor.beforeInvoke(beanId, method, args);
        }
        Object result = super.invoke(proxy, method, args);
        if (interceptor != null) {
            this.interceptor.afterInvoke(beanId, method, args, result);
        }
        return result;
    }

}
