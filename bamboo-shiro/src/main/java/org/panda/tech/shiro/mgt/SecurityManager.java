package org.panda.tech.shiro.mgt;

import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.tomcat.util.http.parser.Authorization;
import org.panda.tech.core.exception.business.BusinessException;
import org.panda.tech.shiro.authority.Authority;

/**
 * 安全管理器
 *
 * @author fangen
 * @since JDK 11
 */
public interface SecurityManager extends SubjectManager {

    void login(Subject subject, AuthenticationToken token) throws BusinessException;

    Object getUser(Subject subject, boolean auto);

    Authorization getAuthorization(Subject subject, boolean reset);

    boolean isAuthorized(Subject subject, Authority authority);

    void validateAuthority(Subject subject, Authority authority) throws BusinessException;

    void logout(Subject subject) throws BusinessException;

}
