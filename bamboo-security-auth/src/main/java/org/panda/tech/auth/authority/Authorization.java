package org.panda.tech.auth.authority;

import java.io.Serializable;
import java.util.Collection;

/**
 * 授权集=角色集+权限集
 *
 * @author fangen
 */
public interface Authorization extends Serializable {

    /**
     * @return 用户具有的角色集合
     */
    Collection<String> getRoles();

    /**
     * @return 用户具有的权限集合
     */
    Collection<String> getPermissions();

}
