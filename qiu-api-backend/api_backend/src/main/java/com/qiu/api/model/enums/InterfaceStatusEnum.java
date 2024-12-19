package com.qiu.api.model.enums;

import lombok.Getter;

/**
 * @description: 接口状态枚举
 * @className: InterfaceStatus.java
 * @author: qiu
 * @createTime: 2024/3/17 11:46
 */
@Getter
public enum InterfaceStatusEnum {

    OFFLINE(0, "关闭"),
    ONLINE(1, "开启");

    private final Integer status;
    private final String description;

    InterfaceStatusEnum(Integer status, String description) {
        this.status = status;
        this.description = description;
    }

}
