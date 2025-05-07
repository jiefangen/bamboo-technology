package org.panda.tech.shiro.authentication;

import javax.servlet.http.Cookie;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 默认的登录信息实现
 *
 * @author fangen
 */
public class DefaultLoginInfo implements LoginInfo {

    private Object user;

    private List<Cookie> cookies = new ArrayList<>();

    public DefaultLoginInfo(Object user) {
        this.user = user;
    }

    @Override
    public Object getUser() {
        return this.user;
    }

    @Override
    public Collection<Cookie> getCookies() {
        return this.cookies;
    }

    public void addCookie(final Cookie cookie) {
        this.cookies.add(cookie);
    }

}
