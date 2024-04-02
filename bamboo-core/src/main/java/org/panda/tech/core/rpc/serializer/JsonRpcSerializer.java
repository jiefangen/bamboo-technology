package org.panda.tech.core.rpc.serializer;

import org.panda.tech.core.serializer.JsonSerializer;
import org.springframework.stereotype.Component;

/**
 * JSON-RPC序列化器
 */
@Component
public class JsonRpcSerializer extends JsonSerializer implements RpcSerializer {

}