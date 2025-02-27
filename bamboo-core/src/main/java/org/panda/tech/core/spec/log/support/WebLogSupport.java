package org.panda.tech.core.spec.log.support;

import cn.hutool.http.useragent.UserAgent;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.panda.bamboo.common.annotation.helper.EnumValueHelper;
import org.panda.bamboo.common.constant.basic.Strings;
import org.panda.bamboo.common.util.LogUtil;
import org.panda.bamboo.common.util.jackson.JsonUtil;
import org.panda.tech.core.spec.log.annotation.WebOperationLog;
import org.panda.tech.core.web.context.SpringWebContext;
import org.panda.tech.core.web.model.WebLogRange;
import org.panda.tech.core.web.mvc.servlet.http.BodyRepeatableRequestWrapper;
import org.panda.tech.core.web.util.WebHttpUtil;
import org.slf4j.Logger;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;

import javax.servlet.http.HttpServletRequest;

/**
 * Web日志操作记录支持
 */
@Order(0)
public abstract class WebLogSupport {
    private static final Logger LOGGER = LogUtil.getLogger(WebLogSupport.class);

    protected static ThreadLocal<WebLogRange> threadLocal = new ThreadLocal<>();

    protected void doBefore(JoinPoint joinPoint, WebOperationLog webOperationLog) {
        WebLogRange threadInfo = new WebLogRange();
        threadInfo.setStartTimeMillis(System.currentTimeMillis());
        // 组装请求参数体中的内容
        HttpServletRequest request = SpringWebContext.getRequest();
        String userAgentHeader = request.getHeader(HttpHeaders.USER_AGENT);
        UserAgent userAgent = WebHttpUtil.getUserAgent(userAgentHeader);
        if (userAgent != null) {
            threadInfo.setTerminalDevice(userAgent.getBrowser().getName() + Strings.SPACE + userAgent.getVersion());
            threadInfo.setTerminalOs(userAgent.getOs().getName() + Strings.SPACE + userAgent.getOsVersion());
        }
        if (StringUtils.isEmpty(webOperationLog.content())) {
            threadInfo.setContent(WebHttpUtil.getRelativeRequestUrl(request));
        } else {
            threadInfo.setContent(webOperationLog.content());
        }
        request = new BodyRepeatableRequestWrapper(request);
        threadInfo.setHost(WebHttpUtil.getRemoteAddress(request));
        threadInfo.setIdentity(getIdentity(request));
        String actionTypeValue = EnumValueHelper.getValue(webOperationLog.actionType());
        threadInfo.setActionType(actionTypeValue);
        threadInfo.setBodyStr(WebHttpUtil.getRequestBodyString(request));
        threadLocal.set(threadInfo);
        LOGGER.debug("{} starts calling: requestData={}", threadInfo.getContent(), JsonUtil.toJson(threadInfo));
    }

    protected void doAfterReturning(WebOperationLog webOperationLog, Object res) {
        WebLogRange threadInfo = threadLocal.get();
        long startTimeMillis = threadInfo.getStartTimeMillis();
        long takeTime = System.currentTimeMillis() - startTimeMillis;
        threadInfo.setTakeTime(takeTime);
        if (webOperationLog.intoStorage()) {
            this.storageLog(res);
        }
        threadLocal.remove();
        LOGGER.debug("{} end call: elapsed time={}ms,result={}", threadInfo.getContent(), takeTime, res);
    }

    protected void doAfterThrowing(WebOperationLog webOperationLog, Throwable throwable) {
        if (webOperationLog.intoStorage()) {
            this.storageLog(throwable);
        }
        threadLocal.remove();
        LOGGER.error("{} invoke exception,exception information: {}", webOperationLog.content(), throwable);
    }

    protected abstract String getIdentity(HttpServletRequest request);

    protected abstract void storageLog(Object res);
}
