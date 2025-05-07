package org.panda.tech.shiro.authority;

/**
 * 服务权限限定常量
 **/
public class PerConstants {
    /**
     * 角色前缀
     */
    public static final String ROLE_PREFIX = "ROLE.";
    /**
     * 权限前缀
     */
    public static final String PER_PREFIX = "per.";

    /**
     * 角色权限
     */
    public static final String ROLE_ADMIN = ROLE_PREFIX + "ADMIN";
    public static final String ROLE_ACCOUNT = ROLE_PREFIX + "ACCOUNT";
    public static final String ROLE_GENERAL = ROLE_PREFIX + "GENERAL";
    public static final String ROLE_CUSTOMER = ROLE_PREFIX + "CUSTOMER";

    /**
     * 账户类型
     */
    public static final String TYPE_MANAGER = PER_PREFIX + "type.manager";
    public static final String TYPE_ACCOUNT = PER_PREFIX + "type.account";
    public static final String TYPE_CUSTOMER = PER_PREFIX + "type.customer";

    /**
     * 账户级别
     */
    public static final String RANK_1 = PER_PREFIX + "rank.1";
    public static final String RANK_2 = PER_PREFIX + "rank.2";
    public static final String RANK_3 = PER_PREFIX + "rank.3";

}
