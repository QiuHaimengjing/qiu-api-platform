package com.qiu.api.provide;

import com.qiu.api.service.IInterfaceInfoService;
import com.qiu.common.model.InvokeInterface;
import com.qiu.common.service.InnerInterfaceService;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

/**
 * @description: 远程调用接口服务
 * @className: InnerInterfaceServiceImpl.java
 * @author: qiu
 * @createTime: 2024/3/21 20:11
 */
@DubboService
public class InnerInterfaceServiceImpl implements InnerInterfaceService {

    @Resource
    private IInterfaceInfoService interfaceInfoService;

    /**
     * @description: 网关校验接口信息
     * @params: [host, path, method]
     * @return: com.qiu.common.model.InvokeInterface
     * @author: qiu
     * @dateTime: 2024/3/22 23:08
     */
    @Override
    public InvokeInterface getInvokeInterface(String host, String path, String method) {
        return interfaceInfoService.getInvokeInterface(host, path, method);
    }
}
