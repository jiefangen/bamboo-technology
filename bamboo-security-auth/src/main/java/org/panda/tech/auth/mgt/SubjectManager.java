package org.panda.tech.auth.mgt;

import org.panda.tech.auth.subject.Subject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Subject管理器
 *
 * @author fangen
 */
public interface SubjectManager {

    Subject getSubject(HttpServletRequest request, HttpServletResponse response);

    Subject getSubject(HttpServletRequest request, HttpServletResponse response,
                       Class<?> userClass);

}
