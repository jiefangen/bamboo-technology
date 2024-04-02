package org.panda.tech.core.spec.http;

import org.panda.bamboo.common.util.lang.StringUtil;
import org.springframework.http.HttpMethod;

/**
 * HTTP连接
 *
 * @author fangen
 */
public class HttpLink implements HttpResource {

    private static final long serialVersionUID = -6376110053750451144L;

    /**
     * 链接地址
     */
    private String href;
    /**
     * 方法类型
     */
    private HttpMethod method;

    /**
     * HTTP连接构造方法
     *
     * @param href
     *            链接地址
     */
    public HttpLink(String href) {
        this.href = href;
    }

    /**
     * HTTP连接构造方法
     *
     * @param href
     *            链接地址
     * @param method
     *            方法类型
     */
    public HttpLink(String href, HttpMethod method) {
        this.href = href;
        this.method = method;
    }

    /**
     * @return 链接地址
     */
    public String getHref() {
        return this.href;
    }

    /**
     * @param href
     *            链接地址
     */
    public void setHref(String href) {
        this.href = href;
    }

    /**
     * @return 方法类型
     */
    public HttpMethod getMethod() {
        return this.method;
    }

    /**
     * @param method
     *            方法类型
     */
    public void setMethod(HttpMethod method) {
        this.method = method;
    }

    /**
     * 判断是否匹配指定方法类型
     *
     * @param method 方法类型
     * @return 是否匹配指定方法类型
     */
    public boolean matches(HttpMethod method) {
        return this.method == null || this.method == method;
    }

    /**
     * 判断是否匹配指定链接地址和方法类型
     *
     * @param href 链接地址
     * @param method 方法类型
     * @return 是否匹配指定链接地址和方法类型
     */
    public boolean matches(String href, HttpMethod method) {
        return this.href != null && StringUtil.antPathMatch(href, this.href) && matches(method);
    }

    @Override
    public String toString() {
        return "[" + this.method.name() + "]" + this.href;
    }
}
