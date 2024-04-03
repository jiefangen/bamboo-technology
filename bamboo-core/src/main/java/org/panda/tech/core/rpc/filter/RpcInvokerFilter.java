package org.panda.tech.core.rpc.filter;

import org.apache.commons.lang3.StringUtils;
import org.panda.bamboo.common.constant.basic.Strings;
import org.panda.tech.core.crypto.aes.AesEncryptor;
import org.panda.tech.core.crypto.sha.ShaEncryptor;
import org.panda.tech.core.rpc.RpcConstants;
import org.panda.tech.core.web.restful.RestfulResult;
import org.panda.tech.core.web.util.WebHttpUtil;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * RPC调用过滤器
 * 被调用的内部服务需注册验证转换
 *
 * @author fangen
 **/
@Order(Ordered.HIGHEST_PRECEDENCE) // 优先于其它过滤器执行
public class RpcInvokerFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        String url = WebHttpUtil.getRelativeRequestUrl(request);
        if (url.startsWith(RpcConstants.URL_RPC_PREFIX)) { // 内部RPC调用拦截处理
            String credentials = WebHttpUtil.getHeader(request, RpcConstants.HEADER_RPC_CREDENTIALS);
            String type = WebHttpUtil.getHeader(request, RpcConstants.HEADER_RPC_TYPE);
            // 通信凭证合法性校验
            if (StringUtils.isNotEmpty(credentials) && StringUtils.isNotEmpty(type)) {
                AesEncryptor aesEncryptor = new AesEncryptor();
                String secretKey = aesEncryptor.encrypt(type, RpcConstants.CREDENTIALS_KEY);
                ShaEncryptor shaEncryptor = new ShaEncryptor();
                if (credentials.equals(shaEncryptor.encrypt(secretKey))) {
                    String targetUrl = url.replace(RpcConstants.URL_RPC_PREFIX, Strings.EMPTY);
                    // 转发到内部真实接口，不再继续处理后续的拦截器或者处理器
                    WebHttpUtil.forward(req, resp, targetUrl);
                } else {
                    RestfulResult<?> failureResult = RestfulResult.failure(RpcConstants.INVALID_CREDENTIALS_CODE,
                            RpcConstants.INVALID_CREDENTIALS);
                    WebHttpUtil.buildJsonResponse((HttpServletResponse) resp, failureResult);
                    return;
                }
            }
        }
        // 不是RPC调用直接放行，进行后续相关拦截器处理
        chain.doFilter(req, resp);
    }

}
