package org.panda.tech.shiro.mgt;

import org.panda.tech.shiro.authentication.token.AuthenticationToken;
import org.panda.tech.shiro.authority.Authority;
import org.panda.tech.shiro.authority.Authorization;
import org.panda.tech.shiro.subject.Subject;
import org.panda.tech.core.exception.business.BusinessException;
import org.panda.tech.core.exception.business.HandleableException;

/**
 * 安全管理器
 *
 * @author fangen
 */
public interface SecurityManager extends SubjectManager {

    void login(Subject subject, AuthenticationToken token) throws HandleableException;

    Object getUser(Subject subject, boolean auto);

    Authorization getAuthorization(Subject subject, boolean reset);

    boolean isAuthorized(Subject subject, Authority authority);

    void validateAuthority(Subject subject, Authority authority) throws BusinessException;

    void logout(Subject subject) throws BusinessException;

}
