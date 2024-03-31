package org.panda.tech.core.rpc.annotation;

import org.panda.bamboo.common.constant.basic.Strings;

import java.lang.annotation.*;

/**
 * RPC客户端标注
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface RpcClient {
    /**
     * @return 服务根路径【必须】
     */
    String value();

    String beanId() default Strings.EMPTY;

}
