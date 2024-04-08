package org.panda.tech.core.rpc.constant.enums;

import org.panda.bamboo.common.annotation.EnumValue;

/**
 * RPC通信模式
 */
public enum CommMode {

    @EnumValue("HttpClient")
    HTTP_CLIENT,

    @EnumValue("RestTemplate")
    REST_TEMPLATE,

    @EnumValue("gRPC")
    GRPC;

}
