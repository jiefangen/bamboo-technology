package org.panda.tech.shiro.authentication;

import java.util.Collection;

/**
 * 登出信息
 *
 * @author fangen
 */
public interface LogoutInfo {

    /**
     *
     * @return 是否登出时调用HttpSession.invalidate()方法
     */
    boolean isInvalidatingSession();

    /**
     *
     * @return 需要在登出之后移除的cookie名称集合
     */
    Collection<String> getCookieNames();

}
