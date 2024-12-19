package com.qiu.common.service;

/**
 * @description: 远程调用用户接口关系服务
 * @className: InnerUserInterfaceService.java
 * @author: qiu
 * @createTime: 2024/3/21 20:49
 */
public interface InnerUserInterfaceService {

    Boolean invokeCount(Long interfaceId, Long userId);

}
