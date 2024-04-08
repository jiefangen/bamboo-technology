package org.panda.tech.core.rpc.annotation;

import org.panda.bamboo.common.constant.basic.Strings;
import org.springframework.http.HttpMethod;

import java.lang.annotation.*;

/**
 * RPC方法标注
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RpcMethod {
    /**
     * @return 资源路径【必须】
     */
    String value() default Strings.EMPTY;

    /**
     * @return 请求方法
     */
    HttpMethod method() default HttpMethod.POST;

}
