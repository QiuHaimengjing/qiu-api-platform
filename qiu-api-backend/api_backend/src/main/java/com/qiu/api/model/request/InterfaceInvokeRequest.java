package com.qiu.api.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @description: 接口调用请求体对象
 * @className: InterfaceInvokeRequest.java
 * @author: qiu
 * @createTime: 2024/3/17 18:30
 */
@Data
@ApiModel(value = "InterfaceInvokeRequest", description = "接口调用请求体对象")
public class InterfaceInvokeRequest implements Serializable {

    private static final long serialVersionUID = 8581854646336362257L;

    @ApiModelProperty(value = "接口id", required = true)
    private Long id;

    @ApiModelProperty(value = "请求参数")
    private String requestParams;

}
