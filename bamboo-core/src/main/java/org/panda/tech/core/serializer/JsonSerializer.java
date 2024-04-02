package org.panda.tech.core.serializer;

import org.panda.bamboo.common.util.jackson.JsonUtil;

import java.util.List;

/**
 * JSON字符串序列化器
 */
public class JsonSerializer implements StringSerializer {

    @Override
    public String serialize(Object bean) {
        return JsonUtil.toJson(bean);
    }

    @Override
    public <T> T deserialize(String s, Class<T> type) {
        return JsonUtil.json2Bean(s, type);
    }

    @Override
    public Object[] deserializeArray(String s) {
        final List<Object> list = JsonUtil.json2List(s);
        if (list != null) {
            return list.toArray(new Object[list.size()]);
        }
        return new Object[0];
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> List<T> deserializeList(String s, Class<T> elementType) {
        final List<T> list = JsonUtil.json2List(s, elementType);
        if (list != null) {
            // 类型反序列化时需针对void进行特殊处理，"void"可能被解析为null
            if (elementType == Class.class && s.contains("\"void\"") && s.indexOf("null") < 0) { // 包含void但不包含null时才处理
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i) == null) { // 反序列化为null的视为void类型
                        list.set(i, (T) void.class);
                    }
                }
            }
        }
        return list;
    }

}
