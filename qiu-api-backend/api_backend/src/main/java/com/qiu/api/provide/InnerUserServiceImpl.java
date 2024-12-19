package com.qiu.api.provide;

import com.qiu.api.service.IUserService;
import com.qiu.common.model.InvokeUser;
import com.qiu.common.service.InnerUserService;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

/**
 * @description: 远程调用用户服务
 * @className: InnerUserServiceImpl.java
 * @author: qiu
 * @createTime: 2024/3/21 18:54
 */
@DubboService
public class InnerUserServiceImpl implements InnerUserService {

    @Resource
    private IUserService userService;

    /**
     * @description: 网关校验用户权限
     * @params: [accessKey]
     * @return: com.qiu.common.model.InvokeUser
     * @author: qiu
     * @dateTime: 2024/3/22 23:09
     */
    @Override
    public InvokeUser getInvokeUser(String accessKey) {
        return userService.getInvokeUser(accessKey);
    }
}
