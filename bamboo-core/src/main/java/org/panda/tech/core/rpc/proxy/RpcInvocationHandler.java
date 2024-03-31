package org.panda.tech.core.rpc.proxy;

import org.panda.bamboo.common.tools.DelegateInvocationHandler;
import org.panda.tech.core.rpc.client.RpcClientInvoker;
import org.panda.tech.core.web.restful.RestfulResult;

import java.lang.reflect.Method;

public class RpcInvocationHandler extends DelegateInvocationHandler {

    private final RpcClientInvoker clientProxy;

    public RpcInvocationHandler(RpcClientInvoker clientProxy, Object delegate) {
        super(delegate);
        this.clientProxy = clientProxy;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
//        clientProxy.invoke("", "", args, null);

        return RestfulResult.success();
    }

}
