package org.panda.tech.shiro.annotation;

import org.panda.bamboo.common.constant.basic.Strings;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 可访问性
 *
 * @author fangen
 */
@Documented
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Accessibility {
    /**
     * @return 是否匿名可访问
     */
    boolean anonymous() default false;

    /**
     * @return 是否只有局域网可访问
     */
    boolean lan() default false;

    /**
     * @return 所需角色
     */
    String role() default Strings.EMPTY;

    /**
     * @return 所需许可
     */
    String permission() default Strings.EMPTY;
}
