package com.qiu.api.mapper;

import com.qiu.api.model.po.UserInterfaceInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qiu.api.model.vo.StatisticVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 用户接口调用关系 Mapper 接口
 * </p>
 *
 * @author qiu
 * @since 2024-03-18
 */
public interface UserInterfaceInfoMapper extends BaseMapper<UserInterfaceInfo> {

    List<StatisticVO> queryInterfaceSta(@Param("limit") Long limit);

}
