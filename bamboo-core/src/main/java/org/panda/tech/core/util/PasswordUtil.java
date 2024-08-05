package org.panda.tech.core.util;

import org.apache.commons.lang3.StringUtils;
import org.panda.bamboo.common.util.LogUtil;
import org.panda.tech.core.crypto.md5.Md5Encryptor;
import org.slf4j.Logger;

import java.nio.charset.StandardCharsets;

public class PasswordUtil {
    private static final Logger LOGGER = LogUtil.getLogger(PasswordUtil.class);

    // 密码加密掩码
    private static final String PASS_MASK = "JoQVxFdlaPDEnKteOdIbJmFXkBYHeQLqTKoG";
    // 密码加密噪音
    private static final String PASS_NOISE = "+ko(*o9N(*nldf8GH(7n,hs";

    /**
     * 用指定掩码对密码处理后，再用指定策略对处理后的密码加密
     * 从掩码中截取密码长度的子串，再和密码做异或，然后再加密
     *
     * @param password 密码
     * @param mask 掩码
     * @return 加密后的密码
     */
    public static String encryptPassword(String password, String mask) {
        if (StringUtils.isEmpty(password) || StringUtils.isEmpty(mask)) {
            return password;
        }

        String maskedPassword = mask.substring(0, password.length());
        byte[] pwdBytes = password.getBytes(StandardCharsets.UTF_8);
        byte[] keyBytes = maskedPassword.getBytes(StandardCharsets.UTF_8);

        for (int i = 0; i < pwdBytes.length; i++) {
            pwdBytes[i] ^= keyBytes[i];
        }
        //mask
        String encryptedPassword = bytes2HexString(pwdBytes);

        Md5Encryptor md5Encryptor = new Md5Encryptor();
        //1st md5
        encryptedPassword = md5Encryptor.encrypt(encryptedPassword);
        //append noise
        encryptedPassword += encryptedPassword + PASS_NOISE;
        //2nd md5
        encryptedPassword = md5Encryptor.encrypt(encryptedPassword);
        return encryptedPassword;
    }

	/**
     * 用指定掩码对密码处理后，再用指定策略对处理后的密码加密
     *
     * @param password 密码
     * @return 加密后的密码
     */
    public static String encryptPassword(String password) {
        return encryptPassword(password, PASS_MASK);
    }

    /**
     * 字节流转十六进制字符串
     */
    private static String bytes2HexString(byte[] bytes) {
        if (null == bytes) {
            return null;
        }
        StringBuilder strBuilder = new StringBuilder();
        for (byte b : bytes) {
            if (b < 0x10 && b >= 0) {
                strBuilder.append("0");
            }
            strBuilder.append(Integer.toHexString(((int) b) & 0xFF));
        }
        return strBuilder.toString().toUpperCase();
    }
}
