package org.panda.tech.core.rpc.annotation;

import org.panda.tech.core.rpc.filter.RpcInvokerFilter;
import org.panda.tech.core.rpc.proxy.RpcClientProcessor;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 开启分布式RPC组件调用
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({RpcClientProcessor.class, RpcInvokerFilter.class})
public @interface EnableRpcInvoker {
}
