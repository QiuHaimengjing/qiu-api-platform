package com.qiu.api.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qiu.api.common.ErrorCode;
import com.qiu.api.mapper.UserInterfaceInfoMapper;
import com.qiu.api.model.request.user_interface.UserInterfaceAddRequest;
import com.qiu.api.model.request.user_interface.UserInterfaceQueryRequest;
import com.qiu.api.model.request.user_interface.UserInterfaceUpdateRequest;
import com.qiu.api.model.enums.InterfaceStatusEnum;
import com.qiu.api.model.po.UserInterfaceInfo;
import com.qiu.api.model.vo.StatisticVO;
import com.qiu.api.model.vo.user_interface.UserInterfaceListVO;
import com.qiu.api.model.vo.user_interface.UserInterfaceVO;
import com.qiu.api.model.dto.UserSessionDTO;
import com.qiu.api.service.IUserService;
import com.qiu.api.utils.ParameterValidation;
import com.qiu.api.exception.BusinessException;
import com.qiu.api.service.IUserInterfaceInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.qiu.api.constant.UserConstant.USER_LOGIN_STATE;

/**
 * <p>
 * 用户接口调用关系 服务实现类
 * </p>
 *
 * @author qiu
 * @since 2024-03-18
 */
@Service
public class UserInterfaceInfoServiceImpl extends ServiceImpl<UserInterfaceInfoMapper, UserInterfaceInfo> implements IUserInterfaceInfoService {

    @Resource
    private ParameterValidation parameterValidation;

    @Resource
    private IUserService userService;

    /**
     * @description: 创建用户接口调用关系
     * @params: [userInterfaceAddRequest, request]
     * @return: java.lang.Long
     * @author: qiu
     * @dateTime: 2024/3/18 21:03
     */
    @Override
    public Long addUserInterface(UserInterfaceAddRequest userInterfaceAddRequest, HttpServletRequest request) {
        // 1. 校验请求参数
        parameterValidation.validationParamsAndUser(userInterfaceAddRequest, request);
        // 2. 判断该用户是否有ak、sk
        UserSessionDTO user = userService.getLoginUser(request);
        String accessKey = user.getAccessKey();
        String secretKey = user.getSecretKey();
        if (StrUtil.isBlank(accessKey) || StrUtil.isBlank(secretKey)) {
            throw new BusinessException(ErrorCode.NO_AUTH, "你还未获取API签名认证");
        }
        // 3. 该关系是否已经存在
        Long count = lambdaQuery().eq(UserInterfaceInfo::getUserId, user.getId())
                .eq(UserInterfaceInfo::getInterfaceId, userInterfaceAddRequest.getInterfaceId()).count();
        if (BeanUtil.isNotEmpty(count) && count > 0) {
            throw new BusinessException(ErrorCode.FORBIDDEN, "不能重复获取该接口调用权限");
        }
        // 4. 更新数据库
        UserInterfaceInfo userInterfaceInfo = BeanUtil.copyProperties(userInterfaceAddRequest, UserInterfaceInfo.class);
        // 5. 授予50次免费调用的机会
        userInterfaceInfo.setLeftNum(50);
        boolean result = save(userInterfaceInfo);
        if (!result) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }
        return userInterfaceInfo.getId();
    }

    /**
     * @description: 删除用户接口调用关系
     * @params: [id, request]
     * @return: java.lang.Boolean
     * @author: qiu
     * @dateTime: 2024/3/18 21:05
     */
    @Override
    public Boolean deleteUserInterface(Long id, HttpServletRequest request) {
        // 1. 校验请求参数
        parameterValidation.validationParamsAndUser(id, request);
        // 更新数据库
        boolean result = removeById(id);
        if (!result) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "删除失败");
        }
        return true;
    }

    /**
     * @description: 修改用户接口调用关系
     * @params: [userInterfaceUpdateRequest, request]
     * @return: com.qiu.api.common.BaseResponse<java.lang.Boolean>
     * @author: qiu
     * @dateTime: 2024/3/18 21:06
     */
    @Override
    public Boolean updateUserInterface(UserInterfaceUpdateRequest userInterfaceUpdateRequest, HttpServletRequest request) {
        // 1. 校验请求参数
        parameterValidation.validationParamsAndUser(userInterfaceUpdateRequest, request);
        Long id = userInterfaceUpdateRequest.getId();
        Long userId = userInterfaceUpdateRequest.getUserId();
        Long interfaceId = userInterfaceUpdateRequest.getInterfaceId();
        Integer totalNum = userInterfaceUpdateRequest.getTotalNum();
        Integer leftNum = userInterfaceUpdateRequest.getLeftNum();
        Integer status = userInterfaceUpdateRequest.getStatus();
        if (BeanUtil.isEmpty(id) || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户接口关系id不符合规范");
        }
        if (BeanUtil.isEmpty(userId) || userId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户id不符合规范");
        }
        if (BeanUtil.isEmpty(interfaceId) || interfaceId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "接口id不符合规范");
        }
        if (totalNum < 0 || leftNum < 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "总次数或剩余次数必须为正数");
        }
        boolean contains = Arrays.stream(InterfaceStatusEnum.values())
                .map(InterfaceStatusEnum::getStatus)
                .collect(Collectors.toList()).contains(status);
        if (!contains) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "关系状态设置错误");
        }
        // 2. 更新数据库
        UserInterfaceInfo userInterfaceInfo = BeanUtil.copyProperties(userInterfaceUpdateRequest, UserInterfaceInfo.class);
        boolean result = updateById(userInterfaceInfo);
        if (!result) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "修改失败");
        }
        return true;
    }

    /**
     * @description: 查询用户接口调用关系列表
     * @params: [userInterfaceQueryRequest, request]
     * @return: com.qiu.api.model.vo.user_interface.UserInterfaceListVO
     * @author: qiu
     * @dateTime: 2024/3/18 21:08
     */
    @Override
    public UserInterfaceListVO getUserInterfaceList(UserInterfaceQueryRequest userInterfaceQueryRequest, HttpServletRequest request) {
        // 1. 校验请求参数
        parameterValidation.validationParamsAndUser(userInterfaceQueryRequest, request);
        Long id = userInterfaceQueryRequest.getId();
        Long userId = userInterfaceQueryRequest.getUserId();
        Long interfaceId = userInterfaceQueryRequest.getInterfaceId();
        Integer status = userInterfaceQueryRequest.getStatus();
        Integer pageNO = userInterfaceQueryRequest.getPageNO();
        Integer pageSize = userInterfaceQueryRequest.getPageSize();
        // 2. 组合查询条件
        LambdaQueryWrapper<UserInterfaceInfo> queryWrapper = new LambdaQueryWrapper<>();
        // 2.1 根据id
        Optional.ofNullable(id).filter(BeanUtil::isNotEmpty)
                .ifPresent(item -> queryWrapper.eq(UserInterfaceInfo::getId, item));
        // 2.2 根据用户id
        Optional.ofNullable(userId).filter(BeanUtil::isNotEmpty)
                .ifPresent(item -> queryWrapper.eq(UserInterfaceInfo::getUserId, item));
        // 2.3 根据接口id
        Optional.ofNullable(interfaceId).filter(BeanUtil::isNotEmpty)
                .ifPresent(item -> queryWrapper.eq(UserInterfaceInfo::getInterfaceId, item));
        // 2.4 根据关系状态
        Optional.ofNullable(status).filter(BeanUtil::isNotEmpty)
                .ifPresent(item -> queryWrapper.eq(UserInterfaceInfo::getStatus, item));
        // 3. 对用户接口关系信息进行分页
        // 校验分页参数
        if (BeanUtil.isEmpty(pageNO) || BeanUtil.isEmpty(pageSize)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "非法的分页参数");
        }
        Page<UserInterfaceInfo> page = Page.of(pageNO, pageSize);
        Page<UserInterfaceInfo> teamsPage = this.page(page, queryWrapper);
        // 获取分页记录
        List<UserInterfaceInfo> records = teamsPage.getRecords();
        UserInterfaceListVO userInterfaceListVO = new UserInterfaceListVO();
        userInterfaceListVO.setTotal(teamsPage.getTotal());
        // 如果为空直接返回
        if (CollUtil.isEmpty(records)) {
            userInterfaceListVO.setUserInterfaceList(Collections.emptyList());
            return userInterfaceListVO;
        }
        // 4. 信息脱敏
        List<UserInterfaceVO> userInterfaceVOS = BeanUtil.copyToList(records, UserInterfaceVO.class);
        userInterfaceListVO.setUserInterfaceList(userInterfaceVOS);
        return userInterfaceListVO;
    }

    /**
     * @description: 网关更新用户接口关联信息
     * @params: [interfaceId, userId]
     * @return: java.lang.Boolean
     * @author: qiu
     * @dateTime: 2024/3/22 22:38
     */
    @Override
    public Boolean invokeCount(Long interfaceId, Long userId) {
        // 1.校验参数
        if (BeanUtil.isEmpty(interfaceId) || BeanUtil.isEmpty(userId)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 2.更新数据库
        boolean update = lambdaUpdate().eq(UserInterfaceInfo::getInterfaceId, interfaceId)
                .eq(UserInterfaceInfo::getUserId, userId)
                .setSql("`left_num` = `left_num` - 1, `total_num` = `total_num` + 1").update();
        if (!update) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "用户接口关系更新失败");
        }
        return true;
    }

    /**
     * @description: 统计分析接口调用次数
     * @params: [limit]
     * @return: java.util.List<com.qiu.api.model.vo.StatisticVO>
     * @author: qiu
     * @dateTime: 2024/3/22 15:27
     */
    @Override
    public List<StatisticVO> interfaceStatistic(Long limit) {
        // 1.校验参数
        if (BeanUtil.isEmpty(limit) || limit <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "接口数量必须大于0");
        }
        // 查询数据库
        List<StatisticVO> statisticVOS = baseMapper.queryInterfaceSta(limit);
        if (CollUtil.isEmpty(statisticVOS)) {
            return Collections.emptyList();
        }
        return statisticVOS;
    }

    /**
     * @description: 用户获取对应接口关系
     * @params: [id, request]
     * @return: com.qiu.api.model.vo.user_interface.UserInterfaceVO
     * @author: qiu
     * @dateTime: 2024/3/23 14:30
     */
    @Override
    public UserInterfaceVO getUserInterfaceById(Long id, HttpServletRequest request) {
        // 1.校验参数
        if (BeanUtil.isEmpty(id)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if (request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 2.获取当前登录用户
        UserSessionDTO userSessionVO;
        Object attribute = request.getSession().getAttribute(USER_LOGIN_STATE);
        // 如果未登录直接返回null
        if (BeanUtil.isEmpty(attribute)) {
            return null;
        }
        userSessionVO = BeanUtil.copyProperties(attribute, UserSessionDTO.class);
        // 3.查询数据库
        UserInterfaceInfo userInterfaceInfo = lambdaQuery().eq(UserInterfaceInfo::getInterfaceId, id)
                .eq(UserInterfaceInfo::getUserId, userSessionVO.getId()).one();
        if (BeanUtil.isEmpty(userInterfaceInfo)) {
            return null;
        }
        return BeanUtil.copyProperties(userInterfaceInfo, UserInterfaceVO.class);
    }
}
