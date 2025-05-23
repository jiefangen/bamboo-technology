package org.panda.tech.shiro.subject;

import org.panda.tech.shiro.authentication.token.AuthenticationToken;
import org.panda.tech.shiro.authority.Authority;
import org.panda.tech.shiro.authority.Authorization;
import org.panda.tech.core.exception.business.BusinessException;
import org.panda.tech.core.exception.business.HandleableException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 类似Shiro中的Subject，用于表示一个用户的相关信息
 * <T> 用户类型
 *
 * @author fangen
 */
public interface Subject {

    HttpServletRequest getServletRequest();

    HttpServletResponse getServletResponse();

    Class<?> getUserClass();

    <T> T getUser();

    boolean isLogined();

    void login(AuthenticationToken token) throws HandleableException;

    <T extends Authorization> T getAuthorization(boolean reset);

    boolean isAuthorized(Authority authority);

    void validateAuthority(Authority authority) throws BusinessException;

    void logout() throws BusinessException;

}
