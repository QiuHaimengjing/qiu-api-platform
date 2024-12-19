package com.qiu.api.utils;

import cn.hutool.core.bean.BeanUtil;
import com.qiu.api.model.dto.UserSessionDTO;
import com.qiu.api.model.vo.user.UserVO;
import com.qiu.api.common.ErrorCode;
import com.qiu.api.exception.BusinessException;
import com.qiu.api.service.IUserService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @description:
 * @className: ParameterValidation.java
 * @author: qiu
 * @createTime: 2024/3/18 20:54
 */
@Component
public class ParameterValidation {

    @Resource
    private IUserService userService;

    /**
     * @description: 请求参数和登录校验，返回当前登录用户
     * @params: [object, request]
     * @return: com.qiu.api.model.vo.UserVO
     * @author: qiu
     * @dateTime: 2024/3/18 20:54
     */
    public <T> UserVO validationParamsAndUser(T object, HttpServletRequest request) {
        // 1.校验请求参数
        if (BeanUtil.isEmpty(object) || request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 2.获取当前登录用户
        UserSessionDTO userSessionVO = userService.getLoginUser(request);
        Long userId = userSessionVO.getId();
        if (BeanUtil.isEmpty(userSessionVO) || BeanUtil.isEmpty(userId)) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        return BeanUtil.copyProperties(userSessionVO, UserVO.class);
    }

}
