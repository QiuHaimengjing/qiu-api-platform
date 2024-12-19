package com.qiu.api.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @description: 通用返回对象
 * @className: BaseResponse.java
 * @author: qiu
 * @createTime: 2023/12/16 15:55
 */
@Data
@ApiModel(value = "BaseResponse", description = "通用返回对象")
public class BaseResponse<T> implements Serializable {

    private static final long serialVersionUID = 6541485659560619874L;

    @ApiModelProperty(value = "响应码", required = true)
    private Integer code;

    @ApiModelProperty(value = "响应消息", required = true)
    private String message;

    @ApiModelProperty(value = "响应描述", required = true)
    private String description;

    @ApiModelProperty(value = "响应数据", required = true)
    private T data;

    public BaseResponse(Integer code, String message, String description, T data) {
        this.code = code;
        this.message = message;
        this.description = description;
        this.data = data;
    }

    public BaseResponse(Integer code, String message, String description) {
        this.code = code;
        this.message = message;
        this.description = description;
        this.data = null;
    }

    public BaseResponse(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.description = "";
        this.data = data;
    }

    public BaseResponse(Integer code, String message) {
        this.code = code;
        this.message = message;
        this.description = "";
        this.data = null;
    }

    public BaseResponse(ErrorCode errorCode) {
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
        this.description = errorCode.getMessage();
        this.data = null;
    }
}
