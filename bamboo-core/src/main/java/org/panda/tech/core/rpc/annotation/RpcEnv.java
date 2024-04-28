package org.panda.tech.core.rpc.annotation;

import java.lang.annotation.*;

/**
 * RPC环境参量
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RpcEnv {
    /**
     * @return 服务根路径
     */
    String value();
    /**
     * @return 绑定的环境变量
     */
    String active();
}
