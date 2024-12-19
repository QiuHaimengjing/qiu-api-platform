package com.qiu.api.model.request.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @description: 用户登录请求体对象
 * @className: UserLoginRequest.java
 * @author: qiu
 * @createTime: 2023/12/9 16:33
 */
@Data
@ApiModel(value = "UserLoginRequest", description = "用户登录请求体对象")
public class UserLoginRequest implements Serializable {

    private static final long serialVersionUID = 3401305470779258625L;

    @ApiModelProperty(value = "账号", required = true)
    private String userAccount;

    @ApiModelProperty(value = "密码", required = true)
    private String userPassword;

}
