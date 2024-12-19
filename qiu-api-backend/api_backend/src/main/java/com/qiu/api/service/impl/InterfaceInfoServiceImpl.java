package com.qiu.api.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qiu.api.common.ErrorCode;
import com.qiu.api.mapper.InterfaceInfoMapper;
import com.qiu.api.model.request.interface_info.InterfaceAddRequest;
import com.qiu.api.model.request.interface_info.InterfaceQueryRequest;
import com.qiu.api.model.request.interface_info.InterfaceUpdateRequest;
import com.qiu.api.model.enums.InterfaceStatusEnum;
import com.qiu.api.model.po.InterfaceInfo;

import com.qiu.api.model.po.UserInterfaceInfo;
import com.qiu.api.model.request.InterfaceInvokeRequest;
import com.qiu.api.model.vo.interface_info.InterfaceListVO;
import com.qiu.api.model.vo.interface_info.InterfaceVO;
import com.qiu.api.model.dto.UserSessionDTO;
import com.qiu.api.model.vo.user.UserVO;
import com.qiu.api.service.IInterfaceInfoService;
import com.qiu.api.service.IUserInterfaceInfoService;
import com.qiu.api.service.IUserService;
import com.qiu.api.utils.ParameterValidation;
import com.qiu.api.exception.BusinessException;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qiu.apiclientsdk.client.QiuApiClient;
import com.qiu.apiclientsdk.model.InvokeParams;
import com.qiu.common.model.InvokeInterface;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static com.qiu.api.constant.UserConstant.USER_LOGIN_STATE;

/**
 * <p>
 * 接口信息 服务实现类
 * </p>
 *
 * @author qiu
 * @since 2024-03-12
 */
@Service
public class InterfaceInfoServiceImpl extends ServiceImpl<InterfaceInfoMapper, InterfaceInfo> implements IInterfaceInfoService {

    @Resource
    private IUserService userService;

    @Resource
    private ParameterValidation parameterValidation;

    @Resource
    IUserInterfaceInfoService userInterfaceInfoService;

    /**
     * @description: 创建接口
     * @params: [interfaceAddRequest, request]
     * @return: java.lang.Long
     * @author: qiu
     * @dateTime: 2024/3/12 13:50
     */
    @Override
    public Long addInterface(InterfaceAddRequest interfaceAddRequest, HttpServletRequest request) {
        // 1. 校验请求参数
        UserVO user = parameterValidation.validationParamsAndUser(interfaceAddRequest, request);
        // 2. 获取当前登录用户
        Long userId = user.getId();
        String name = interfaceAddRequest.getName();
        String host = interfaceAddRequest.getHost();
        String url = interfaceAddRequest.getUrl();
        String method = interfaceAddRequest.getMethod();
        // 3. 名称不能为空
        if (StrUtil.isBlank(name)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "接口名称不能为空");
        }
        // 4. 接口地址不能为空
        if (StrUtil.isBlank(url) || StrUtil.isBlank(host)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "接口地址不能为空");
        }
        // 5.请求类型
        if (StrUtil.isBlank(method)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求类型不能为空");
        }
        // 更新数据库
        InterfaceInfo interfaceInfo = BeanUtil.copyProperties(interfaceAddRequest, InterfaceInfo.class);
        interfaceInfo.setUserId(userId);
        boolean result = save(interfaceInfo);
        if (!result) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "创建接口失败");
        }
        return interfaceInfo.getId();
    }

    /**
     * @description: 删除接口
     * @params: [ids, request]
     * @return: java.lang.Boolean
     * @author: qiu
     * @dateTime: 2024/3/23 16:46
     */
    @Override
    @Transactional(rollbackFor = BusinessException.class)
    public Boolean deleteInterface(List<Long> idList, HttpServletRequest request) {
        // 1. 校验请求参数
        parameterValidation.validationParamsAndUser(idList, request);
        // 更新数据库
        boolean result = removeBatchByIds(idList);
        if (!result) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "删除失败");
        }
        return true;
    }

    /**
     * @description: 修改接口
     * @params: [interfaceUpdateRequest, request]
     * @return: java.lang.Boolean
     * @author: qiu
     * @dateTime: 2024/3/12 13:51
     */
    @Override
    public Boolean updateInterface(InterfaceUpdateRequest interfaceUpdateRequest, HttpServletRequest request) {
        // 1. 校验请求参数
        parameterValidation.validationParamsAndUser(interfaceUpdateRequest, request);
        Long id = interfaceUpdateRequest.getId();
        String name = interfaceUpdateRequest.getName();
        String host = interfaceUpdateRequest.getHost();
        String url = interfaceUpdateRequest.getUrl();
        Integer status = interfaceUpdateRequest.getStatus();
        String method = interfaceUpdateRequest.getMethod();
        // 2.查询接口是否存在
        if (BeanUtil.isEmpty(id) || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo exist = getById(id);
        if (BeanUtil.isEmpty(exist)) {
            throw new BusinessException(ErrorCode.NULL_ERROR, "接口不存在");
        }
        // 3.检查接口名称和路径
        if (StrUtil.isBlank(name) || StrUtil.isBlank(url) || StrUtil.isBlank(host)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "接口名称或路径不能为空");
        }
        // 4.检查接口方法
        if (StrUtil.isBlank(method)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求方法不能为空");
        }
        // 5.检查接口状态参数
        if (!status.equals(InterfaceStatusEnum.OFFLINE.getStatus()) &&
                !status.equals(InterfaceStatusEnum.ONLINE.getStatus())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "接口状态参数错误");
        }
        // 更新数据库
        InterfaceInfo interfaceInfo = BeanUtil.copyProperties(interfaceUpdateRequest, InterfaceInfo.class);
        boolean result = updateById(interfaceInfo);
        if (!result) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "修改接口信息失败");
        }
        return true;
    }

    /**
     * @description: 查询接口列表
     * @params: [interfaceQueryRequest, request]
     * @return: com.qiu.api.model.vo.interface_info.InterfaceListVO
     * @author: qiu
     * @dateTime: 2024/3/12 13:51
     */
    @Override
    public InterfaceListVO interfaceList(InterfaceQueryRequest interfaceQueryRequest, HttpServletRequest request) {
        // 1. 校验请求参数
        if (BeanUtil.isEmpty(interfaceQueryRequest)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if (request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Object attribute = request.getSession().getAttribute(USER_LOGIN_STATE);
        boolean admin = false;
        if (BeanUtil.isNotEmpty(attribute)) {
            admin = userService.isAdmin(request);
        }
        // 接口信息
        Long id = interfaceQueryRequest.getId();
        String name = interfaceQueryRequest.getName();
        String description = interfaceQueryRequest.getDescription();
        String host = interfaceQueryRequest.getHost();
        String url = interfaceQueryRequest.getUrl();
        Integer status = interfaceQueryRequest.getStatus();
        Integer pageNO = interfaceQueryRequest.getPageNO();
        Integer pageSize = interfaceQueryRequest.getPageSize();
        // 2.组合查询条件
        LambdaQueryWrapper<InterfaceInfo> queryWrapper = new LambdaQueryWrapper<>();
        // 2.1 根据id查询
        if (BeanUtil.isNotEmpty(id) && id > 0) {
            queryWrapper.eq(InterfaceInfo::getId, id);
        }
        // 2.2 根据接口名称查询
        Optional.ofNullable(name).filter(StrUtil::isNotBlank).ifPresent(item -> queryWrapper.like(InterfaceInfo::getName, item));
        // 2.3 根据描述查询
        Optional.ofNullable(description).filter(StrUtil::isNotBlank).ifPresent(item -> queryWrapper.like(InterfaceInfo::getDescription, item));
        // 根据服务器地址查询
        Optional.ofNullable(host).filter(StrUtil::isNotBlank).ifPresent(item -> queryWrapper.like(InterfaceInfo::getHost, item));
        // 2.4 根据url地址查询
        Optional.ofNullable(url).filter(StrUtil::isNotBlank).ifPresent(item -> queryWrapper.like(InterfaceInfo::getUrl, item));
        // 2.5 如果是管理员可选查询
        if (BeanUtil.isNotEmpty(status) && admin) {
            queryWrapper.eq(InterfaceInfo::getStatus, status);
        }
        if (!admin) {
            // 默认查询已上线的接口
            queryWrapper.eq(InterfaceInfo::getStatus, InterfaceStatusEnum.ONLINE.getStatus());
        }
        // 对接口进行分页
        // 校验分页参数
        if (BeanUtil.isEmpty(pageNO) || BeanUtil.isEmpty(pageSize)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "非法的分页参数");
        }
        Page<InterfaceInfo> page = Page.of(pageNO, pageSize);
        Page<InterfaceInfo> teamsPage = this.page(page, queryWrapper);
        // 获取分页记录
        List<InterfaceInfo> records = teamsPage.getRecords();
        // 如果为空直接返回
        InterfaceListVO interfaceListVO = new InterfaceListVO();
        interfaceListVO.setTotal(teamsPage.getTotal());
        if (CollUtil.isEmpty(records)) {
            interfaceListVO.setInterfaceList(Collections.emptyList());
            return interfaceListVO;
        }
        // 队伍信息脱敏
        List<InterfaceVO> interfaceVOS = BeanUtil.copyToList(records, InterfaceVO.class);
        interfaceListVO.setInterfaceList(interfaceVOS);
        return interfaceListVO;
    }

    /**
     * @description: 根据id查询接口
     * @params: [id]
     * @return: com.qiu.api.model.vo.interface_info.InterfaceVO
     * @author: qiu
     * @dateTime: 2024/3/17 16:16
     */
    @Override
    public InterfaceVO queryInterfaceById(Long id) {
        // 1.校验参数
        if (BeanUtil.isEmpty(id) || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 2.检查是否存在该接口
        InterfaceInfo interfaceInfo = getById(id);
        if (BeanUtil.isEmpty(interfaceInfo)) {
            throw new BusinessException(ErrorCode.NULL_ERROR, "接口不存在");
        }
        // 3.脱敏
        return BeanUtil.copyProperties(interfaceInfo, InterfaceVO.class);
    }

    /**
     * @description: 上线接口
     * @params: [id, request]
     * @return: java.lang.Boolean
     * @author: qiu
     * @dateTime: 2024/3/17 13:01
     */
    @Override
    public Boolean onlineInterface(Long id, HttpServletRequest request) {
        // 1.检验参数
        if (BeanUtil.isEmpty(id) || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 2.校验该接口是否存在
        InterfaceInfo interfaceInfo = getById(id);
        if (BeanUtil.isEmpty(interfaceInfo)) {
            throw new BusinessException(ErrorCode.NULL_ERROR, "接口不存在");
        }
        // 3.判断该接口是否可以调用
        // TODO，与网关统一请求冲突，需要额外添加一个上线专用的调用接口，或者在 InvokeParams 添加 invoke、online标识
//        UserSessionDTO loginUser = userService.getLoginUser(request);
//        String accessKey = loginUser.getAccessKey();
//        String secretKey = loginUser.getSecretKey();
//        // 3.如果当前用户ak或sk为空则无权限
//        if (StrUtil.isBlank(accessKey) || StrUtil.isBlank(secretKey)) {
//            throw new BusinessException(ErrorCode.NO_AUTH);
//        }
//        InvokeParams invokeParams = new InvokeParams();
//        invokeParams.setHost(interfaceInfo.getHost());
//        invokeParams.setUrl(interfaceInfo.getUrl());
//        invokeParams.setRequestParams(interfaceInfo.getRequestParams());
//        invokeParams.setMethod(interfaceInfo.getMethod());
//        QiuApiClient qiuApiClient = new QiuApiClient(accessKey, secretKey);
//        Boolean result = qiuApiClient.invokeInterfaceStatus(invokeParams);
//        if (!result) {
//            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "接口调用失败");
//        }
        // 4.仅本人和管理员可修改
        interfaceInfo.setStatus(InterfaceStatusEnum.ONLINE.getStatus());
        boolean updateResult = updateById(interfaceInfo);
        if (!updateResult) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "更新状态失败");
        }
        return true;
    }

    /**
     * @description: 下线接口
     * @params: [id]
     * @return: java.lang.Boolean
     * @author: qiu
     * @dateTime: 2024/3/17 13:01
     */
    @Override
    public Boolean offlineInterface(Long id) {
        // 1.校验参数
        if (BeanUtil.isEmpty(id) || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 2.校验该接口是否存在
        InterfaceInfo interfaceInfo = getById(id);
        if (BeanUtil.isEmpty(interfaceInfo)) {
            throw new BusinessException(ErrorCode.NULL_ERROR, "接口不存在");
        }
        // 3.仅本人或管理员可修改
        interfaceInfo.setStatus(InterfaceStatusEnum.OFFLINE.getStatus());
        boolean result = updateById(interfaceInfo);
        if (!result) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "更新接口状态失败");
        }
        return true;
    }

    /**
     * @description: 调用接口
     * @params: [invokeRequest, request]
     * @return: java.lang.Object
     * @author: qiu
     * @dateTime: 2024/3/17 19:52
     */
    @Override
    public Object invokeInterface(InterfaceInvokeRequest invokeRequest, HttpServletRequest request) {
        // 1.校验参数
        parameterValidation.validationParamsAndUser(invokeRequest, request);
        // 2.获取当前用户ak、sk
        UserSessionDTO loginUser = userService.getLoginUser(request);
        String accessKey = loginUser.getAccessKey();
        String secretKey = loginUser.getSecretKey();
        // 3.如果当前用户ak或sk为空则无权限
        if (StrUtil.isBlank(accessKey) || StrUtil.isBlank(secretKey)) {
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        // 4.获取调用参数
        Long id = invokeRequest.getId();
        String requestParams = invokeRequest.getRequestParams();
        if (StrUtil.isBlank(requestParams)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        // 5.检查接口是否存在
        InterfaceInfo interfaceInfo = getById(id);
        if (BeanUtil.isEmpty(interfaceInfo)) {
            throw new BusinessException(ErrorCode.NULL_ERROR, "接口不存在");
        }
        // 6.检查该用户对该接口的调用关系
        UserInterfaceInfo userInterfaceInfo = userInterfaceInfoService.lambdaQuery()
                .eq(UserInterfaceInfo::getInterfaceId, interfaceInfo.getId())
                .eq(UserInterfaceInfo::getUserId, loginUser.getId()).one();
        if (BeanUtil.isEmpty(userInterfaceInfo)) {
            throw new BusinessException(ErrorCode.NO_AUTH, "你还未获取该接口调用权限");
        }
        Integer leftNum = userInterfaceInfo.getLeftNum();
        if (BeanUtil.isEmpty(leftNum) || leftNum <= 0) {
            throw new BusinessException(ErrorCode.NO_AUTH, "调用次数不足");
        }
        // 7.封装调用参数
        InvokeParams invokeParams = new InvokeParams();
        invokeParams.setHost(interfaceInfo.getHost());
        invokeParams.setUrl(interfaceInfo.getUrl());
        invokeParams.setRequestParams(requestParams);
        invokeParams.setMethod(interfaceInfo.getMethod());
        // 8.调用接口
        QiuApiClient qiuApiClient = new QiuApiClient(accessKey, secretKey);
        return qiuApiClient.invokeInterface(invokeParams);
    }

    /**
     * @description: 网关检查接口
     * @params: [host, path, method]
     * @return: com.qiu.common.model.InvokeInterface
     * @author: qiu
     * @dateTime: 2024/3/22 22:38
     */
    @Override
    public InvokeInterface getInvokeInterface(String host, String path, String method) {
        // 1.校验参数
        if (StrUtil.isBlank(path) || StrUtil.isBlank(method)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 2.检查数据库该方法是否存在
        InterfaceInfo interfaceInfo = lambdaQuery().eq(InterfaceInfo::getHost, host).eq(InterfaceInfo::getUrl, path)
                .eq(InterfaceInfo::getMethod, method).one();
        if (BeanUtil.isEmpty(interfaceInfo)) {
            throw new BusinessException(ErrorCode.NULL_ERROR, "接口不存在");
        }
        // 3.检查接口是否关闭
        Integer status = interfaceInfo.getStatus();
        if (status.equals(InterfaceStatusEnum.OFFLINE.getStatus())) {
            throw new BusinessException(ErrorCode.NULL_ERROR, "接口已关闭");
        }
        // 4.返回接口信息
        return BeanUtil.copyProperties(interfaceInfo, InvokeInterface.class);
    }

}
