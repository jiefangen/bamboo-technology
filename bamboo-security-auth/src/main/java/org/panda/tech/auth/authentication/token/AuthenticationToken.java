package org.panda.tech.auth.authentication.token;

/**
 * 用户Token，类似Shiro中的AuthenticationToken
 *
 * @author fangen
 */
public interface AuthenticationToken {

    /**
     *
     * @return 能唯一表示一个用户的标识，一般为用户名
     */
    Object getPrincipal();

    /**
     * 
     * @return 登录凭证，一般为密码
     */
    Object getCredentials();

}
