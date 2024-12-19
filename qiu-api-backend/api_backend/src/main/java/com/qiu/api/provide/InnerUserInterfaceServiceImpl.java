package com.qiu.api.provide;

import com.qiu.api.service.IUserInterfaceInfoService;
import com.qiu.common.service.InnerUserInterfaceService;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

/**
 * @description: 远程调用用户接口关系服务
 * @className: InnerUserInterfaceServiceImpl.java
 * @author: qiu
 * @createTime: 2024/3/21 21:27
 */
@DubboService
public class InnerUserInterfaceServiceImpl implements InnerUserInterfaceService {

    @Resource
    private IUserInterfaceInfoService userInterfaceInfoService;

    /**
     * @description: 网关处理用户接口关系统计服务
     * @params: [interfaceId, userId]
     * @return: java.lang.Boolean
     * @author: qiu
     * @dateTime: 2024/3/22 23:08
     */
    @Override
    public Boolean invokeCount(Long interfaceId, Long userId) {
        return userInterfaceInfoService.invokeCount(interfaceId, userId);
    }
}
