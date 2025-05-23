package org.panda.tech.core.util.grouper;

import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

/**
 * 集合分组器
 *
 * @author fangen
 */
public class CollectionGrouper {
    /**
     * 按照特定元素个数平均分组
     *
     * @param list 分组集合
     * @param number 分组个数
     * @param <T> 集合元素类型
     * @return 分组后的集合
     */
    public static <T> List<List<T>> groupByNumber(List<T> list, int number) {
        if (CollectionUtils.isEmpty(list) || number < 1) {
            throw new IllegalArgumentException();
        }
        return list.stream().collect(CustomCollectors.groupByNumber(number));
    }
}
