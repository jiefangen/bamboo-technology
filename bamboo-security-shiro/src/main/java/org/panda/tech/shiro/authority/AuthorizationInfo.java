package org.panda.tech.shiro.authority;

/**
 * 授权信息
 *
 * @author fangen
 */
public interface AuthorizationInfo extends Authorization {

    /**
     * @return 是否需要缓存当前授权信息对象
     */
    boolean isCaching();

}
