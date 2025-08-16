package org.panda.tech.core.boot;

import org.panda.bamboo.common.util.lang.MathUtil;

/**
 * Spring容器启动完成后延时执行的执行器
 */
public interface ApplicationContextDelayRunner extends ApplicationContextRunner {

    long DEFAULT_MIN_DELAY_MILLIS = 3000;

    default long getDelayMillis() {
        // 默认延时启动毫秒数为[最小延时,两倍最小延时]之间的随机数，以尽量错开初始化执行的启动时间
        return MathUtil.randomLong(DEFAULT_MIN_DELAY_MILLIS, DEFAULT_MIN_DELAY_MILLIS * 2);
    }
}
