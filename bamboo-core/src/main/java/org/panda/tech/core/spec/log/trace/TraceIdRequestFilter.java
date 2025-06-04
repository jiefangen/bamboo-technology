package org.panda.tech.core.spec.log.trace;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Order(1)
public class TraceIdRequestFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String traceId = request.getHeader(TraceContext.TRACE_ID_KEY);
        if (StringUtils.isEmpty(traceId)) {
            TraceContext.ensureTraceId();
        } else {
            TraceContext.setTraceId(traceId);
        }
        response.setHeader(TraceContext.TRACE_ID_KEY, traceId);
        try {
            filterChain.doFilter(request, response);
        } finally {
            TraceContext.clear(); // 避免线程复用问题
        }
    }
}
