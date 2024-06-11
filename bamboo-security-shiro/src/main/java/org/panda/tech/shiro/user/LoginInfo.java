package org.panda.tech.shiro.user;

import javax.servlet.http.Cookie;

/**
 * 登录信息
 *
 * @author fangen
 * @since JDK 11
 */
public interface LoginInfo {

    Object getUser();

    Iterable<Cookie> getCookies();

}
