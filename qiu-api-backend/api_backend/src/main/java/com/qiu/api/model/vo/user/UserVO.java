package com.qiu.api.model.vo.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @description: 脱敏的用户信息
 * @className: UserVO.java
 * @author: qiu
 * @createTime: 2023/12/9 15:50
 */
@Data
@ApiModel(value = "UserVO", description = "脱敏的用户信息")
public class UserVO implements Serializable {

    private static final long serialVersionUID = 3228473485689861354L;

    @ApiModelProperty(value = "用户id")
    private Long id;

    @ApiModelProperty(value = "账号")
    private String userAccount;

    @ApiModelProperty(value = "昵称")
    private String username;

    @ApiModelProperty(value = "头像")
    private String avatarUrl;

    @ApiModelProperty(value = "性别")
    private Integer gender;

    @ApiModelProperty(value = "电话")
    private String phone;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "个人简介")
    private String profile;

    @ApiModelProperty(value = "标签列表")
    private String tags;

    @ApiModelProperty(value = "角色 0-普通用户 1-管理员")
    private Integer userRole;

    @ApiModelProperty(value = "状态，0正常")
    private Integer userStatus;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

}
