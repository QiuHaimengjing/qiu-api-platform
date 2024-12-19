package com.qiu.api.model.request.user_interface;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @description: 更新用户接口调用关系请求体对象
 * @className: UserInterfaceUpdateRequest.java
 * @author: qiu
 * @createTime: 2024/3/18 20:32
 */
@Data
@ApiModel(value = "UserInterfaceUpdateRequest", description = "更新用户接口调用关系请求体对象")
public class UserInterfaceUpdateRequest implements Serializable {

    private static final long serialVersionUID = 2239089894902391996L;

    @ApiModelProperty(value = "id", required = true)
    private Long id;

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "接口id")
    private Long interfaceId;

    @ApiModelProperty(value = "总调用次数")
    private Integer totalNum;

    @ApiModelProperty(value = "剩余调用次数")
    private Integer leftNum;

    @ApiModelProperty(value = "调用状态 （0-正常，1-禁用）")
    private Integer status;

}
