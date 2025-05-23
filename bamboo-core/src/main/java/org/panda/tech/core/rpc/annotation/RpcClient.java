package org.panda.tech.core.rpc.annotation;

import org.panda.bamboo.common.constant.basic.Strings;
import org.panda.tech.core.rpc.constant.enums.CommMode;

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
     * @return 服务根路径-绑定环境变量
     */
    RpcEnv[] values() default {};

    /**
     * @return 通信模式
     */
    CommMode mode() default CommMode.REST_TEMPLATE;

    /**
     * @return Bean Id
     */
    String beanId() default Strings.EMPTY;

    /**
     * @return 服务名称
     */
    String serviceName() default Strings.EMPTY;

    /**
     * @return 是否是内部调用
     */
    boolean internal() default true;
}
