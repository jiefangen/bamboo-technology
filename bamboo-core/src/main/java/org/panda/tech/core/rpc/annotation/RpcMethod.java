package org.panda.tech.core.rpc.annotation;

import org.panda.bamboo.common.constant.basic.Strings;
import org.panda.tech.core.web.restful.RestfulResult;
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
     * 标准返回{@link RestfulResult}中<T>类型
     *
     * @return 泛型实际类型
     */
    Class<?>[] subType() default {};
}
