package org.panda.tech.core.rpc.annotation;

import java.lang.annotation.*;

/**
 * RPC环境参量
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RpcEnv {
    String value();

    String active();
}
