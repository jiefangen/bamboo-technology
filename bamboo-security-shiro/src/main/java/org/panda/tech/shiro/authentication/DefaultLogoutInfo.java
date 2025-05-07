package org.panda.tech.shiro.authentication;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * 默认的登出信息实现
 *
 * @author fangen
 */
public class DefaultLogoutInfo implements LogoutInfo {

    private boolean invalidatingSession;
    private Set<String> cookieNames = new HashSet<>();

    public DefaultLogoutInfo(final boolean invalidatingSession) {
        this.invalidatingSession = invalidatingSession;
    }

    @Override
    public boolean isInvalidatingSession() {
        return this.invalidatingSession;
    }

    @Override
    public Collection<String> getCookieNames() {
        return this.cookieNames;
    }

    public void addCookieName(final String name) {
        this.cookieNames.add(name);
    }

}
