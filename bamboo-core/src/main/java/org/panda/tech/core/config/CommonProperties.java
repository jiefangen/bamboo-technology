package org.panda.tech.core.config;

import org.apache.commons.lang3.StringUtils;
import org.panda.bamboo.common.constant.basic.Strings;
import org.panda.bamboo.common.util.lang.CollectionUtil;
import org.panda.tech.core.config.app.AppConfiguration;
import org.panda.tech.core.config.app.AppFacade;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.*;

/**
 * Web通用配置属性
 */
@Configuration
@ConfigurationProperties("bamboo.common")
public class CommonProperties implements InitializingBean {

    private Map<String, AppConfiguration> apps = new HashMap<>(); // 必须用Map，在Spring占位符表达式中才可以取指定应用的配置
    private boolean gatewayEnabled;
    private String gatewayUri;
    /**
     * 受保护应用服务鉴权的URL模式
     */
    private List<String> authUrlPatterns;
    /**
     * 分布式全局统一认证授权环境参量
     * key-运行环境；value-服务根路径
     */
    private Map<String, String> authEnvs = new HashMap<>();
    /**
     * RPC内部全局调用根路径
     * key-服务beanId；value-服务根路径
     */
    private Map<String, String> serviceRoot = new HashMap<>();

    public Map<String, AppConfiguration> getApps() {
        return Collections.unmodifiableMap(this.apps);
    }

    public void setApps(Map<String, AppConfiguration> apps) {
        this.apps = CollectionUtil.sortedByValueMap(apps, (app1, app2) -> {
            int result = app1.getContextUri(false).compareTo(app2.getContextUri(false));
            if (result == 0) {
                result = app1.getContextPath().compareTo(app2.getContextPath());
            }
            return -result; // 反向排序，以避免错误匹配
        });
    }

    public boolean isGatewayEnabled() {
        return this.gatewayEnabled;
    }

    public void setGatewayEnabled(boolean gatewayEnabled) {
        this.gatewayEnabled = gatewayEnabled;
    }

    public String getGatewayUri() {
        return this.gatewayUri;
    }

    public void setGatewayUri(String gatewayUri) {
        this.gatewayUri = gatewayUri;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (this.gatewayEnabled) {
            if (StringUtils.isNotBlank(this.gatewayUri)) {
                this.apps.values().forEach(app -> {
                    // 应用未配置特殊的网关地址，则用通用网关地址作为应用网关地址
                    if (StringUtils.isBlank(app.getGatewayUri())) {
                        app.setGatewayUri(this.gatewayUri);
                    }
                });
            }
        } else { // 不开启网关访问，则将所有应用的网关地址置为空
            this.apps.values().forEach(app -> {
                app.setGatewayUri(null);
            });
        }
    }

    public int getAppSize() {
        return this.apps.size();
    }

    public AppConfiguration getApp(String name) {
        return name == null ? null : this.apps.get(name);
    }

    public String findAppName(String url, boolean direct) {
        if (url != null) {
            for (Map.Entry<String, AppConfiguration> entry : this.apps.entrySet()) {
                AppConfiguration configuration = entry.getValue();
                String contextUri = configuration.getContextUri(direct);
                if (url.equals(contextUri) || url.startsWith(contextUri + Strings.SLASH)
                        || url.startsWith(contextUri + Strings.WELL) || url.startsWith(contextUri + Strings.QUESTION)) {
                    return entry.getKey();
                }
            }
        }
        return null;
    }

    /**
     * 获取所有应用URI，提供给cors配置，作为允许跨域访问的地址清单
     *
     * @return 所有应用URI
     */
    public Set<String> getAllAppUris() {
        Set<String> uris = new HashSet<>();
        this.apps.forEach((name, app) -> {
            if (StringUtils.isNotBlank(app.getGatewayUri())) {
                uris.add(app.getGatewayUri());
            }
            uris.add(app.getDirectUri());
        });
        return uris;
    }

    public Map<String, String> getAppContextUriMapping() {
        Map<String, String> urls = new HashMap<>();
        this.apps.forEach((name, app) -> {
            urls.put(name, app.getContextUri(false));
        });
        return urls;
    }

    public AppFacade getAppFacade(String name, boolean relativeContextUri) {
        AppConfiguration appConfig = getApp(name);
        if (appConfig == null) {
            return null;
        }
        AppFacade basic = new AppFacade();
        basic.setName(name);
        basic.setCaption(appConfig.getCaption());
        basic.setBusiness(appConfig.getBusiness());
        if (relativeContextUri) {
            basic.setContextPath(appConfig.getContextPath());
        } else {
            basic.setContextPath(appConfig.getContextUri(false));
        }
        basic.setDirectUri(appConfig.getDirectUri());
        return basic;
    }

    public List<String> getAuthUrlPatterns() {
        return authUrlPatterns;
    }

    public void setAuthUrlPatterns(List<String> authUrlPatterns) {
        this.authUrlPatterns = authUrlPatterns;
    }

    public Map<String, String> getAuthEnvs() {
        return authEnvs;
    }

    public void setAuthEnvs(Map<String, String> authEnvs) {
        this.authEnvs = authEnvs;
    }

    public Map<String, String> getServiceRoot() {
        return serviceRoot;
    }

    public void setServiceRoot(Map<String, String> serviceRoot) {
        this.serviceRoot = serviceRoot;
    }
}
