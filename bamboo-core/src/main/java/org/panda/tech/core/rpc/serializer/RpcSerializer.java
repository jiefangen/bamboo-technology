package org.panda.tech.core.rpc.serializer;

import org.panda.tech.core.serializer.StringSerializer;

/**
 * RPC序列化器
 */
public interface RpcSerializer extends StringSerializer {

    /**
     * RPC反序列化为Bean
     *
     * @param s 序列化字符串
     * @param type 期望类型
     * @param subType 期望类型中的子类型
     * @return Bean
     */
    Object deserializeBean(String s, Class<?> type, Class<?> subType);

}
