package org.panda.tech.core.web.controller;

import org.panda.bamboo.common.constant.basic.Strings;
import org.panda.bamboo.common.util.lang.StringUtil;
import org.panda.tech.core.config.app.AppConstants;
import org.panda.tech.core.web.context.SpringWebContext;
import org.panda.tech.core.web.restful.RestfulResult;
import org.panda.tech.core.web.util.NetUtil;
import org.panda.tech.core.web.util.WebHttpUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 服务端健康检查控制器支持
 */
@RequestMapping(value = "/home")
public abstract class HomeControllerSupport {

    @Value(AppConstants.EL_SPRING_APP_NAME)
    private String name;
    @Value(AppConstants.EL_SPRING_PROFILES_ACTIVE)
    private String env;
    @Value(AppConstants.EL_SERVER_PORT)
    private String port;

    @GetMapping
    @ResponseBody
    public RestfulResult<Object> home() {
        HttpServletRequest request = SpringWebContext.getRequest();
        return RestfulResult.success(getApplicationMap(request));
    }

    @GetMapping(value = "/index")
    public ModelAndView index(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView(getIndexViewName());
        Map<String, Object> applicationMap = getApplicationMap(request);
        for (Map.Entry<String, Object> entry : applicationMap.entrySet()) {
            modelAndView.addObject(entry.getKey(), entry.getValue());
        }
        return modelAndView;
    }

    protected Map<String, Object> getApplicationMap(HttpServletRequest request) {
        Map<String, Object> applicationMap = new HashMap<>(6);
        applicationMap.put("appName", name);
        applicationMap.put("env", env);
        applicationMap.put("port", port);
        applicationMap.put("appDesc", getApplicationDesc());
        applicationMap.put("localHost", NetUtil.getLocalHost());
        applicationMap.put("remoteAddress", WebHttpUtil.getRemoteAddress(request));
        return applicationMap;
    }

    protected String getApplicationDesc() {
        if (name.contains(Strings.MINUS)) {
            String[] names = name.split(Strings.MINUS);
            return StringUtil.firstToUpperCase(names[0]) + Strings.SPACE + StringUtil.firstToUpperCase(names[1]);
        }
        return StringUtil.firstToUpperCase(name);
    }

    protected String getIndexViewName() {
        return "index";
    }

}
