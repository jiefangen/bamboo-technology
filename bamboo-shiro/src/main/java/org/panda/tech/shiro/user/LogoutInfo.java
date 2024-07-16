package org.panda.tech.shiro.user;

/**
 * 登出信息
 *
 * @author fangen
 * @since JDK 11
 */
public interface LogoutInfo {

    /**
     * @return 是否登出时调用HttpSession.invalidate()方法
     */
    boolean isInvalidatingSession();

    /**
     * @return 需要在登出之后移除的cookie名称集合
     */
    Iterable<String> getCookieNames();

}
