package com.qiu.common.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @description: 返回网关校验的接口实体类
 * @className: InvokeInterface.java
 * @author: qiu
 * @createTime: 2024/3/21 20:06
 */
@Data
public class InvokeInterface implements Serializable {

    private static final long serialVersionUID = 69097568682793932L;

    private Long id;

    private String name;

    private String description;

    private String host;

    private String url;

    private String requestParams;

    private String requestHeader;

    private String responseHeader;

    private Integer status;

    private String method;

}
