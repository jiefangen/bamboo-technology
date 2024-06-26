package org.panda.tech.core.web.mvc.servlet.filter;

import org.apache.commons.lang3.ArrayUtils;
import org.panda.bamboo.common.util.LogUtil;
import org.panda.bamboo.common.util.jackson.JsonUtil;
import org.panda.bamboo.common.util.lang.StringUtil;
import org.panda.tech.core.web.mvc.servlet.http.BodyRepeatableRequestWrapper;
import org.panda.tech.core.web.util.WebHttpUtil;
import org.slf4j.Logger;
import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 请求日志打印过滤器
 */
@Order(0)
public class RequestLogFilter implements Filter {

    private final Logger logger = LogUtil.getLogger(getClass());

    protected String[] urlPatterns;

    public RequestLogFilter(String... urlPatterns) {
        this.urlPatterns = urlPatterns;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
            throws IOException, ServletException {
        if (ArrayUtils.isNotEmpty(this.urlPatterns)) {
            HttpServletRequest request = (HttpServletRequest) req;
            // 提前包装request请求，即使不符合打印规则后续操作中也可重复读取
            request = new BodyRepeatableRequestWrapper(request);
            String url = WebHttpUtil.getRelativeRequestUrl(request);
            if (StringUtil.antPathMatchOneOf(url, this.urlPatterns)) {
                this.logger.info("====== request from {} ======", WebHttpUtil.getRemoteAddress(request));
                this.logger.info("{} {}", request.getMethod(), url);
                this.logger.info("headers: {}", JsonUtil.toJson(WebHttpUtil.getHeaders(request)));
                this.logger.info("parameters: {}", JsonUtil.toJson(WebHttpUtil.getRequestParameterMap(request)));
                this.logger.info("body: {}", WebHttpUtil.getRequestBodyString(request));
            }
            req = request;
        }
        chain.doFilter(req, resp);
    }

}
