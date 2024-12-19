package com.qiu.api.model.request.interface_info;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @description: 接口创建请求体对象
 * @className: InterfaceAddRequest.java
 * @author: qiu
 * @createTime: 2024/3/12 13:16
 */
@Data
@ApiModel(value = "InterfaceAddRequest", description = "接口创建请求体对象")
public class InterfaceAddRequest implements Serializable {

    private static final long serialVersionUID = -3630307839649826551L;

    @ApiModelProperty(value = "名称", required = true)
    private String name;

    @ApiModelProperty(value = "描述", required = true)
    private String description;

    @ApiModelProperty(value = "服务器地址", required = true)
    private String host;

    @ApiModelProperty(value = "接口地址", required = true)
    private String url;

    @ApiModelProperty(value = "请求参数")
    private String requestParams;

    @ApiModelProperty(value = "请求头")
    private String requestHeader;

    @ApiModelProperty(value = "响应头")
    private String responseHeader;

    @ApiModelProperty(value = "接口状态 （0-关闭，1-开启）")
    private Integer status;

    @ApiModelProperty(value = "请求类型", required = true)
    private String method;

}
