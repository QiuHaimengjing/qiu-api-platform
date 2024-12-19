package com.qiu.common.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @description: 返回网关校验的用户密钥对实体类
 * @className: User.java
 * @author: qiu
 * @createTime: 2024/3/21 19:24
 */
@Data
public class InvokeUser implements Serializable {

    private static final long serialVersionUID = 2872982712839886750L;

    private Long id;

    private String accessKey;

    private String secretKey;
}
