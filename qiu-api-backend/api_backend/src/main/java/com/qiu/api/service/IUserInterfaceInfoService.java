package com.qiu.api.service;

import com.qiu.api.common.BaseResponse;
import com.qiu.api.model.request.user_interface.UserInterfaceAddRequest;
import com.qiu.api.model.request.user_interface.UserInterfaceQueryRequest;
import com.qiu.api.model.request.user_interface.UserInterfaceUpdateRequest;
import com.qiu.api.model.po.UserInterfaceInfo;
import com.qiu.api.model.vo.StatisticVO;
import com.qiu.api.model.vo.user_interface.UserInterfaceListVO;
import com.qiu.api.model.vo.user_interface.UserInterfaceVO;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 用户接口调用关系 服务类
 * </p>
 *
 * @author qiu
 * @since 2024-03-18
 */
public interface IUserInterfaceInfoService extends IService<UserInterfaceInfo> {

    Long addUserInterface(UserInterfaceAddRequest userInterfaceAddRequest, HttpServletRequest request);

    Boolean deleteUserInterface(Long id, HttpServletRequest request);

    Boolean updateUserInterface(UserInterfaceUpdateRequest userInterfaceUpdateRequest, HttpServletRequest request);

    UserInterfaceListVO getUserInterfaceList(UserInterfaceQueryRequest userInterfaceQueryRequest, HttpServletRequest request);

    List<StatisticVO> interfaceStatistic(Long limit);

    UserInterfaceVO getUserInterfaceById(Long id, HttpServletRequest request);

    Boolean invokeCount(Long interfaceId, Long userId);

}
