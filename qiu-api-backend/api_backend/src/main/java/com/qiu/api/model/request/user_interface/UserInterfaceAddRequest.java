package com.qiu.api.model.request.user_interface;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @description: 创建用户接口调用关系请求体对象
 * @className: UserInterfaceAddRequest.java
 * @author: qiu
 * @createTime: 2024/3/18 20:16
 *
 */
@Data
@ApiModel(value = "UserInterfaceAddRequest", description = "创建用户接口调用关系请求体对象")
public class UserInterfaceAddRequest implements Serializable {

    private static final long serialVersionUID = -3443495553214027985L;

    @ApiModelProperty(value = "接口id", required = true)
    private Long interfaceId;

    @ApiModelProperty(value = "用户id", required = true)
    private Long userId;

}
