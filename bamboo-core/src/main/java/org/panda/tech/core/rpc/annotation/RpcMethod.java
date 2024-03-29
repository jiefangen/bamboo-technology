package org.panda.tech.core.rpc.annotation;

import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.annotation.*;

/**
 * RPC方法标注
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RpcMethod {

    String value();

    RequestMethod reqMethod() default RequestMethod.POST;

}
