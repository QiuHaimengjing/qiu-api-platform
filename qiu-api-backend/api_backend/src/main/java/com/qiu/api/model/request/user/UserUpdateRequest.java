package com.qiu.api.model.request.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @description: 用户更新信息请求体对象
 * @className: UserUpdateRequest.java
 * @author: qiu
 * @createTime: 2024/2/23 22:21
 */
@Data
@ApiModel(value = "UserUpdateRequest", description = "用户更新信息请求体对象")
public class UserUpdateRequest implements Serializable {

    private static final long serialVersionUID = -2538383941421620984L;

    @ApiModelProperty(value = "用户id", required = true)
    private Long id;

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

}
