package org.panda.tech.core.rpc.exception;

import org.panda.tech.core.exception.business.BusinessException;
import org.panda.tech.core.rpc.RpcConstants;

/**
 * RPC调用的异常
 */
public class RpcInvokerException extends BusinessException {

    private static final long serialVersionUID = -6426668472065294328L;

    public RpcInvokerException(String message) {
        super(RpcConstants.EXC_RPC_INVOKER_CODE, message);
    }

    public RpcInvokerException() {
        super(RpcConstants.EXC_RPC_INVOKER_CODE, RpcConstants.EXC_RPC_INVOKER);
    }

}
