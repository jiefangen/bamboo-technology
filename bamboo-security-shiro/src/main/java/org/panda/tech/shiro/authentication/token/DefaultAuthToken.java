package org.panda.tech.shiro.authentication.token;

/**
 * 默认形式的登录Token
 *
 * @author fangen
 */
public class DefaultAuthToken implements RememberMeAuthenticationToken, HostAuthenticationToken {

    private Object principal;

    private Object credentials;

    private boolean rememberMe;

    private String host;

    public DefaultAuthToken() {
    }

    @Override
    public boolean isRememberMe() {
        return this.rememberMe;
    }

    public void setRememberMe(final boolean rememberMe) {
        this.rememberMe = rememberMe;
    }

    public void setHost(final String host) {
        this.host = host;
    }

    @Override
    public String getHost() {
        return this.host;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }

    @Override
    public Object getCredentials() {
        return credentials;
    }

    public void setPrincipal(Object principal) {
        this.principal = principal;
    }

    public void setCredentials(Object credentials) {
        this.credentials = credentials;
    }

}
