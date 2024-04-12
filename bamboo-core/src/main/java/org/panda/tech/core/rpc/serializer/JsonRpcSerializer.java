package org.panda.tech.core.rpc.serializer;

import org.panda.tech.core.serializer.JsonSerializer;

import java.util.List;

/**
 * JSON-RPC序列化器
 */
public class JsonRpcSerializer extends JsonSerializer implements RpcSerializer {

    @Override
    public Object deserializeBean(String s, Class<?> type) {
        if (List.class.isAssignableFrom(type)) {
            return super.deserializeList(s, type);
        } else if (Object[].class.isAssignableFrom(type)) {
            return super.deserializeArray(s);
        }
        return super.deserialize(s, type);
    }
}