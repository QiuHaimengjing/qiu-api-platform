package com.qiu.api.model.vo.user_interface;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @description: 脱敏的用户接口关系信息对象
 * @className: User_InterfaceVO.java
 * @author: qiu
 * @createTime: 2024/3/18 20:15
 */
@Data
@ApiModel(value = "UserInterfaceVO", description = "脱敏的用户接口关系信息对象")
public class UserInterfaceVO implements Serializable {

    private static final long serialVersionUID = -6141642664489033953L;

    @ApiModelProperty(value = "id")
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

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

}
