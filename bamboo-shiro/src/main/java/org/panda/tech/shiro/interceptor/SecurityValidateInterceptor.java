package org.panda.tech.shiro.interceptor;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.panda.bamboo.common.util.LogUtil;
import org.panda.tech.core.exception.ExceptionEnum;
import org.panda.tech.core.exception.business.BusinessException;
import org.panda.tech.core.rpc.constant.RpcConstants;
import org.panda.tech.core.util.UrlPatternMatchSupport;
import org.panda.tech.core.web.restful.RestfulResult;
import org.panda.tech.core.web.util.NetUtil;
import org.panda.tech.core.web.util.WebHttpUtil;
import org.panda.tech.shiro.annotation.Accessibility;
import org.springframework.http.HttpStatus;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 安全校验拦截器
 *
 * @author fangen
 * @since JDK 11
 */
public class SecurityValidateInterceptor extends UrlPatternMatchSupport implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 请求为include请求则忽略当前拦截器
        if (WebUtils.isIncludeRequest(request)) {
            return true;
        }
        String url = WebHttpUtil.getRelativeRequestAction(request); // 取不含扩展名的URL
        if (matches(url)) { // URL匹配才进行校验
            // 校验Accessibility注解限制
            if (handler instanceof HandlerMethod) {
                Accessibility accessibility = ((HandlerMethod) handler).getMethodAnnotation(Accessibility.class);
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
            Subject subject = SecurityUtils.getSubject();
            if (subject != null) { // 能取得subject才进行校验
                // 登录校验
                if (!validateLogin(subject, response)) {
                    return false;
                }
                // 授权校验
                if (!validateAuthority(subject, request)) {
                    return false;
                }
            }
        }
        return true;
    }

    protected boolean validateLogin(Subject subject, HttpServletResponse response) throws IOException {
        if (!subject.isAuthenticated()) {
            // 未登录且不允许匿名访问
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            Object obj = RestfulResult.getFailure(ExceptionEnum.UNAUTHORIZED);
            WebHttpUtil.buildJsonResponse(response, obj);
            return false;
        }
        return true;
    }

    protected boolean validateAuthority(Subject subject, HttpServletRequest request)
            throws BusinessException {
        return true;
    }

    @Override
    protected boolean matches(String url) {
        // 始终排除RPC访问
        return !url.startsWith(RpcConstants.URL_RPC_PREFIX) && super.matches(url);
    }

}
