package org.panda.tech.core.spec.log.trace;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TraceIdInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String traceId = request.getHeader(TraceContext.TRACE_ID_KEY);
        if (StringUtils.isEmpty(traceId)) {
            TraceContext.ensureTraceId();
        } else {
            if (StringUtils.isEmpty(TraceContext.getTraceId())) {
                TraceContext.setTraceId(traceId);
            }
        }
        response.setHeader(TraceContext.TRACE_ID_KEY, traceId);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        TraceContext.clear();
    }
}