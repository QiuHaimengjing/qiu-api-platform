package com.qiu.api.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @description: 分页参数请求体对象
 * @className: PageRequest.java
 * @author: qiu
 * @createTime: 2024/3/2 13:24
 */
@Data
@ApiModel(value = "PageRequest", description = "分页参数请求体对象")
public class PageRequest implements Serializable {

    private static final long serialVersionUID = -4110763895693248080L;

    @ApiModelProperty(value = "页码", required = true)
    private Integer pageNO;

    @ApiModelProperty(value = "每页记录数", required = true)
    private Integer pageSize;
}
