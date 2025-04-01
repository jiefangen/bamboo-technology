package org.panda.tech.core.concurrent;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * 线程池工具类
 */
public class ExecutorUtil {

    private ExecutorUtil() {
    }

    public static final String DEFAULT_EXECUTOR_BEAN_NAME = "defaultExecutor"; // 自定义默认
    public static final String TASK_EXECUTOR_BEAN_NAME = "taskExecutor"; // 系统默认
    public static final String SCHEDULED_EXECUTOR_BEAN_NAME = "scheduledExecutor"; // 定时任务线程池
    /**
     * 一般4核8G的服务器。核心线程设置为8左右
     */
    public static final int DEFAULT_CORE_POOL_SIZE = 8;

    public static ExecutorService buildDefaultExecutor(int corePoolSize) {
        return new DefaultThreadPoolExecutor(corePoolSize);
    }

    public static ExecutorService buildDefaultExecutor() {
        return new DefaultThreadPoolExecutor(DEFAULT_CORE_POOL_SIZE);
    }

    public static ThreadPoolTaskExecutor buildTaskExecutor(int corePoolSize) {
        return new TaskThreadPoolExecutor(corePoolSize);
    }

    public static ScheduledExecutorService buildScheduledExecutor(int corePoolSize) {
        BasicThreadFactory factory = new BasicThreadFactory.Builder()
                .namingPattern(ScheduledExecutorService.class.getSimpleName() + "-%d")
                .daemon(true).build();
        return new ScheduledThreadPoolExecutor(corePoolSize, factory);
    }

    public static ScheduledExecutorService buildScheduledExecutor() {
        return buildScheduledExecutor(DEFAULT_CORE_POOL_SIZE);
    }

}
