package org.panda.tech.core.spec.log.util;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.panda.bamboo.common.constant.basic.Strings;
import org.panda.bamboo.common.constant.basic.Times;
import org.panda.bamboo.common.util.LogUtil;
import org.panda.bamboo.common.util.date.TemporalUtil;
import org.panda.bamboo.core.context.SpringContextHolder;
import org.panda.tech.core.spec.log.trace.TraceContext;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;

/**
 * 动态日志记录工具
 *
 * @author fangen
 * @since 2025/5/30
 **/
public class DynamicLogUtil {
    /**
     * 动态日志写入内容
     *
     * @param clazz 反射类
     * @param template 内容模版
     * @param args 内容参数
     */
    public static void writerContent(Class<?> clazz, String template, Object... args) {
        writerContent(clazz.getSimpleName(), template, args);
    }

    /**
     * 动态日志写入内容
     *
     * @param name 文件名称
     * @param template 内容模版
     * @param args 内容参数
     */
    public static void writerContent(String name, String template, Object... args) {
        String enable = SpringContextHolder.getProperty("logging.dynamic.enable");
        if (StringUtils.isEmpty(enable) || !"true".equals(enable)) {
            return;
        }
        try {
            File file = getLogFile(StringUtils.isEmpty(name) ? "info" : name);
            FileWriter writer = new FileWriter(file, true);
            String traceId = TraceContext.getTraceId();
            if (StringUtils.isEmpty(traceId)) {
                Thread currentThread = Thread.currentThread();
                traceId = currentThread.getName() + ":" + currentThread.getId();
            }
            String content = TemporalUtil.formatLong(Instant.now()) + " |-DYNAMIC  ["+ traceId + "] "
                    + LogUtil.format(template, args) + System.lineSeparator();
            IOUtils.write(content, writer);
            writer.close();
        } catch (IOException e) {
            LogUtil.error(DynamicLogUtil.class, e);
        }
    }

    private static File getLogFile(String name) throws IOException {
        String projectPath = System.getProperty("user.dir"); // 运行项目工程根
        String currentDate = TemporalUtil.format(Instant.now(), Times.SHORT_DATE_NO_DELIMITER_PATTERN);
        String pathname = projectPath + Strings.SLASH + "dynamicLogs/" + currentDate + Strings.SLASH + name + ".log";
        File file = new File(pathname);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        if (!file.exists()) {
            file.createNewFile();
        }
        return file;
    }
}
