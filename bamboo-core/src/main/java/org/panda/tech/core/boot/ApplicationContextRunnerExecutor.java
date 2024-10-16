package org.panda.tech.core.boot;

import org.panda.bamboo.common.util.LogUtil;
import org.panda.bamboo.common.util.clazz.BeanUtil;
import org.panda.tech.core.concurrent.ExecutorUtil;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 在Spring Boot应用启动完成之后执行
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ApplicationContextRunnerExecutor implements ApplicationRunner, DisposableBean {

    @Autowired
    private ApplicationContext context;

    private final ScheduledExecutorService executor;

    public ApplicationContextRunnerExecutor(Optional<ScheduledExecutorService> executorOptional) {
        this.executor = executorOptional.orElseGet(ExecutorUtil::buildScheduledExecutor);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Map<String, ApplicationContextRunner> beans = this.context.getBeansOfType(ApplicationContextRunner.class);
        List<ApplicationContextRunner> list = new ArrayList<>(beans.values());
        list.sort(Comparator.comparingInt(ApplicationContextRunner::getOrder));
        for (ApplicationContextRunner bean : list) {
            if (bean instanceof ApplicationContextDelayRunner) {
                ApplicationContextDelayRunner runner = (ApplicationContextDelayRunner) bean;
                this.executor.schedule(() -> {
                    try {
                        runner.run(this.context);
                    } catch (Exception e) {
                        LogUtil.error(BeanUtil.getUltimateClass(runner), e);
                    }
                }, runner.getDelayMillis(), TimeUnit.MILLISECONDS);
            } else {
                try {
                    bean.run(this.context);
                } catch (Exception e) {
                    LogUtil.error(getClass(), e);
                }
            }
        }
    }

    @Override
    public void destroy() throws Exception {
        if (executor != null && !executor.isShutdown()) {
            executor.shutdown();
        }
    }
}
