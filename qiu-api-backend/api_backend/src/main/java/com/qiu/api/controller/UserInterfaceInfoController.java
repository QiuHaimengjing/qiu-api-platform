package com.qiu.api.controller;


import cn.hutool.core.bean.BeanUtil;
import com.qiu.api.annotation.AuthCheck;
import com.qiu.api.common.BaseResponse;
import com.qiu.api.common.ErrorCode;
import com.qiu.api.common.Result;
import com.qiu.api.exception.BusinessException;
import com.qiu.api.model.request.user_interface.UserInterfaceAddRequest;
import com.qiu.api.model.request.user_interface.UserInterfaceQueryRequest;
import com.qiu.api.model.request.user_interface.UserInterfaceUpdateRequest;
import com.qiu.api.model.enums.UserRoleEnum;
import com.qiu.api.model.vo.StatisticVO;
import com.qiu.api.model.vo.user_interface.UserInterfaceListVO;
import com.qiu.api.model.vo.user_interface.UserInterfaceVO;
import com.qiu.api.service.IUserInterfaceInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 用户接口调用关系 前端控制器
 * </p>
 *
 * @author qiu
 * @since 2024-03-18
 */
@RestController
@RequestMapping("/user-interface")
@Api(tags = "UserInterface")
public class UserInterfaceInfoController {
    
    @Resource
    private IUserInterfaceInfoService userInterfaceInfoService;

    /**
     * @description: 创建用户接口调用关系
     * @params: [userInterfaceAddRequest, request]
     * @return: com.qiu.api.common.BaseResponse<java.lang.Long>
     * @author: qiu
     * @dateTime: 2024/3/18 20:44
     */
    @PostMapping("/")
    @ApiOperation(value = "创建用户接口调用关系", nickname = "addUserInterfaceService")
    public BaseResponse<Long> addUserInterface(@ApiParam(value = "userInterfaceAddRequest", required = true) 
                                                   @RequestBody UserInterfaceAddRequest userInterfaceAddRequest,
            HttpServletRequest request) {
        Long id = userInterfaceInfoService.addUserInterface(userInterfaceAddRequest, request);
        return Result.success(id, "创建用户接口调用关系成功");
    }

    /**
     * @description: 删除用户接口调用关系
     * @params: [id, request]
     * @return: com.qiu.api.common.BaseResponse<java.lang.Boolean>
     * @author: qiu
     * @dateTime: 2024/3/18 20:42
     */
    @DeleteMapping("/")
    @AuthCheck(mustRole = UserRoleEnum.ADMIN)
    @ApiOperation(value = "删除用户接口调用关系", nickname = "deleteUserInterfaceService")
    public BaseResponse<Boolean> deleteUserInterface(@ApiParam(value = "id", required = true) 
                                                         @RequestParam Long id, 
                                                     HttpServletRequest request) {
        Boolean result = userInterfaceInfoService.deleteUserInterface(id, request);
        return Result.success(result, "删除成功");
    }

    /**
     * @description: 修改用户接口调用关系
     * @params: [userInterfaceUpdateRequest, request]
     * @return: com.qiu.api.common.BaseResponse<java.lang.Boolean>
     * @author: qiu
     * @dateTime: 2024/3/18 20:44
     */
    @PutMapping("/")
    @AuthCheck(mustRole = UserRoleEnum.ADMIN)
    @ApiOperation(value = "修改用户接口调用关系", nickname = "updateUserInterfaceService")
    public BaseResponse<Boolean> updateUserInterface(@ApiParam(value = "userInterfaceUpdateRequest", required = true) 
                                                         @RequestBody UserInterfaceUpdateRequest userInterfaceUpdateRequest,
            HttpServletRequest request) {
        Boolean result = userInterfaceInfoService.updateUserInterface(userInterfaceUpdateRequest, request);
        return Result.success(result, "修改成功");
    }

    /**
     * @description: 查询用户接口调用关系列表
     * @params: [userInterfaceQueryRequest, request]
     * @return: com.qiu.api.common.BaseResponse<com.qiu.api.model.vo.user_interface.UserInterfaceListVO>
     * @author: qiu
     * @dateTime: 2024/3/18 20:51
     */
    @PostMapping("/search")
    @AuthCheck(mustRole = UserRoleEnum.ADMIN)
    @ApiOperation(value = "查询用户接口调用关系列表", nickname = "getUserInterfaceListService")
    public BaseResponse<UserInterfaceListVO> getUserInterfaceList(@ApiParam(value = "userInterfaceQueryRequest", required = true) 
                                                                   @RequestBody UserInterfaceQueryRequest userInterfaceQueryRequest,
            HttpServletRequest request) {
        UserInterfaceListVO userInterfaceListVO = userInterfaceInfoService.getUserInterfaceList(userInterfaceQueryRequest, request);
        return Result.success(userInterfaceListVO, "查询成功");
    }

    /**
     * @description: 统计分析：接口调用次数排名（降序）
     * @params: [limit]
     * @return: com.qiu.api.common.BaseResponse<java.util.List<com.qiu.api.model.vo.StatisticVO>>
     * @author: qiu
     * @dateTime: 2024/3/22 15:25
     */
    @GetMapping("/statistic")
    @AuthCheck(mustRole = UserRoleEnum.ADMIN)
    @ApiOperation(value = "统计分析：接口调用次数排名（降序）", nickname = "interfaceStatisticService")
    public BaseResponse<List<StatisticVO>> interfaceStatistic(@ApiParam(value = "limit", required = true) 
                                                                  @RequestParam Long limit) {
        if (BeanUtil.isEmpty(limit) || limit <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        List<StatisticVO> statisticVOS = userInterfaceInfoService.interfaceStatistic(limit);
        return Result.success(statisticVOS);
    }

    /**
     * @description: 根据id查询用户接口关系
     * @params: [id, request]
     * @return: com.qiu.api.common.BaseResponse<com.qiu.api.model.vo.user_interface.UserInterfaceVO>
     * @author: qiu
     * @dateTime: 2024/3/23 14:26
     */
    @GetMapping("/")
    @ApiOperation(value = "根据id查询用户接口关系", nickname = "getUserInterfaceByIdService")
    public BaseResponse<UserInterfaceVO> getUserInterfaceById(@ApiParam(value = "id", required = true) 
                                                                  @RequestParam Long id, 
                                                              HttpServletRequest request) {
        UserInterfaceVO userInterfaceInfo = userInterfaceInfoService.getUserInterfaceById(id, request);
        return Result.success(userInterfaceInfo);
    }

}
