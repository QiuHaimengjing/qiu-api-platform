package com.qiu.api.service;

import com.qiu.api.model.request.interface_info.InterfaceAddRequest;
import com.qiu.api.model.request.interface_info.InterfaceQueryRequest;
import com.qiu.api.model.request.interface_info.InterfaceUpdateRequest;
import com.qiu.api.model.po.InterfaceInfo;
import com.qiu.api.model.request.InterfaceInvokeRequest;
import com.qiu.api.model.vo.interface_info.InterfaceListVO;
import com.qiu.api.model.vo.interface_info.InterfaceVO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.qiu.common.model.InvokeInterface;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 接口信息 服务类
 * </p>
 *
 * @author qiu
 * @since 2024-03-12
 */
public interface IInterfaceInfoService extends IService<InterfaceInfo> {

    Long addInterface(InterfaceAddRequest interfaceAddRequest, HttpServletRequest request);

    Boolean deleteInterface(List<Long> idList, HttpServletRequest request);

    Boolean updateInterface(InterfaceUpdateRequest interfaceUpdateRequest, HttpServletRequest request);

    InterfaceListVO interfaceList(InterfaceQueryRequest interfaceQueryRequest, HttpServletRequest request);

    InterfaceVO queryInterfaceById(Long id);

    Boolean onlineInterface(Long id, HttpServletRequest request);

    Boolean offlineInterface(Long id);

    Object invokeInterface(InterfaceInvokeRequest invokeRequest, HttpServletRequest request);

    InvokeInterface getInvokeInterface(String host, String path, String method);

}
