package org.panda.tech.shiro.authentication.token;

/**
 * 带有请求来源地址的登录Token，类似Shiro中的HostAuthenticationToken
 *
 * @author fangen
 */
public interface HostAuthenticationToken extends AuthenticationToken {

    /**
     *
     * @return 访问者远程地址
     */
    String getHost();

}
