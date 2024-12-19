package com.qiu.api.model.vo.interface_info;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @description: 脱敏的接口列表信息
 * @className: InterfaceListVO.java
 * @author: qiu
 * @createTime: 2024/3/12 13:25
 */
@Data
@ApiModel(value = "InterfaceListVO", description = "脱敏的接口列表信息")
public class InterfaceListVO implements Serializable {

    private static final long serialVersionUID = -7989929293404507970L;

    @ApiModelProperty(value = "脱敏的接口列表")
    private List<InterfaceVO> interfaceList;

    @ApiModelProperty(value = "总记录数")
    private Long total;
}
