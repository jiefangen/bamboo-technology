package org.panda.tech.core.config.app.security.executor.strategy.client;

import org.panda.tech.core.config.app.security.model.AppServiceModel;
import org.panda.tech.core.rpc.annotation.RpcClient;
import org.panda.tech.core.rpc.annotation.RpcMethod;
import org.panda.tech.core.web.config.WebConstants;
import org.panda.tech.core.web.restful.RestfulResult;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 认证授权客户端实现
 *
 * @author fangen
 **/
@Component
@RpcClient(serverRoot = "http://localhost:11006")
public class AuthServerClientImpl implements AuthServerClient {

    @RpcMethod("/auth/login")
    public RestfulResult<String> login(@RequestParam("service") String service, @RequestParam("username") String username,
                                       @RequestParam("password") String password) {
        return null;
    }

    @RpcMethod("/auth/login")
    public RestfulResult<String> loginByCredentials(@RequestHeader(WebConstants.HEADER_SECRET_KEY) String secretKey,
                                                    @RequestHeader(WebConstants.HEADER_AUTH_CREDENTIALS) String credentials,
                                                    @RequestParam("service") String service) {
        return null;
    }

    @RpcMethod("/auth/access/validate")
    public RestfulResult<?> validate(@RequestHeader(WebConstants.HEADER_AUTH_JWT) String authToken,
                                     @RequestParam("service") String service) {
        return null;
    }

    @RpcMethod("/auth/service/authorize")
    public RestfulResult<?> authorize(@RequestBody AppServiceModel appServiceModel) {
        System.out.println("请求");
        return null;
    }
}
