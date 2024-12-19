package com.qiu.api.controller;


import cn.hutool.core.bean.BeanUtil;
import com.qiu.api.annotation.AuthCheck;
import com.qiu.api.common.BaseResponse;
import com.qiu.api.common.ErrorCode;
import com.qiu.api.common.Result;
import com.qiu.api.constant.UserConstant;
import com.qiu.api.exception.BusinessException;
import com.qiu.api.model.enums.UserRoleEnum;
import com.qiu.api.model.request.user.UserLoginRequest;
import com.qiu.api.model.request.user.UserRegisterRequest;
import com.qiu.api.model.request.user.UserUpdateRequest;
import com.qiu.api.model.po.User;
import com.qiu.api.model.vo.user.UserKeyVO;
import com.qiu.api.model.dto.UserSessionDTO;
import com.qiu.api.model.vo.user.UserListVO;
import com.qiu.api.model.vo.user.UserVO;
import com.qiu.api.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author qiu
 * @since 2023-12-09
 */
@RestController
@RequestMapping("/user")
@Api(tags = "User")
public class UserController {

    @Resource
    private IUserService userService;

    /**
     * @description: 用户注册
     * @params: [userRegisterRequest]
     * @return: com.qiu.api.common.BaseResponse<java.lang.Boolean>
     * @author: qiu
     * @dateTime: 2023/12/16 19:33
     */
    @PostMapping("/")
    @ApiOperation(value = "用户注册", nickname = "userRegisterService")
    public BaseResponse<Boolean> userRegister(@ApiParam(value = "userRegisterRequest", required = true)
                                                  @RequestBody UserRegisterRequest userRegisterRequest) {
        if (BeanUtil.isEmpty(userRegisterRequest)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        Boolean result = userService.userRegister(userRegisterRequest);
        return Result.success(result, "注册成功");
    }

    /**
     * @description: 用户登录
     * @params: [userLoginRequest, request]
     * @return: com.qiu.api.common.BaseResponse<com.qiu.apibackend.model.vo.user.UserVO>
     * @author: qiu
     * @dateTime: 2023/12/16 19:46
     */
    @PostMapping("/session")
    @ApiOperation(value = "用户登录", nickname = "userLoginService")
    public BaseResponse<UserVO> userLogin(@ApiParam(value = "userLoginRequest", required = true)
                                              @RequestBody UserLoginRequest userLoginRequest,
                                          HttpServletRequest request) {
        if (BeanUtil.isEmpty(userLoginRequest)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        UserVO userVO = userService.userLogin(userLoginRequest, request);
        return Result.success(userVO, "登录成功");
    }

    /**
     * @description: 获取用户登录态
     * @params: [request]
     * @return: com.qiu.api.common.BaseResponse<com.qiu.api.model.vo.UserVO>
     * @author: qiu
     * @dateTime: 2023/12/16 19:50
     */
    @GetMapping("/me")
    @ApiOperation(value = "获取用户登录态", nickname = "userGetCurrentService")
    public BaseResponse<UserVO> userCurrent(HttpServletRequest request) {
        // 检验是否登录
        UserSessionDTO userSessionVO = userService.getLoginUser(request);
        if (BeanUtil.isEmpty(userSessionVO)) {
            throw new BusinessException(ErrorCode.NOT_LOGIN, "用户未登录");
        }
        // 查库检验
        User user = userService.getById(userSessionVO.getId());
        if (BeanUtil.isEmpty(user)) {
            throw new BusinessException(ErrorCode.NULL_ERROR, "用户不存在");
        }
        UserVO userInfo = BeanUtil.copyProperties(user, UserVO.class);
        return Result.success(userInfo);
    }

    /**
     * @description: 查询用户列表
     * @params: [username]
     * @return: com.qiu.api.common.BaseResponse<com.qiu.api.model.vo.user.UserListVO>
     * @author: qiu
     * @dateTime: 2023/12/16 19:55
     */
    @PostMapping("/search")
    @AuthCheck(mustRole = UserRoleEnum.ADMIN)
    @ApiOperation(value = "查询用户列表", nickname = "getUserListService")
    public BaseResponse<UserListVO> getUserList(@ApiParam(value = "username")
                                                      @RequestParam(value = "username", required = false)
                                                      String username) {
        UserListVO userListVO = userService.searchUsers(username);
        return Result.success(userListVO, "查询用户成功");
    }

    /**
     * @description: 删除用户
     * @params: [id, request]
     * @return: com.qiu.api.common.BaseResponse<java.lang.Boolean>
     * @author: qiu
     * @dateTime: 2023/12/16 19:57
     */
    @DeleteMapping("/")
    @AuthCheck(mustRole = UserRoleEnum.ADMIN)
    @ApiOperation(value = "删除用户", nickname = "deleteUserService")
    public BaseResponse<Boolean> deleteUser(@ApiParam(value = "id", required = true)
                                                @RequestParam("id") Long id) {
        boolean isDeleted = userService.removeById(id);
        if (!isDeleted) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "删除失败");
        }
        return Result.success(true, "删除成功");
    }

    /**
     * @description: 用户注销
     * @params: [request]
     * @return: com.qiu.api.common.BaseResponse<java.lang.Boolean>
     * @author: qiu
     * @dateTime: 2023/12/16 19:58
     */
    @DeleteMapping("/session")
    @ApiOperation(value = "用户注销", nickname = "userLogoutService")
    public BaseResponse<Boolean> userLogout(HttpServletRequest request) {
        UserSessionDTO loginUser = userService.getLoginUser(request);
        if (BeanUtil.isEmpty(loginUser)) {
            throw new BusinessException(ErrorCode.NOT_LOGIN, "用户未登录");
        }
        request.getSession().removeAttribute(UserConstant.USER_LOGIN_STATE);
        return Result.success(true, "注销成功");
    }

    /**
     * @description: 管理员更新用户信息
     * @params: [user]
     * @return: com.qiu.api.common.BaseResponse<java.lang.Boolean>
     * @author: qiu
     * @dateTime: 2023/12/21 10:11
     */
    @PutMapping("/admin")
    @AuthCheck(mustRole = UserRoleEnum.ADMIN)
    @ApiOperation(value = "管理员更新用户信息", nickname = "updateUserByAdminService")
    public BaseResponse<Boolean> updateUserByAdmin(@ApiParam(value = "user", required = true)
                                                    @RequestBody User user) {
        if (BeanUtil.isEmpty(user)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户对象为空");
        }
        Boolean result = userService.updateUserByAdmin(user);
        return Result.success(result, "更新成功");
    }

    /**
     * @description: 用户更新个人信息
     * @params: [userUpdateRequest, request]
     * @return: com.qiu.api.common.BaseResponse<java.lang.Boolean>
     * @author: qiu
     * @dateTime: 2024/2/23 22:46
     */
    @PutMapping("/")
    @ApiOperation(value = "用户更新个人信息", nickname = "updateUserService")
    public BaseResponse<Boolean> updateUser(@ApiParam(value = "userUpdateRequest", required = true) 
                                                @RequestBody UserUpdateRequest userUpdateRequest,
                                            HttpServletRequest request) {
        if (BeanUtil.isEmpty(userUpdateRequest)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户对象为空");
        }
        Boolean result = userService.updateUser(userUpdateRequest, request);
        return Result.success(result, "更新成功");
    }

    /**
     * @description: 生成密钥认证
     * @params: [request]
     * @return: com.qiu.api.common.BaseResponse<java.lang.Boolean>
     * @author: qiu
     * @dateTime: 2024/3/22 14:00
     */
    @PostMapping("/key")
    @ApiOperation(value = "生成密钥认证", nickname = "generateKeyService")
    public BaseResponse<UserKeyVO> generateKey(HttpServletRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "request is null");
        }
        UserKeyVO userKeyVO = userService.generateKey(request);
        return Result.success(userKeyVO, "生成密钥成功");
    }

    /**
     * @description: 用户获取密钥对
     * @params: [request]
     * @return: com.qiu.api.common.BaseResponse<com.qiu.api.model.vo.user.UserKeyVO>
     * @author: qiu
     * @dateTime: 2024/3/23 21:07
     */
    @GetMapping("/key")
    @ApiOperation(value = "用户获取密钥对", nickname = "getKeyService")
    public BaseResponse<UserKeyVO> getKey(HttpServletRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "request is null");
        }
        UserKeyVO userKeyVO = userService.getKey(request);
        return Result.success(userKeyVO, "获取密钥成功");
    }

    /**
     * @description: 用户上传头像
     * @params: [file, request]
     * @return: com.qiu.apibackend.common.BaseResponse<java.lang.String>
     * @author: qiu
     * @dateTime: 2024/3/28 10:10
     */
    @PostMapping("/avatar")
    @ApiOperation(value = "用户上传头像", nickname = "uploadAvatarService")
    public BaseResponse<String> uploadAvatar(@ApiParam(value = "file", required = true) MultipartFile file,
                                             HttpServletRequest request) {
        String avatar = userService.uploadAvatar(file, request);
        return Result.success(avatar, "头像上传成功");
    }

}
