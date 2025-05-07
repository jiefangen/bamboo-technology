package org.panda.tech.shiro.realm;

import javax.servlet.http.Cookie;

/**
 * 支持“记住我”功能的校验领域
 *
 * @author fangen
 */
public interface RememberMeRealm<T> extends Realm<T> {

    /**
     * 根据远程访问地址和Cookie获取登录用户
     * 
     * @param host
     *            远程访问地址
     * @param cookies
     *            当前所有cookie
     * @return 登录用户，如果登录验证不通过则返回null
     */
    default T getLoginUser(String host, Cookie[] cookies) {
        return null;
    }

    /**
     * 根据token获取登录用户
     *
     * @param token
     *            交互凭证
     * @return 登录用户，如果登录验证不通过则返回null
     */
    T getLoginUser(String token);

}
