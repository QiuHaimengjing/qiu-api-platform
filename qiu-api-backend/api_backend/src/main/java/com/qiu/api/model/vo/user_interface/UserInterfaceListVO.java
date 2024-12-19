package com.qiu.api.model.vo.user_interface;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @description: 脱敏的用户接口列表信息
 * @className: UserInterfaceListVO.java
 * @author: qiu
 * @createTime: 2024/3/12 13:25
 */
@Data
@ApiModel(value = "UserInterfaceListVO", description = "脱敏的用户接口列表信息")
public class UserInterfaceListVO implements Serializable {

    private static final long serialVersionUID = -6126235476418644268L;

    @ApiModelProperty(value = "脱敏的用户接口列表")
    private List<UserInterfaceVO> userInterfaceList;

    @ApiModelProperty(value = "总记录数")
    private Long total;
}
