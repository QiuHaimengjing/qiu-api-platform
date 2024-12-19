package com.qiu.apiclientsdk.model.firstBatch;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @description: 点名接口请求参数
 * @className: rollCallRequest.java
 * @author: qiu
 * @createTime: 2024/3/28 10:44
 */
@Data
@ApiModel(value = "点名接口请求参数")
public class RollCallRequest implements Serializable {

    private static final long serialVersionUID = 4985333954392102818L;

    @ApiModelProperty(value = "参与人列表")
    private List<String> participants;

    @ApiModelProperty(value = "抽选人数")
    private Integer number;
}
