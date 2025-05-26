package org.panda.tech.core.rpc.lb;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 轮询负载均衡器
 **/
public class RoundRobinLoadBalancer<T> implements LoadBalancer<T> {

    private final AtomicInteger index = new AtomicInteger(0);
    private volatile List<T> nodes = Collections.emptyList();

    public RoundRobinLoadBalancer(List<T> nodes) {
        updateNodes(nodes);
    }

    @Override
    public T select() {
        List<T> currentNodes = this.nodes;
        if (currentNodes.isEmpty()) {
            return null;
        }
        int currentIndex = Math.abs(index.getAndIncrement());
        return currentNodes.get(currentIndex % currentNodes.size());
    }

    @Override
    public void updateNodes(List<T> newNodes) {
        if (newNodes == null) {
            this.nodes = Collections.emptyList();
        } else {
            this.nodes = new ArrayList<>(newNodes); // 禁止外部修改
        }
    }
}
