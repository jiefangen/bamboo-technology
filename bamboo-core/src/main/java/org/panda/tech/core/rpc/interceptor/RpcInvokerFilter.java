package org.panda.tech.core.rpc.interceptor;

import org.apache.commons.lang3.StringUtils;
import org.panda.bamboo.common.constant.basic.Strings;
import org.panda.tech.core.crypto.aes.AesEncryptor;
import org.panda.tech.core.crypto.sha.ShaEncryptor;
import org.panda.tech.core.rpc.RpcConstants;
import org.panda.tech.core.web.util.WebHttpUtil;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * RPC调用过滤器
 *
 * @author fangen
 **/
@Component
public class RpcInvokerFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String uri = WebHttpUtil.getRelativeRequestUrl(request);
        if (uri.startsWith(RpcConstants.URL_RPC_PREFIX)) { // 内部RPC调用拦截处理
            String credentials = WebHttpUtil.getHeader(request, RpcConstants.HEADER_RPC_CREDENTIALS);
            String type = WebHttpUtil.getHeader(request, RpcConstants.HEADER_RPC_TYPE);
            // 通信凭证合法性校验
            if (StringUtils.isNotEmpty(credentials) && StringUtils.isNotEmpty(type)) {
                AesEncryptor aesEncryptor = new AesEncryptor();
                String secretKey = aesEncryptor.encrypt(type, RpcConstants.CREDENTIALS_KEY);
                ShaEncryptor shaEncryptor = new ShaEncryptor();
                if (credentials.equals(shaEncryptor.encrypt(secretKey))) {
                    String targetUri = uri.replace(RpcConstants.URL_RPC_PREFIX, Strings.EMPTY);
                    // 转发到内部真实接口，不再继续处理后续的拦截器或者处理器
                    WebHttpUtil.forward(servletRequest, servletResponse, targetUri);
//                    return false;
                }
            }
        }
        // 不是RPC调用直接放行，进行后续相关拦截器处理
//        return true;
    }

}
