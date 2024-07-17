package org.panda.tech.auth.authentication.token;

/**
 * 带有"记住我"标志的登录Token，类似Shiro中的RememberMeAuthenticationToken
 *
 * @author fangen
 */
public interface RememberMeAuthenticationToken extends AuthenticationToken {

    boolean isRememberMe();

}
