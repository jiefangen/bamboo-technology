package org.panda.tech.core.exception.business;

/**
 * 可处理的异常<br/>
 * 仅作为标识，在进行异常处理时便于判断
 *
 * @author fangen
 * @since JDK 11
 */
public abstract class HandleableException extends Exception {

    private static final long serialVersionUID = 7354083075880723483L;

    public HandleableException() {
        super();
    }

    public HandleableException(final String message) {
        super(message);
    }

}
