package org.panda.tech.shiro.authority;

import org.apache.commons.lang3.StringUtils;
import org.panda.bamboo.common.constant.basic.Strings;
import org.panda.bamboo.common.util.lang.CollectionUtil;

import java.util.*;

/**
 * 授权=角色+权限
 *
 * @author fangen
 */
public class Authority {

    private String role;
    private String permission;
    /**
     * 所属角色清单
     */
    private Set<String> belongs = new HashSet<>();
    /**
     * 登录即可访问的权限
     */
    public static final Authority LOGINED = new Authority(":LOGINED", null);

    public Authority(String role, String permission) {
        this.role = role;
        this.permission = permission;
    }

    public String getRole() {
        return this.role;
    }

    public String getPermission() {
        return this.permission;
    }

    public Set<String> getBelongs() {
        return this.belongs;
    }

    public boolean isNotEmpty() {
        return StringUtils.isNotEmpty(this.role) || StringUtils.isNotEmpty(this.permission);
    }

    /**
     * 判断当前授权是否被包含在指定的授权集中
     *
     * @param authorization 授权集
     *
     * @return 当前授权是否被包含在指定的授权集中
     */
    public boolean isContained(Authorization authorization) {
        if (authorization == null) {
            return false;
        }
        if (this == LOGINED) { // 当前授权如果为登录即可访问，authorization不为null，则为已登录，此时返回true
            return true;
        }

        boolean roleContained = true;
        Collection<String> roles = authorization.getRoles();
        if (roles == null) {
            roles = Collections.emptyList();
        }
        if (StringUtils.isNotEmpty(this.role)) { // 当前授权包含角色限定才校验角色
            // 被校验角色中包含*通配符角色，或者包含当前授权的角色，则视为角色匹配
            roleContained = CollectionUtil.contains(roles, Strings.ASTERISK)
                    || CollectionUtil.contains(roles, this.role);
        }
        if (!roleContained) { // 角色不包含，则视为不匹配
            return false;
        }

        boolean permissionContained = true;
        if (StringUtils.isNotEmpty(this.permission)) { // 当前授权包含权限限定才校验权限
            Collection<String> permissions = authorization.getPermissions();
            if (permissions == null) {
                permissions = Collections.emptyList();
            }
            // 被校验权限中包含*通配符权限，或者包含当前授权的权限，则视为权限匹配
            permissionContained = CollectionUtil.contains(permissions, Strings.ASTERISK)
                    || CollectionUtil.contains(permissions, this.permission);
            // 如果权限校验未通过，则检查被校验角色中是否包含所属角色清单中的一个，只要一个匹配则视为匹配
            if (!permissionContained && this.belongs.size() > 0) {
                permissionContained = CollectionUtil.containsOneOf(roles, this.belongs);
            }
        }
        return permissionContained;
    }

    @Override
    public String toString() {
        return "role=" + this.role + ", permission=" + this.permission + ", belongs="
                + StringUtils.join(this.belongs, Strings.COMMA);
    }

}
