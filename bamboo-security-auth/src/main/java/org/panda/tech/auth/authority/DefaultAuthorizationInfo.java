package org.panda.tech.auth.authority;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * 默认的授权信息实现
 *
 * @author fangen
 */
public class DefaultAuthorizationInfo implements AuthorizationInfo {

    private Set<String> roles = new HashSet<>();

    private Set<String> permissions = new HashSet<>();

    private boolean caching = true;

    public DefaultAuthorizationInfo(final boolean caching) {
        this.caching = caching;
    }

    @Override
    public Collection<String> getRoles() {
        return this.roles;
    }

    @Override
    public Collection<String> getPermissions() {
        return this.permissions;
    }

    @Override
    public boolean isCaching() {
        return this.caching;
    }

    public void addRole(final String role) {
        this.roles.add(role);
    }

    public void addPermission(final String permission) {
        this.permissions.add(permission);
    }

}