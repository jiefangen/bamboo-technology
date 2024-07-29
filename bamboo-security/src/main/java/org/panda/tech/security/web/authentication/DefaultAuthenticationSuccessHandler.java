package org.panda.tech.security.web.authentication;

import org.panda.bamboo.common.util.jackson.JsonUtil;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 支持请求的授权成功处理器
 */
public abstract class DefaultAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Override
    public final void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException {
        Object result = getDefaultLoginResult(request, authentication);
        if (result != null) {
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            response.getWriter().print(JsonUtil.toJson(result));
        }
        // 清除当前线程的SecurityContext，使用一次性授权token交互认证
        SecurityContextHolder.clearContext();
    }

    protected abstract Object getDefaultLoginResult(HttpServletRequest request, Authentication authentication);

}
