package org.panda.tech.core.rpc.constant;

/**
 * RPC常量类
 **/
public class RpcConstants {

    private RpcConstants() {
    }

    /**
     * RPC通信凭证key
     */
    public static final String CREDENTIALS_KEY = "6N7BfKJCMDhhTF3ru86TuXeM4BXXTYBH";

    /**
     * 头信息名：RPC业务类型
     */
    public static final String HEADER_RPC_TYPE = "Rpc-Type";
    /**
     * 头信息名：RPC内部通信凭证
     */
    public static final String HEADER_RPC_CREDENTIALS = "Rpc-Credentials";
    /**
     * RPC调用前缀
     */
    public static final String URL_RPC_PREFIX = "/rpc/invoke";

    /**
     * RPC无效凭证
     */
    public static final int INVALID_CREDENTIALS_CODE = 4100;
    public static final String INVALID_CREDENTIALS = "Invalid rpc credentials";

    /**
     * RPC调用异常
     */
    public static final int EXC_RPC_INVOKER_CODE = 5200;
    public static final String EXC_RPC_INVOKER = "Rpc invoker failure";
    /**
     * 无效的RPC客户端ID
     */
    public static final String EXC_RPC_ILLEGAL_BEAN = "Illegal Rpc client beanId";
    /**
     * 未配置RPC注解
     */
    public static final String EXC_RPC_NOT_CONFIG = "Rpc annotations are not configured";
    /**
     * 无效的URL根路径
     */
    public static final String EXC_RPC_ILLEGAL_ROOT = "Illegal Rpc urlRoot";
    /**
     * 内部请求服务异常
     */
    public static final String EXC_RPC_NO_SERVICE = "Internal request service exception, urlRoot: %s";

}
