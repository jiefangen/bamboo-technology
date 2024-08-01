package org.panda.tech.core.spec.debounce.support;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.panda.tech.core.exception.ExceptionEnum;
import org.panda.tech.core.exception.business.BusinessException;
import org.panda.tech.core.spec.debounce.annotation.RequestLock;
import org.panda.tech.core.spec.debounce.generator.LockKeyGenerator;
import org.panda.tech.core.web.restful.RestfulResult;
import org.springframework.core.annotation.Order;

import java.lang.reflect.Method;

/**
 * 缓存请求锁切面支持
 */
@Order(10)
public abstract class RequestLockSupport {

    protected Object doAround(ProceedingJoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature)joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        RequestLock requestLock = method.getAnnotation(RequestLock.class);
        if (StringUtils.isEmpty(requestLock.prefix())) {
            return RestfulResult.failure("RequestLock prefix cannot be empty");
        }
        // 获取自定义请求锁key
        String lockKey = LockKeyGenerator.getLockKey(joinPoint);
        if (Boolean.FALSE.equals(isLocked(lockKey, requestLock))) {
            return RestfulResult.getFailure(ExceptionEnum.INTERCEPT_LOCK);
        }
        try {
            return joinPoint.proceed();
        } catch (Throwable throwable) {
            throw new BusinessException(ExceptionEnum.SYSTEM);
        }
    }

    /**
     * 请求锁判断
     *
     * @param lockKey 锁键
     * @param requestLock 请求锁注解
     * @return 判断结果
     */
    protected abstract Boolean isLocked(String lockKey, RequestLock requestLock);

}
