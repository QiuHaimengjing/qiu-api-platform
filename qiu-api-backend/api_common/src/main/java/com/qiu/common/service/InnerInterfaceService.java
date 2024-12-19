package com.qiu.common.service;

import com.qiu.common.model.InvokeInterface;

/**
 * @description: 远程调用接口服务
 * @className: InnerInterfaceService.java
 * @author: qiu
 * @createTime: 2024/3/21 20:03
 */
public interface InnerInterfaceService {

    InvokeInterface getInvokeInterface(String host, String path, String method);

}
