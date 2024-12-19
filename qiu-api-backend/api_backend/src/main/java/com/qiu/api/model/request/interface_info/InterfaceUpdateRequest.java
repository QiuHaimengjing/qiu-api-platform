package com.qiu.api.model.request.interface_info;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @description: 接口更新信息请求体对象
 * @className: InterfaceUpdateRequest.java
 * @author: qiu
 * @createTime: 2024/3/12 13:24
 */
@Data
@ApiModel(value = "InterfaceUpdateRequest", description = "接口更新信息请求体对象")
public class InterfaceUpdateRequest implements Serializable {

    private static final long serialVersionUID = -8047081231916614670L;

    @ApiModelProperty(value = "接口id", required = true)
    private Long id;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "服务器地址")
    private String host;

    @ApiModelProperty(value = "接口地址")
    private String url;

    @ApiModelProperty(value = "请求参数")
    private String requestParams;

    @ApiModelProperty(value = "请求头")
    private String requestHeader;

    @ApiModelProperty(value = "响应头")
    private String responseHeader;

    @ApiModelProperty(value = "接口状态 （0-关闭，1-开启）")
    private Integer status;

    @ApiModelProperty(value = "请求类型")
    private String method;

}
