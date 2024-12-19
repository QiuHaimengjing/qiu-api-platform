package com.qiu.api.constant;

/**
 * @description: Redis常量接口
 * @className: RedisConstant.java
 * @author: qiu
 * @createTime: 2024/2/27 11:12
 */
public interface RedisConstant {

    // 头像上传限制
    String USER_UPLOAD_LIMIT_KEY = "qiuapi:user:upload:";
    Long USER_UPLOAD_LIMIT_TTL = 24L;
}
