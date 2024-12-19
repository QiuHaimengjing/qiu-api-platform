package com.qiu.api.model.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author qiu
 * @since 2023-12-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("user")
@ApiModel(value="User", description="用户信息")
public class User implements Serializable {

    private static final long serialVersionUID = 4711750271961596479L;

    @ApiModelProperty(value = "用户id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "账号")
    private String userAccount;

    @ApiModelProperty(value = "昵称")
    private String username;

    @ApiModelProperty(value = "头像")
    private String avatarUrl;

    @ApiModelProperty(value = "性别")
    private Integer gender;

    @ApiModelProperty(value = "密码")
    private String userPassword;

    @ApiModelProperty(value = "加密盐")
    private String salt;

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

    @ApiModelProperty(value = "用户唯一标识")
    private String accessKey;

    @ApiModelProperty(value = "用户密钥")
    private String secretKey;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "是否删除 0 1（默认值0）")
    private Integer deleted;


}
