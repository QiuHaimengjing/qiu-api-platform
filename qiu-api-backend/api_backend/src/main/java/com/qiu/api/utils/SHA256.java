package com.qiu.api.utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.security.SecureRandom;
import java.util.Base64;

/**
 * @description: SHA-256加密
 * @className: SHA256.java
 * @author: qiu
 * @createTime: 2023/12/9 12:39
 */
@Data
@AllArgsConstructor
public class SHA256 {

    // 加密盐
    private String salt;

    // SHA-256密码
    private String sha256Password;

    // ak加密盐
    private static final String akSalt = "MeiBronya";

    // sk加密盐
    private static final String skSalt = "KianaK423QiuApi";

    /**
     * @description: 加密
     * @params: [originalPassword]
     * @return: com.qiu.api.utils.SHA256
     * @author: qiu
     * @dateTime: 2023/12/9 13:01
     */
    public static SHA256 encryptionPassword(String originalPassword) {
        // 1.加盐，生成随机盐
        int saltLength = 16;
        byte[] bytes = new byte[saltLength];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(bytes);
        String salt = Base64.getEncoder().encodeToString(bytes);
        // 2.盐和密码合并
        String saltedPassword = StrUtil.format("{}{}", salt, originalPassword);
        String finalPassword = SecureUtil.sha256(saltedPassword);
        return new SHA256(salt, finalPassword);
    }

    /**
     * @description: 解密
     * @params: [salt, originalPassword]
     * @return: java.lang.String
     * @author: qiu
     * @dateTime: 2023/12/9 13:08
     */
    public static String decryptPassword(String salt, String originalPassword) {
        // 用户输入密码与盐合并
        String saltInputPassword = StrUtil.format("{}{}", salt, originalPassword);
        // 使用相同哈希函数对合并后的字符串进行哈希处理
        return SecureUtil.sha256(saltInputPassword);
    }

    /**
     * @description: ak加密
     * @params: [originalPassword]
     * @return: java.lang.String
     * @author: qiu
     * @dateTime: 2024/3/22 14:11
     */
    public static String encryptionPasswordForAk(String originalPassword) {
        String saltedPassword = StrUtil.format("{}{}", akSalt, originalPassword);
        return SecureUtil.sha256(saltedPassword);
    }

    /**
     * @description: sk加密
     * @params: [originalPassword]
     * @return: java.lang.String
     * @author: qiu
     * @dateTime: 2024/3/22 14:11
     */
    public static String encryptionPasswordForSk(String originalPassword) {
        String saltedPassword = StrUtil.format("{}{}", skSalt, originalPassword);
        return SecureUtil.sha256(saltedPassword);
    }

    /**
     * @description: ak解密
     * @params: [originalPassword]
     * @return: java.lang.String
     * @author: qiu
     * @dateTime: 2024/3/22 14:12
     */
    public static String decryptPasswordForAk(String originalPassword) {
        return decryptPassword(akSalt, originalPassword);
    }

    /**
     * @description: sk解密
     * @params: [originalPassword]
     * @return: java.lang.String
     * @author: qiu
     * @dateTime: 2024/3/22 14:12
     */
    public static String decryptPasswordForSk(String originalPassword) {
        return decryptPassword(skSalt, originalPassword);
    }
}
