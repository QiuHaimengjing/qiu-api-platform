package com.qiu.api.service;

import com.qiu.api.model.request.user.UserLoginRequest;
import com.qiu.api.model.request.user.UserRegisterRequest;
import com.qiu.api.model.request.user.UserUpdateRequest;
import com.qiu.api.model.po.User;
import com.qiu.api.model.vo.user.UserKeyVO;
import com.qiu.api.model.dto.UserSessionDTO;
import com.qiu.api.model.vo.user.UserListVO;
import com.qiu.api.model.vo.user.UserVO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.qiu.common.model.InvokeUser;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author qiu
 * @since 2023-12-09
 */
public interface IUserService extends IService<User> {

    Boolean userRegister(UserRegisterRequest userRegisterRequest);

    UserVO userLogin(UserLoginRequest userLoginRequest, HttpServletRequest request);

    UserListVO searchUsers(String username);

    Boolean updateUserByAdmin(User user);

    Boolean updateUser(UserUpdateRequest userUpdateRequest, HttpServletRequest request);

    UserKeyVO generateKey(HttpServletRequest request);

    UserKeyVO getKey(HttpServletRequest request);

    String uploadAvatar(MultipartFile file, HttpServletRequest request);

    Boolean isAdmin(HttpServletRequest request);

    UserSessionDTO getLoginUser(HttpServletRequest request);

    InvokeUser getInvokeUser(String accessKey);

}
