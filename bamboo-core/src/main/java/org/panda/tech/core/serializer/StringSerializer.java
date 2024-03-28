package org.panda.tech.core.serializer;

import java.util.List;

/**
 * 基于字符串的序列化器
 */
public interface StringSerializer {

    /**
     * 序列化Bean
     *
     * @param bean
     *            Bean
     * @return 序列化后的字符串
     */
    String serialize(Object bean);

    /**
     * 反序列化为Bean
     *
     * @param s
     *            序列化字符串
     * @param type
     *            期望类型
     * @return Bean
     */
    <T> T deserialize(String s, Class<T> type);

    /**
     * 反序列化为数组
     *
     * @param s
     *            序列化字符串
     * @return 数组
     */
    Object[] deserializeArray(String s);

    /**
     * 反序列化为集合
     *
     * @param s
     *            序列化字符串
     * @param elementType
     *            集合元素类型
     * @return 集合
     */
    <T> List<T> deserializeList(String s, Class<T> elementType);

}
