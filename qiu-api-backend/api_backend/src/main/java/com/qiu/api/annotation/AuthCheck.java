package com.qiu.api.annotation;

import com.qiu.api.model.enums.UserRoleEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @description: 权限校验注解
 * @className: AuthCheck.java
 * @author: qiu
 * @createTime: 2024/3/17 10:30
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthCheck {

    /**
     * @description: 必须有某个角色
     * @params: []
     * @return: int
     * @author: qiu
     * @dateTime: 2024/3/17 11:29
     */
    UserRoleEnum mustRole() default UserRoleEnum.USER;
}
