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
    String value() default Strings.EMPTY;

    HttpMethod method() default HttpMethod.POST;

    /**
     * @return 多级嵌套泛型实际类型
     */
    Class<?>[] subTypes() default {};
}
