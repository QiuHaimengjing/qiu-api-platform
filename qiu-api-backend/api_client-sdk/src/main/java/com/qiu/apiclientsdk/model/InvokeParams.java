package com.qiu.apiclientsdk.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @description: 调用请求参数
 * @className: InvokeParams.java
 * @author: qiu
 * @createTime: 2024/3/22 22:19
 */
@Data
public class InvokeParams implements Serializable {

    private static final long serialVersionUID = 4481484099307715704L;

    private String host;

    private String url;

    private String requestParams;

    private String method;

}
