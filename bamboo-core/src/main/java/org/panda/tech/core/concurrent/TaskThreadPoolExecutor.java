package org.panda.tech.core.concurrent;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * 异步任务线程池执行器
 */
public class TaskThreadPoolExecutor extends ThreadPoolTaskExecutor implements DisposableBean {

    private static final long serialVersionUID = 2718131720208086502L;

    public TaskThreadPoolExecutor(int corePoolSize, int queueCapacity, int maxPoolSize) {
        // 核心线程数：核心线程会一直存活，即使没有任务需要执行
        setCorePoolSize(corePoolSize);
        // 阻塞队列：当核心线程数达到最大时，新任务会放在队列中排队等待执行
        setQueueCapacity(queueCapacity);
        // 最大线程数：任务队列已满时，线程池会创建新线程来处理任务，直到线程数量达到maxPoolSize。当线程超过后由自定义拒绝策略处理
        setMaxPoolSize(maxPoolSize);
        // 线程空闲时间：当线程空闲时间达到keepAliveTime时，线程会被销毁，直到线程数量等于corePoolSize
        setKeepAliveSeconds(30);
        // 线程池名的前缀：设置好了之后可以方便我们定位处理任务所在的线程池
        setThreadNamePrefix("my-async-task-");
        // 任务拒绝处理器：当线程池的线程数量达到maxPoolSize的时候，如何处理新的线程任务
        // CallerRunsPolicy：执行任务（这个策略重试添加当前的任务，它会自动重复调用execute()方法，直到成功）如果执行器已关闭则丢弃
        setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        initialize();
    }

    public TaskThreadPoolExecutor(int corePoolSize) {
        this(corePoolSize, corePoolSize * 5, corePoolSize * 10);
    }

    @Override
    public void destroy() {
        shutdown();
    }
}
