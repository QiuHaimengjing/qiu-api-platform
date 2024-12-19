package com.qiu.apiclientsdk.model;

import java.util.Arrays;

/**
 * @description: 接口响应状态码
 * @className: ErrorCode.java
 * @author: qiu
 * @createTime: 2024/3/23 11:31
 */
public enum ResponseStatus {

    OK(200, "ok"),
    BAD_REQUEST(400, "错误的请求"),
    FORBIDDEN(403, "禁止调用"),
    SYSTEM_ERROR(500, "系统内部异常")
    ;

    private final Integer status;
    private final String description;

    ResponseStatus(Integer status, String description) {
        this.status = status;
        this.description = description;
    }

    /**
     * @description: 根据响应码获取枚举值
     * @params: []
     * @return: java.lang.String
     * @author: qiu
     * @dateTime: 2024/3/23 11:58
     */
    public static ResponseStatus getStatusByCode(Integer status) {
        return Arrays.stream(ResponseStatus.values())
                .filter(item -> item.getStatus().equals(status))
                .findFirst()
                .orElse(SYSTEM_ERROR);
    }

    public Integer getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }

}
