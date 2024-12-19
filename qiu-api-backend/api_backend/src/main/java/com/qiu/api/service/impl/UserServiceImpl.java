package com.qiu.api.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.qiu.api.common.ErrorCode;
import com.qiu.api.mapper.UserMapper;
import com.qiu.api.model.request.user.UserLoginRequest;
import com.qiu.api.model.request.user.UserRegisterRequest;
import com.qiu.api.model.request.user.UserUpdateRequest;
import com.qiu.api.model.po.User;
import com.qiu.api.model.vo.user.UserKeyVO;
import com.qiu.api.model.dto.UserSessionDTO;
import com.qiu.api.model.vo.user.UserListVO;
import com.qiu.api.model.vo.user.UserVO;
import com.qiu.api.utils.OssClient;
import com.qiu.api.utils.SHA256;
import com.qiu.api.exception.BusinessException;
import com.qiu.api.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qiu.common.model.InvokeUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.qiu.api.constant.RedisConstant.USER_UPLOAD_LIMIT_KEY;
import static com.qiu.api.constant.RedisConstant.USER_UPLOAD_LIMIT_TTL;
import static com.qiu.api.constant.UserConstant.ADMIN;
import static com.qiu.api.constant.UserConstant.USER_LOGIN_STATE;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author qiu
 * @since 2023-12-09
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * @description: 用户注册
     * @params: [userRegisterRequest]
     * @return: java.lang.Boolean
     * @author: qiu
     * @dateTime: 2023/12/16 19:34
     */
    @Override
    public Boolean userRegister(UserRegisterRequest userRegisterRequest) {
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String verifyPassword = userRegisterRequest.getVerifyPassword();
        // 1.参数校验
        if (StrUtil.hasEmpty(userAccount, userPassword, verifyPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数有空值");
        }
        // 2.账号密码校验
        Boolean checkRules = sameCheckRules(userRegisterRequest);
        if (!checkRules) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数错误");
        }
        // 3.密码和校验密码不相同
        if (!StrUtil.equals(userPassword, verifyPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码和校验密码不相同");
        }
        // 4.账户不能重复
        Long count = this.lambdaQuery().eq(User::getUserAccount, userAccount).count();
        if (count > 0) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "账户已注册");
        }
        // 5.对密码进行加密
        SHA256 encryption = SHA256.encryptionPassword(userPassword);
        userRegisterRequest.setUserPassword(encryption.getSha256Password());
        // 6.插入用户数据
        User user = BeanUtil.copyProperties(userRegisterRequest, User.class);
        user.setSalt(encryption.getSalt());
        boolean result = this.save(user);
        if (!result) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "注册失败");
        }
        return Boolean.TRUE;
    }

    /**
     * @description: 用户登录
     * @params: [userLoginRequest, request]
     * @return: com.qiu.api.model.vo.user.UserVO
     * @author: qiu
     * @dateTime: 2023/12/16 19:46
     */
    @Override
    public UserVO userLogin(UserLoginRequest userLoginRequest, HttpServletRequest request) {
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        // 1.校验
        if (StrUtil.hasEmpty(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数有空值");
        }
        Boolean checkRules = sameCheckRules(userLoginRequest);
        if (!checkRules) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数错误");
        }
        // 2.查询该用户
        User user = lambdaQuery().eq(User::getUserAccount, userAccount).one();
        if (BeanUtil.isEmpty(user)) {
            log.info("user login failed, userAccount cannot match userPassword");
            throw new BusinessException(ErrorCode.NULL_ERROR, "账号或密码错误");
        }
        // 3.校验密码
        String password = user.getUserPassword();
        String salt = user.getSalt();
        String decryptPassword = SHA256.decryptPassword(salt, userPassword);
        if (!StrUtil.equals(decryptPassword, password)) {
            log.info("user login failed, userAccount cannot match userPassword");
            throw new BusinessException(ErrorCode.NULL_ERROR, "账号或密码错误");
        }
        // 4.脱敏并记录用户登录状态
        UserSessionDTO userSessionVO = BeanUtil.copyProperties(user, UserSessionDTO.class);
        request.getSession().setAttribute(USER_LOGIN_STATE, userSessionVO);
        return BeanUtil.copyProperties(user, UserVO.class);
    }

    /**
     * @description: 查询用户
     * @params: [username]
     * @return: com.qiu.api.model.vo.user.UserListVO
     * @author: qiu
     * @dateTime: 2023/12/16 19:55
     */
    @Override
    public UserListVO searchUsers(String username) {
        List<User> users = this.lambdaQuery().like(username != null, User::getUsername, username).list();
        UserListVO userListVO = new UserListVO();
        userListVO.setTotal((long) users.size());
        if (CollUtil.isEmpty(users)) {
            userListVO.setUserList(Collections.emptyList());
            return userListVO;
        }
        List<UserVO> userVOS = BeanUtil.copyToList(users, UserVO.class);
        userListVO.setUserList(userVOS);
        return userListVO;

    }

    /**
     * @description: 管理员更新用户信息
     * @params: [user]
     * @return: java.lang.Boolean
     * @author: qiu
     * @dateTime: 2023/12/21 10:19
     */
    @Override
    public Boolean updateUserByAdmin(User user) {
        // 1.更新用户信息
        boolean isUpdate = this.updateById(user);
        // 2.判断是否更新成功
        if (!isUpdate) {
            throw new BusinessException(ErrorCode.NULL_ERROR, "更新失败，用户不存在");
        }
        // 3.更新成功返回true
        return true;
    }

    /**
     * @description: 用户更新个人信息
     * @params: [userUpdateRequest, request]
     * @return: java.lang.Boolean
     * @author: qiu
     * @dateTime: 2024/2/23 23:31
     */
    @Override
    public Boolean updateUser(UserUpdateRequest userUpdateRequest, HttpServletRequest request) {
        // 1.获取当前登录用户
        UserSessionDTO loginUser = getLoginUser(request);
        if (BeanUtil.isEmpty(loginUser)) {
            throw new BusinessException(ErrorCode.NOT_LOGIN, "用户未登录");
        }
        // 2.更新数据库
        User user = BeanUtil.copyProperties(userUpdateRequest, User.class);
        boolean update = updateById(user);
        if (!update) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "更新失败");
        }
        return true;
    }

    /**
     * @description: 生成密钥认证
     * @params: [request]
     * @return: com.qiu.api.model.vo.user.UserKeyVO
     * @author: qiu
     * @dateTime: 2024/3/22 14:05
     */
    @Override
    public UserKeyVO generateKey(HttpServletRequest request) {
        // 1.获取当前登录用户
        UserSessionDTO userSessionVO = getLoginUser(request);
        // 2.随机生成6为ak
        String originAk = RandomUtil.randomNumbers(6);
        // 3.随机生成8为sk
        String originSk = RandomUtil.randomNumbers(8);
        // 4.加密ak、sk
        String accessKey = SHA256.encryptionPasswordForAk(originAk);
        String secretKey = SHA256.encryptionPasswordForSk(originSk);
        // 5.存储
        userSessionVO.setAccessKey(accessKey);
        userSessionVO.setSecretKey(secretKey);
        User user = BeanUtil.copyProperties(userSessionVO, User.class);
        boolean result = lambdaUpdate().eq(User::getId, user.getId()).update(user);
        if (!result) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "生成密钥认证失败");
        }
        return BeanUtil.copyProperties(user, UserKeyVO.class);
    }

    /**
     * @description: 用户获取密钥对
     * @params: [request]
     * @return: com.qiu.api.model.vo.user.UserKeyVO
     * @author: qiu
     * @dateTime: 2024/3/23 21:10
     */
    @Override
    public UserKeyVO getKey(HttpServletRequest request) {
        // 1.获取当前登录用户
        UserSessionDTO user = getLoginUser(request);
        // 2.检查是否有ak、sk
        String accessKey = user.getAccessKey();
        String secretKey = user.getSecretKey();
        if (StrUtil.isBlank(accessKey) || StrUtil.isBlank(secretKey)) {
            throw new BusinessException(ErrorCode.NULL_ERROR, "签名认证不存在或已过期");
        }
        return BeanUtil.copyProperties(user, UserKeyVO.class);
    }

    /**
     * @description: 用户上传头像
     * @params: [file, request]
     * @return: java.lang.String
     * @author: qiu
     * @dateTime: 2024/3/25 22:39
     */
    @Override
    public String uploadAvatar(MultipartFile file, HttpServletRequest request) {
        if (file.getSize() > 1048576) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "头像大小限制1MB");
        }
        // 1.校验参数
        UserSessionDTO userSessionVO = getLoginUser(request);
        // Redis查询该用户上传次数
        String redisKey = USER_UPLOAD_LIMIT_KEY + userSessionVO.getId();
        ValueOperations<String, String> opsForValue = stringRedisTemplate.opsForValue();
        String loadNumStr = opsForValue.get(redisKey);
        int uploadNum = 0;
        // 如果不为空，并且次数达到5次禁止上传
        if (StrUtil.isNotBlank(loadNumStr)) {
            uploadNum = Integer.parseInt(loadNumStr);
            if (uploadNum >= 5) {
                throw new BusinessException(ErrorCode.FORBIDDEN, "已达到今日上传次数限制");
            }
        }
        String avatarUrl;
        try {
            avatarUrl = OssClient.uploadAvatar(file, userSessionVO.getId());
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "上传头像失败");
        }
        // 2.更新数据库
        userSessionVO.setAvatarUrl(avatarUrl);
        User user = BeanUtil.copyProperties(userSessionVO, User.class);
        boolean result = updateById(user);
        if (!result) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "上传头像失败");
        }
        // 如果Redis键为空先创建
        if (StrUtil.isBlank(loadNumStr)) {
            try {
                opsForValue.set(redisKey, "1", USER_UPLOAD_LIMIT_TTL, TimeUnit.HOURS);
            } catch (Exception e) {
                log.error("redis set key error", e);
            }
        } else {
            // 否则次数+1
            uploadNum = uploadNum + 1;
            String nowUploadNum = String.valueOf(uploadNum);
            Long expire = stringRedisTemplate.getExpire(redisKey, TimeUnit.SECONDS);
            if (expire != null && expire > 0) {
                Boolean setIfPresent = opsForValue.setIfPresent(redisKey, nowUploadNum, expire, TimeUnit.SECONDS);
                if (Boolean.FALSE.equals(setIfPresent)) {
                    log.error("redis update key error");
                }
            }
        }
        return avatarUrl;
    }

    /**
     * @description: 用户鉴权
     * @params: [request]
     * @return: java.lang.Boolean
     * @author: qiu
     * @dateTime: 2023/12/9 19:13
     */
    @Override
    public Boolean isAdmin(HttpServletRequest request) {
        Object attribute = request.getSession().getAttribute(USER_LOGIN_STATE);
        UserVO userVO = BeanUtil.copyProperties(attribute, UserVO.class);
        Integer userRole = userVO.getUserRole();
        return NumberUtil.equals(userRole, ADMIN);
    }

    /**
     * @description: 获取当前登录用户
     * @params: [request]
     * @return: com.qiu.api.model.vo.UserVO
     * @author: qiu
     * @dateTime: 2024/2/23 23:03
     */
    @Override
    public UserSessionDTO getLoginUser(HttpServletRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Object attribute = request.getSession().getAttribute(USER_LOGIN_STATE);
        if (BeanUtil.isEmpty(attribute)) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        return BeanUtil.copyProperties(attribute, UserSessionDTO.class);
    }

    /**
     * @description: 网关检查用户调用接口的权限
     * @params: [accessKey]
     * @return: java.lang.Boolean
     * @author: qiu
     * @dateTime: 2024/3/21 19:07
     */
    @Override
    public InvokeUser getInvokeUser(String accessKey) {
        // 1.校验参数
        if (StrUtil.isBlank(accessKey)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 2.数据库查询该用户
        User user = lambdaQuery().eq(User::getAccessKey, accessKey).one();
        if (BeanUtil.isEmpty(user)) {
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        // 3.检查密钥
        String secretKey = user.getSecretKey();
        if (StrUtil.isBlank(secretKey)) {
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        return BeanUtil.copyProperties(user, InvokeUser.class);
    }

    /**
     * @description: 账号密码校验
     * @params: [object]
     * @return: com.qiu.api.common.BaseResponse<java.lang.Boolean>
     * @author: qiu
     * @dateTime: 2023/12/16 19:34
     */
    private Boolean sameCheckRules(Object object) {
        User user = BeanUtil.copyProperties(object, User.class);
        String userAccount = user.getUserAccount();
        String userPassword = user.getUserPassword();
        // 1.非空校验
        if (BeanUtil.isEmpty(user) || StrUtil.hasEmpty(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号密码为空");
        }
        // 2.账户不小于4位
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账户不小于4位");
        }
        // 3.密码不小于8位
        if (userPassword.length() < 6) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码不小于6位");
        }
        // 4.账户不包含特殊字符
        if (!Validator.isGeneral(userAccount)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账户不包含特殊字符");
        }
        return true;
    }
}
