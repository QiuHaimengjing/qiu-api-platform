package com.qiu.api.common;

/**
 * @description: Result工具类
 * @className: ResultUtils.java
 * @author: qiu
 * @createTime: 2023/12/16 18:25
 */
public class Result {

    /**
     * @description: 成功
     * @params: [data, description]
     * @return: com.qiu.api.common.BaseResponse<T>
     * @author: qiu
     * @dateTime: 2023/12/16 19:07
     */
    public static <T> BaseResponse<T> success(T data, String description) {
        return new BaseResponse<>(20000, "ok", description, data);
    }

    /**
     * @description: 成功
     * @params: [data]
     * @return: com.qiu.api.common.BaseResponse<T>
     * @author: qiu
     * @dateTime: 2023/12/16 19:07
     */
    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>(20000, "ok", data);
    }

    /**
     * @description: 错误
     * @params: [errorCode]
     * @return: com.qiu.api.common.BaseResponse<T>
     * @author: qiu
     * @dateTime: 2023/12/16 19:08
     */
    public static <T> BaseResponse<T> error(ErrorCode errorCode) {
        return new BaseResponse<>(errorCode);
    }

    /**
     * @description: 错误
     * @params: [code, message, description]
     * @return: com.qiu.api.common.BaseResponse<T>
     * @author: qiu
     * @dateTime: 2023/12/16 19:11
     */
    public static <T> BaseResponse<T> error(Integer code, String message, String description) {
        return new BaseResponse<>(code, message, description);
    }
}
