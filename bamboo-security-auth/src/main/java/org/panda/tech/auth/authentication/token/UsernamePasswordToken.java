package org.panda.tech.auth.authentication.token;

import org.panda.tech.core.spec.user.UsernamePassword;

/**
 * 用户名+密码形式的登录Token，类似Shiro中的UsernamePasswordToken
 *
 * @author fangen
 */
public class UsernamePasswordToken extends UsernamePassword implements RememberMeAuthenticationToken, HostAuthenticationToken {

    private boolean rememberMe;

    private String host;

    public UsernamePasswordToken() {
    }

    public UsernamePasswordToken(String username, String password) {
        super(username, password);
    }

    public void setHost(String host) {
        this.host = host;
    }

    @Override
    public String getHost() {
        return this.host;
    }

    public void setRememberMe(boolean rememberMe) {
        this.rememberMe = rememberMe;
    }

    @Override
    public boolean isRememberMe() {
        return this.rememberMe;
    }

    @Override
    public Object getPrincipal() {
        return getUsername();
    }

    @Override
    public Object getCredentials() {
        return getPassword();
    }

}
