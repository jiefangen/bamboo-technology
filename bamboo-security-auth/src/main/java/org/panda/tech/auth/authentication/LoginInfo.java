package org.panda.tech.auth.authentication;

import javax.servlet.http.Cookie;
import java.util.Collection;

/**
 * 登录用户信息
 *
 * @author fangen
 */
public interface LoginInfo {

    Object getUser();

    Collection<Cookie> getCookies();

}
