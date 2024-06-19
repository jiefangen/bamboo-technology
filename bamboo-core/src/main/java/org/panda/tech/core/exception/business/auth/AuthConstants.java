package org.panda.tech.core.exception.business.auth;

/**
 * 系统认证鉴权规范常量
 */
public class AuthConstants {
    /**
     * 用户账户不存在
     */
    public static final int USER_NOT_EXIST_CODE = 5001;
    public static final String USERNAME_NOT_EXIST = "Username does not exist.";
    /**
     * 密码验证错误
     */
    public static final int PWD_WRONG_CODE = 5002;
    public static final String PWD_WRONG = "Password verification error.";
    /**
     * 账户已被禁用
     */
    public static final int USER_DISABLED_CODE = 5003;
    public static final String USER_DISABLED = "This account has been disabled.";
    /**
     * 账户已被锁定
     */
    public static final int USER_LOCKED_CODE = 5004;
    public static final String USER_LOCKED = "This account has been locked.";
    /**
     * 账户登出
     */
    public static final int LOGGED_OUT = 5006;
    public static final String LOGGED_OUT_REASON = "Maybe the server restarted or some other reasons.";
    /**
     * 其它客户端登入
     */
    public static final Integer OTHER_CLIENTS_LOGGED_IN = 5012;
}
