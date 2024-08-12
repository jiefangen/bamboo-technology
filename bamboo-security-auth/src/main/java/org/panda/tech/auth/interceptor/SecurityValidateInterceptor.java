package org.panda.tech.auth.interceptor;

import org.apache.commons.lang3.StringUtils;
import org.panda.bamboo.common.util.LogUtil;
import org.panda.bamboo.common.util.lang.StringUtil;
import org.panda.tech.auth.annotation.Accessibility;
import org.panda.tech.auth.authority.Authority;
import org.panda.tech.auth.mgt.SubjectManager;
import org.panda.tech.auth.mgt.exception.NoAuthorityException;
import org.panda.tech.auth.subject.Subject;
import org.panda.tech.core.exception.ExceptionEnum;
import org.panda.tech.core.exception.business.BusinessException;
import org.panda.tech.core.util.UrlPatternMatchSupport;
import org.panda.tech.core.web.config.security.WebSecurityProperties;
import org.panda.tech.core.web.restful.RestfulResult;
import org.panda.tech.core.web.util.NetUtil;
import org.panda.tech.core.web.util.WebHttpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.WebUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;

/**
 * 安全校验拦截器
 *
 * @author fangen
 */
@Component
public class SecurityValidateInterceptor extends UrlPatternMatchSupport implements HandlerInterceptor {
    /**
     * 请求转发的前缀
     */
    public static String FORWARD_PREFIX = "forward:";

    private WebSecurityProperties securityProperties;

    private SubjectManager subjectManager;

    private Class<?> userClass;

    private String loginUrl;

    @Autowired(required = false)
    public void setWebSecurityProperties(WebSecurityProperties securityProperties) {
        this.securityProperties = securityProperties;
    }

    @Autowired
    public void setSubjectManager(SubjectManager subjectManager) {
        this.subjectManager = subjectManager;
    }

    /**
     * @param userClass 要拦截的用户类型，如果整个系统只有一种用户类型，则可以不设置
     */
    public void setUserClass(Class<?> userClass) {
        this.userClass = userClass;
    }

    /**
     * @param loginUrl 未登录时试图访问需登录才能访问的资源时，跳转至的登录页面URL，可通过{0}附带上原访问链接
     */
    public void setLoginUrl(String loginUrl) {
        this.loginUrl = loginUrl;
    }

    private Class<?> getUserClass(HttpServletRequest request, HttpServletResponse response) {
        if (this.userClass == null) {
            Subject subject = this.subjectManager.getSubject(request, response);
            if (subject != null) {
                this.userClass = subject.getUserClass();
            }
        }
        return this.userClass;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 请求为include请求则忽略当前拦截器
        if (WebUtils.isIncludeRequest(request)) {
            return true;
        }
        String url = WebHttpUtil.getRelativeRequestAction(request); // 取不含扩展名的URL
        if (matches(url)) { // URL匹配才进行校验
            Accessibility accessibility = null;
            // 校验Accessibility注解限制
            if (handler instanceof HandlerMethod) {
                accessibility = ((HandlerMethod) handler).getMethodAnnotation(Accessibility.class);
                // 局域网访问限制校验
                if (accessibility != null) {
                    if (accessibility.lan()) {
                        String ip = WebHttpUtil.getRemoteAddress(request);
                        if (!NetUtil.isIntranetIp(ip)) {
                            LogUtil.warn(getClass(), "Forbidden request {} from {}", url, ip);
                            response.sendError(HttpStatus.FORBIDDEN.value()); // 禁止非局域网访问
                            return false;
                        }
                    }
                    // 在访问性注解中设置了可匿名访问，则验证通过
                    if (accessibility.anonymous()) {
                        return true;
                    }
                }
            }
            // 校验资源权限
            Class<?> userClass = getUserClass(request, response);
            Subject subject = this.subjectManager.getSubject(request, response, userClass);
            if (subject != null) { // 能取得subject才进行校验
                // 忽略资源，则跳过不作限制
                if (this.isAnonymous(url)) {
                    return true;
                }
                // 登录校验
                if (!validateLogin(subject, request, response)) {
                    return false;
                }
                // 授权校验
                if (!validateAuthority(accessibility, subject, response)) {
                    return false;
                }
            }
        }
        return true;
    }

    protected boolean isAnonymous(String url) {
        List<String> ignoringPatterns = this.securityProperties.getIgnoringPatterns();
        if (!ignoringPatterns.isEmpty()) {
            for (String ignoringPattern : ignoringPatterns) {
                if (StringUtil.antPathMatchOneOf(url, ignoringPattern)) {
                    return true;
                }
            }
        }
        return false;
    }

    protected boolean validateLogin(Subject subject, HttpServletRequest request,
                                    HttpServletResponse response) throws ServletException, IOException {
        if (!subject.isLogined()) {
            // 未登录且不允许匿名访问
            if (StringUtils.isBlank(this.loginUrl)) { // 未登录或未指定登录页面地址时，返回错误状态
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                Object obj = RestfulResult.getFailure(ExceptionEnum.UNAUTHORIZED);
                WebHttpUtil.buildJsonResponse(response, obj);
            } else { // 普通请求未登录且已指定登录页面地址时，跳转至登录页面
                String originalUrl = WebHttpUtil.getRelativeRequestUrlWithQueryString(request, true);
                String loginUrl = MessageFormat.format(this.loginUrl, originalUrl);
                if (loginUrl.startsWith(FORWARD_PREFIX)) { // 请求转发
                    loginUrl = loginUrl.substring(FORWARD_PREFIX.length());
                    WebHttpUtil.forward(request, response, loginUrl);
                } else { // 直接重定向
                    WebHttpUtil.redirect(request, response, loginUrl);
                }
            }
            return false;
        }
        return true;
    }

    protected boolean validateAuthority(Accessibility accessibility, Subject subject, HttpServletResponse response)
            throws BusinessException, IOException {
        if (accessibility != null) {
            Authority authority;
            String role = accessibility.role();
            String permission = accessibility.permission();
            if (StringUtils.isEmpty(permission) && StringUtils.isEmpty(role)) { // 权限配置为空登录即可访问
                authority = Authority.LOGINED;
            } else {
                authority = new Authority(role, permission);
            }
            // 权限资源授权验证
            try {
                // 此时授权可能为null，为null时将被视为无访问权限
                subject.validateAuthority(authority);
            } catch (BusinessException e) {
                LogUtil.warn(getClass(), e.toString());
                Object failureResult = RestfulResult.failure(e.getMessage());
                if (e instanceof NoAuthorityException) {
                    failureResult = RestfulResult.getFailure(ExceptionEnum.AUTH_NO_OPERA);
                }
                WebHttpUtil.buildJsonResponse(response, failureResult);
                return false;
            }
        }
        return true;
    }

    @Override
    protected boolean matches(String url) {
        // 始终排除RPC访问
        return !url.startsWith("/rpc/") && super.matches(url);
    }

}
