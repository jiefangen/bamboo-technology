package org.panda.tech.core.boot;

import org.springframework.context.ApplicationContext;
import org.springframework.core.Ordered;

/**
 * 应用启动完成后按顺序执行的执行器
 *
 * @author fangen
 */
public interface ApplicationContextRunner extends Ordered {

    @Override
    default int getOrder() {
        return 0;
    }

    /**
     * 容器初始化完成后执行的动作
     *
     * @param context 容器上下文
     * @throws Exception 如果处理过程出现错误
     */
    void run(ApplicationContext context) throws Exception;

}
