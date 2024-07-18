package org.panda.tech.auth.authentication.token;

import org.panda.bamboo.common.model.tuple.Binary;

/**
 * 手机号+验证码形式的登录Token，类似Shiro中的UsernamePasswordToken
 *
 * @author fangen
 */
public class SmsVerifyToken implements RememberMeAuthenticationToken, HostAuthenticationToken {

    private String mobilePhone;

    private boolean rememberMe;

    private String host;

    private Long verifyId;
    private String code;

    public SmsVerifyToken() {
    }

    public SmsVerifyToken(String mobilePhone, Long verifyId, String code) {
        super();
        this.mobilePhone = mobilePhone;
        this.verifyId = verifyId;
        this.code = code;
    }

    public Binary<Long, String> getBinary() {
        return new Binary<Long, String>(this.verifyId, this.code);
    }

    public Long getVerifyId() {
        return this.verifyId;
    }

    public void setVerifyId(final Long verifyId) {
        this.verifyId = verifyId;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(final String code) {
        this.code = code;
    }

    public void setMobilePhone(final String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getMobilePhone() {
        return this.mobilePhone;
    }

    @Override
    public boolean isRememberMe() {
        return this.rememberMe;
    }

    public void setRememberMe(final boolean rememberMe) {
        this.rememberMe = rememberMe;
    }

    public void setHost(final String host) {
        this.host = host;
    }

    @Override
    public String getHost() {
        return this.host;
    }

    @Override
    public Object getPrincipal() {
        return this.getMobilePhone();
    }

    @Override
    public Object getCredentials() {
        return this.getBinary();
    }

}
