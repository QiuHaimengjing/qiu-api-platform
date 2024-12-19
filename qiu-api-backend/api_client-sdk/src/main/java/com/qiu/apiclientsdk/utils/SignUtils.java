package com.qiu.apiclientsdk.utils;

import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;

/**
 * @description: 签名生成工具类
 * @className: SignUtils.java
 * @author: qiu
 * @createTime: 2024/3/14 22:09
 */
public class SignUtils {

    /**
     * @description: 客户端签名算法
     * @params: [body, secretKey]
     * @return: java.lang.String
     * @author: qiu
     * @dateTime: 2024/3/15 0:08
     */
    public static String getSign(String body, String secretKey) {
        Digester sha256 = new Digester(DigestAlgorithm.SHA256);
        return sha256.digestHex(body + "." + secretKey);
    }
}
