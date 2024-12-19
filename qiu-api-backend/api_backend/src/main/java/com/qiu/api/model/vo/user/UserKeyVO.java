package com.qiu.api.model.vo.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @description: ak、sk对象
 * @className: UserKeyVO.java
 * @author: qiu
 * @createTime: 2024/3/22 14:02
 */
@Data
@ApiModel(value = "UserKeyVO", description = "ak、sk对象")
public class UserKeyVO implements Serializable {

    private static final long serialVersionUID = 1862842439219572928L;

    @ApiModelProperty(value = "用户id")
    private Long id;

    @ApiModelProperty(value = "用户唯一标识")
    private String accessKey;

    @ApiModelProperty(value = "用户密钥")
    private String secretKey;

}
