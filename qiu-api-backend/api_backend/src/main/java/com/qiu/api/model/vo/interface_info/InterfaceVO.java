package com.qiu.api.model.vo.interface_info;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @description: 脱敏的接口信息
 * @className: InterfaceVO.java
 * @author: qiu
 * @createTime: 2024/3/12 13:25
 */
@Data
@ApiModel(value = "InterfaceVO", description = "脱敏的接口信息")
public class InterfaceVO implements Serializable {

    private static final long serialVersionUID = -757488349460173014L;

    @ApiModelProperty(value = "接口id")
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

    @ApiModelProperty(value = "创建人")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

}
