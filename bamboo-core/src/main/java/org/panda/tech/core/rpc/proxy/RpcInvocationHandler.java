package org.panda.tech.core.rpc.proxy;

import org.panda.bamboo.common.tools.DelegateInvocationHandler;
import org.panda.tech.core.rpc.filter.RpcInvokeInterceptor;

import java.lang.reflect.Method;

/**
 * RpcInvokeInterceptor初始化顺序会影响RpcClientProcessor初始化bean过滤
 */
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
        boolean isIntercept = interceptor != null && "invoke".equals(method.getName());
        if (isIntercept) {
            this.interceptor.beforeInvoke(proxy, beanId, method, args);
        }
        Object result = super.invoke(proxy, method, args);
        if (isIntercept) {
            this.interceptor.afterInvoke(beanId, method, args, result);
        }
        return result;
    }

}
