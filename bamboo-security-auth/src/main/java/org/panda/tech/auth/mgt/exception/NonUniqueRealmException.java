package org.panda.tech.auth.mgt.exception;

/**
 * 非唯一Realm异常
 *
 * @author fangen
 * @since JDK 11
 */
public class NonUniqueRealmException extends RuntimeException {

    private static final long serialVersionUID = 7924003789277415217L;

    public NonUniqueRealmException() {
        super("There are more than one Realms");
    }

}
