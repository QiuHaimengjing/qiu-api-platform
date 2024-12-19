package com.qiu.api.model.request.user_interface;

import com.qiu.api.model.request.PageRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @description: 查询用户接口调用关系请求体对象
 * @className: UserInterfaceQueryRequest.java
 * @author: qiu
 * @createTime: 2024/3/18 20:45
 */
@Data
@ApiModel(value = "UserInterfaceQueryRequest", description = "查询用户接口调用关系请求体对象")
@EqualsAndHashCode(callSuper = true)
public class UserInterfaceQueryRequest extends PageRequest {

    private static final long serialVersionUID = 5637795046550650725L;

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "接口id")
    private Long interfaceId;

    @ApiModelProperty(value = "调用状态 （0-正常，1-禁用）")
    private Integer status;

}
