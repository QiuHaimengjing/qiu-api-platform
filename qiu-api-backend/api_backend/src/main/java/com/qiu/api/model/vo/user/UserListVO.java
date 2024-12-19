package com.qiu.api.model.vo.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @description: 脱敏的用户列表信息
 * @className: UserListVO.java
 * @author: qiu
 * @createTime: 2023/12/9 15:50
 */
@Data
@ApiModel(value = "UserListVO", description = "脱敏的用户列表信息")
public class UserListVO implements Serializable {

    private static final long serialVersionUID = 882679522381029516L;

    @ApiModelProperty(value = "脱敏的用户列表")
    private List<UserVO> userList;

    @ApiModelProperty(value = "总记录数")
    private Long total;

}
