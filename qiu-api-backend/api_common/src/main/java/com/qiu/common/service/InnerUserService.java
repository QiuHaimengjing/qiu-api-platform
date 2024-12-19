package com.qiu.common.service;

import com.qiu.common.model.InvokeUser;

/**
 * @description: 远程调用用户服务
 * @className: InnerUserServiceImpl.java
 * @author: qiu
 * @createTime: 2024/3/21 18:57
 */
public interface InnerUserService {

    InvokeUser getInvokeUser(String accessKey);

}
