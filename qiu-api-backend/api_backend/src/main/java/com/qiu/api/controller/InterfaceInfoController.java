package com.qiu.api.controller;


import com.qiu.api.common.BaseResponse;
import com.qiu.api.common.Result;
import com.qiu.api.model.request.interface_info.InterfaceAddRequest;
import com.qiu.api.model.request.interface_info.InterfaceQueryRequest;
import com.qiu.api.model.request.interface_info.InterfaceUpdateRequest;
import com.qiu.api.model.enums.UserRoleEnum;
import com.qiu.api.model.request.InterfaceInvokeRequest;
import com.qiu.api.model.vo.interface_info.InterfaceListVO;
import com.qiu.api.model.vo.interface_info.InterfaceVO;
import com.qiu.api.service.IInterfaceInfoService;
import com.qiu.api.annotation.AuthCheck;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 接口信息 前端控制器
 * </p>
 *
 * @author qiu
 * @since 2024-03-12
 */
@RestController
@RequestMapping("/interface")
@Api(tags = "InterfaceInfo")
public class InterfaceInfoController {

    @Resource
    private IInterfaceInfoService interfaceInfoService;

    /**
     * @description: 创建接口
     * @params: [interfaceAddRequest, request]
     * @return: com.qiu.api.common.BaseResponse<java.lang.Long>
     * @author: qiu
     * @dateTime: 2024/3/12 13:50
     */
    @PostMapping("/")
    @AuthCheck(mustRole = UserRoleEnum.ADMIN)
    @ApiOperation(value = "创建接口", nickname = "addInterfaceService")
    public BaseResponse<Long> addInterface(@ApiParam(value = "interfaceAddRequest", required = true)
                                               @RequestBody InterfaceAddRequest interfaceAddRequest, 
                                           HttpServletRequest request) {
        Long id = interfaceInfoService.addInterface(interfaceAddRequest, request);
        return Result.success(id, "创建接口成功");
    }

    /**
     * @description: 删除接口
     * @params: [idList, request]
     * @return: com.qiu.apibackend.common.BaseResponse<java.lang.Boolean>
     * @author: qiu
     * @dateTime: 2024/3/23 16:46
     */
    @DeleteMapping("/items")
    @AuthCheck(mustRole = UserRoleEnum.ADMIN)
    @ApiOperation(value = "删除接口", nickname = "deleteInterfaceService")
    public BaseResponse<Boolean> deleteInterface(@ApiParam(value = "idList", required = true)
                                                     @RequestBody List<Long> idList, 
                                                 HttpServletRequest request) {
        Boolean result = interfaceInfoService.deleteInterface(idList, request);
        return Result.success(result, "删除成功");
    }

    /**
     * @description: 修改接口
     * @params: [interfaceUpdateRequest, request]
     * @return: com.qiu.api.common.BaseResponse<java.lang.Boolean>
     * @author: qiu
     * @dateTime: 2024/3/12 13:50
     */
    @PutMapping("/")
    @AuthCheck(mustRole = UserRoleEnum.ADMIN)
    @ApiOperation(value = "修改接口", nickname = "updateInterfaceService")
    public BaseResponse<Boolean> updateInterface(@ApiParam(value = "interfaceUpdateRequest", required = true) 
                                                     @RequestBody InterfaceUpdateRequest interfaceUpdateRequest, 
                                                 HttpServletRequest request) {
        Boolean result = interfaceInfoService.updateInterface(interfaceUpdateRequest, request);
        return Result.success(result, "修改成功");
    }

    /**
     * @description: 查询接口列表
     * @params: [interfaceQueryRequest, request]
     * @return: com.qiu.api.common.BaseResponse<com.qiu.api.model.vo.interface_info.InterfaceListVO>
     * @author: qiu
     * @dateTime: 2024/3/12 13:50
     */
    @PostMapping("/search")
    @ApiOperation(value = "查询接口列表", nickname = "getInterfaceListService")
    public BaseResponse<InterfaceListVO> getInterfaceList(@ApiParam(value = "interfaceQueryRequest", required = true)
                                                           @RequestBody InterfaceQueryRequest interfaceQueryRequest,
                                                       HttpServletRequest request) {
        InterfaceListVO interfaceListVO = interfaceInfoService.interfaceList(interfaceQueryRequest, request);
        return Result.success(interfaceListVO, "查询成功");
    }

    /**
     * @description: 根据id查询接口
     * @params: [id]
     * @return: com.qiu.api.common.BaseResponse<com.qiu.api.model.vo.interface_info.InterfaceVO>
     * @author: qiu
     * @dateTime: 2024/3/17 16:17
     */
    @GetMapping("/")
    @ApiOperation(value = "根据id查询接口", nickname = "getInterfaceByIdService")
    public BaseResponse<InterfaceVO> getInterfaceById(@ApiParam(value = "id", required = true)
                                                            @RequestParam Long id) {
        InterfaceVO interfaceVO = interfaceInfoService.queryInterfaceById(id);
        return Result.success(interfaceVO);
    }

    /**
     * @description: 上线接口
     * @params: [id, request]
     * @return: com.qiu.api.common.BaseResponse<java.lang.Boolean>
     * @author: qiu
     * @dateTime: 2024/3/17 13:00
     */
    @PostMapping("/online")
    @AuthCheck(mustRole = UserRoleEnum.ADMIN)
    @ApiOperation(value = "上线接口", nickname = "onlineInterfaceService")
    public BaseResponse<Boolean> onlineInterface(@ApiParam(value = "id", required = true)
                                                     @RequestParam Long id, HttpServletRequest request) {
        Boolean result = interfaceInfoService.onlineInterface(id, request);
        return Result.success(result);
    }

    /**
     * @description: 下线接口
     * @params: [id]
     * @return: com.qiu.api.common.BaseResponse<java.lang.Boolean>
     * @author: qiu
     * @dateTime: 2024/3/17 13:00
     */
    @PostMapping("/offline")
    @AuthCheck(mustRole = UserRoleEnum.ADMIN)
    @ApiOperation(value = "下线接口", nickname = "offlineInterfaceService")
    public BaseResponse<Boolean> offlineInterface(@ApiParam(value = "id", required = true)
                                                      @RequestParam Long id) {
        Boolean result = interfaceInfoService.offlineInterface(id);
        return Result.success(result);
    }

    /**
     * @description: 调用接口
     * @params: [invokeRequest, request]
     * @return: com.qiu.api.common.BaseResponse<java.lang.Object>
     * @author: qiu
     * @dateTime: 2024/3/17 19:53
     */
    @PostMapping("/invoke")
    @ApiOperation(value = "调用接口", nickname = "invokeInterfaceService")
    public BaseResponse<Object> invokeInterface(@ApiParam(value = "invokeRequest", required = true)
                                                    @RequestBody InterfaceInvokeRequest invokeRequest,
                                                HttpServletRequest request) {
        Object result = interfaceInfoService.invokeInterface(invokeRequest, request);
        return Result.success(result);
    }

}
