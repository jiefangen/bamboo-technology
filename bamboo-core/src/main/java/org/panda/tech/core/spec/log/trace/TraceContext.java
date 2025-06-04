package org.panda.tech.core.spec.log.trace;

import org.apache.commons.lang3.StringUtils;
import org.panda.bamboo.common.util.lang.UUIDUtil;
import org.slf4j.MDC;

import java.util.Map;

public class TraceContext {

    public static final String TRACE_ID_KEY = "traceId";

    private TraceContext() {}

    public static void setTraceId(String traceId) {
        MDC.put(TRACE_ID_KEY, traceId);
    }

    public static String getTraceId() {
        return MDC.get(TRACE_ID_KEY);
    }

    public static void clear() {
        MDC.remove(TRACE_ID_KEY);
    }

    /**
     * 如果不存在则生成并设置
     */
    public static String ensureTraceId() {
        String traceId = getTraceId();
        if (StringUtils.isEmpty(traceId)) {
            traceId = UUIDUtil.randomUUID32();
            setTraceId(traceId);
        }
        return traceId;
    }

    /**
     * 拷贝当前 MDC 上下文（适用于异步线程）
     */
    public static Map<String, String> copyContextMap() {
        return MDC.getCopyOfContextMap();
    }

    /**
     * 恢复 MDC 上下文
     */
    public static void restoreContextMap(Map<String, String> contextMap) {
        MDC.setContextMap(contextMap);
    }
}
