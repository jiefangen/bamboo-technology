package org.panda.tech.core.config.app;

/**
 * 应用的对外门面
 */
public class AppFacade {

    private String name;
    private String caption;
    private String business;
    private String contextPath;
    private String directUri;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCaption() {
        return this.caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getBusiness() {
        return this.business;
    }

    public void setBusiness(String business) {
        this.business = business;
    }

    public String getContextPath() {
        return contextPath;
    }

    public void setContextPath(String contextPath) {
        this.contextPath = contextPath;
    }

    public String getDirectUri() {
        return directUri;
    }

    public void setDirectUri(String directUri) {
        this.directUri = directUri;
    }
}
