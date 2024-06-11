package org.panda.tech.shiro.mgt;

import org.apache.shiro.subject.Subject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Subject管理器
 *
 * @author fangen
 * @since JDK 11
 */
public interface SubjectManager {

    Subject getSubject(HttpServletRequest request, HttpServletResponse response);

    Subject getSubject(HttpServletRequest request, HttpServletResponse response,
            Class<?> userClass);

}
