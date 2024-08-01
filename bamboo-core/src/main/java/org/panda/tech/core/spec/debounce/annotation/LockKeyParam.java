package org.panda.tech.core.spec.debounce.annotation;

import java.lang.annotation.*;

/**
 * 控制锁唯一key设置
 */
@Documented
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface LockKeyParam {
}
