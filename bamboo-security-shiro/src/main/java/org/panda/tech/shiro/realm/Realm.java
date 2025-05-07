package org.panda.tech.shiro.realm;

import org.panda.tech.shiro.authentication.LoginInfo;
import org.panda.tech.shiro.authentication.LogoutInfo;
import org.panda.tech.shiro.authentication.token.AuthenticationToken;
import org.panda.tech.shiro.authority.AuthorizationInfo;
import org.panda.tech.core.exception.business.BusinessException;
import org.panda.tech.core.exception.business.HandleableException;

/**
 * 校验领域，用于完成登录校验、权限校验等逻辑处理，类似Shiro中的Realm
 *
 * @author fangen
 * @param <T> 用户类型
 */
public interface Realm<T> {

    /**
     * 部分系统可能支持多种类型的用户同时登录，故需要通过该方法进行界定
     *
     * @return 用户类型
     */
    Class<T> getUserClass(); // 默认实现下无法取得实现类的泛型类型

    /**
     *
     * @return 用户信息保存到会话中的属性名称
     */
    default String getUserSessionName() {
        return "_SESSION_" + getUserClass().getSimpleName().toUpperCase();
    }

    /**
     * 根据登录token获取登录用户信息，即完成登录校验
     *
     * @param loginToken
     *            登录token
     * @return 用户信息，返回null将被忽略，不应返回null
     * @throws HandleableException
     *             如果登录校验不通过
     */
    LoginInfo getLoginInfo(AuthenticationToken loginToken) throws BusinessException;

    /**
     * 获取指定用户的授权信息
     *
     * @param user 用户
     * @return 授权信息
     */
    AuthorizationInfo getAuthorizationInfo(T user);

    /**
     * 校验指定用户能否登出，并获取登出时要清除的信息
     *
     * @param user 用户
     * @return 登出信息
     * @throws BusinessException 如果不允许登出
     */
    LogoutInfo getLogoutInfo(T user) throws BusinessException;

    /**
     * 在指定用户登出后调用，用于进行登出后的处理
     *
     * @param user 登出用户
     */
    default void onLogouted(final T user) {
    }

}
