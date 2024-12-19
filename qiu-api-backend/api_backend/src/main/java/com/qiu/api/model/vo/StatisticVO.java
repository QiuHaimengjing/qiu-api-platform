package com.qiu.api.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @description: 统计分析包装类
 * @className: StatisticsVO.java
 * @author: qiu
 * @createTime: 2024/3/22 14:56
 */
@Data
@ApiModel(value = "StatisticsVO", description = "统计分析包装类")
public class StatisticVO implements Serializable {

    private static final long serialVersionUID = -5679219866212664079L;

    @ApiModelProperty(value = "接口id")
    private Long id;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "调用次数")
    private String totalNum;
}
