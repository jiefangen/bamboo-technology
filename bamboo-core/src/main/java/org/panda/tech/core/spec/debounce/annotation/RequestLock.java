package org.panda.tech.core.spec.debounce.annotation;

import org.panda.bamboo.common.constant.basic.Strings;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestLock {
    /**
     * @return 失效时间
     */
    long expire();

    /**
     * @return 前缀
     */
    String prefix() default Strings.EMPTY;

    /**
     * @return 时间单位
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;

    /**
     * @return 分隔符
     */
    String delimiter() default Strings.AND;

}
