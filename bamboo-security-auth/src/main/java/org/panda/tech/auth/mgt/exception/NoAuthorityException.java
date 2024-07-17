package org.panda.tech.auth.mgt.exception;

import org.panda.bamboo.common.constant.basic.Strings;
import org.panda.tech.auth.authority.Authority;
import org.panda.tech.core.exception.ExceptionEnum;
import org.panda.tech.core.exception.business.auth.NoOperationAuthorityException;

/**
 * 没有授权异常
 *
 * @author fangen
 */
public class NoAuthorityException extends NoOperationAuthorityException {

    private static final long serialVersionUID = -2641659636351257342L;

    public NoAuthorityException(Authority authority) {
        super(ExceptionEnum.AUTH_NO_OPERA.getMessage() + Strings.COLON + authority.toString());
    }

}
