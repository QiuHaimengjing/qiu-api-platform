package com.qiu.api.model.request.interface_info;

import com.qiu.api.model.request.PageRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @description: 接口查询信息请求体对象
 * @className: InterfaceQueryRequest.java
 * @author: qiu
 * @createTime: 2024/3/12 13:29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "InterfaceQueryRequest", description = "接口查询信息请求体对象")
public class InterfaceQueryRequest extends PageRequest {

    private static final long serialVersionUID = -7320086634330130553L;

    @ApiModelProperty(value = "接口id")
    private Long id;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "服务器地址")
    private String host;

    @ApiModelProperty(value = "接口地址")
    private String url;

    @ApiModelProperty(value = "接口状态 （0-关闭，1-开启）")
    private Integer status;

}
