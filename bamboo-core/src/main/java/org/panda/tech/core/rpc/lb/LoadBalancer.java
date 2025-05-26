package org.panda.tech.core.rpc.lb;

import java.util.List;

/**
 * 负载均衡接口定义
 */
public interface LoadBalancer<T> {
    T select();
    void updateNodes(List<T> nodes);
}
