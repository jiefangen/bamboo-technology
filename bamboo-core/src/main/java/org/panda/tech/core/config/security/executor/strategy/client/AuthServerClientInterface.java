package org.panda.tech.core.config.security.executor.strategy.client;

import org.panda.tech.core.config.security.model.AppServiceModel;
import org.panda.tech.core.rpc.annotation.RpcClient;
import org.panda.tech.core.rpc.annotation.RpcMethod;
import org.panda.tech.core.web.config.WebConstants;
import org.panda.tech.core.web.restful.RestfulResult;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 认证授权客户端实现
 *
 * @author fangen
 **/
@Component
@RpcClient("http://localhost:11006/auth")
public interface AuthServerClientInterface {

    @RpcMethod("/login")
    RestfulResult<String> login(@RequestParam("service") String service,
                                @RequestParam("username") String username,
                                @RequestParam("password") String password);
    @RpcMethod("/login")
    RestfulResult<String> loginByCredentials(@RequestHeader(WebConstants.HEADER_SECRET_KEY) String secretKey,
                                             @RequestHeader(WebConstants.HEADER_AUTH_CREDENTIALS) String credentials,
                                             @RequestParam("service") String service);

    @RpcMethod("/access/validate")
    RestfulResult<?> validate(@RequestHeader(WebConstants.HEADER_AUTH_JWT) String authToken,
                              @RequestParam("service") String service);

    @RpcMethod(value = "/service/authorize", method = RequestMethod.POST)
    RestfulResult<?> authorize(@RequestBody AppServiceModel appServiceModel);

}
