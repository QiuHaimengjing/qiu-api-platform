package com.qiu.api.model.request.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @description: 用户注册请求体对象
 * @className: UserRegisterRequest.java
 * @author: qiu
 * @createTime: 2023/12/9 10:07
 */
@Data
@ApiModel(value = "UserRegisterRequest", description = "用户注册请求体对象")
public class UserRegisterRequest implements Serializable {

    private static final long serialVersionUID = 4999271778047191305L;

    @ApiModelProperty(value = "账号", required = true)
    private String userAccount;

    @ApiModelProperty(value = "密码", required = true)
    private String userPassword;

    @ApiModelProperty(value = "校验密码", required = true)
    private String verifyPassword;
}
