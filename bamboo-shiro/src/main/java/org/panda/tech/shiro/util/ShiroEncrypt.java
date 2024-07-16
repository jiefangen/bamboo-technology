package org.panda.tech.shiro.util;

import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

/**
 * Shiro加密器
 *
 * @author fangen
 * @since 2020/5/13
 **/
public class ShiroEncrypt {

    private final RandomNumberGenerator randomNumberGenerator = new SecureRandomNumberGenerator();

    /**
     * 随机生成盐
     */
    public String getRandomSalt(){
        return randomNumberGenerator.nextBytes().toHex();
    }

    /**
     * 依据生成的盐加密
     */
    public String encryptPassword(String password, String salt){
        return new SimpleHash("md5", password, ByteSource.Util.bytes(salt), 2).toHex();
    }
}
