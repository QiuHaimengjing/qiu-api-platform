// @ts-ignore
/* eslint-disable */
import request from '@/utils/request';
import type { API } from './typings';

/** 根据id查询用户接口关系 GET /user-interface/ */
export async function getUserInterfaceByIdService(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getUserInterfaceByIdServiceParams,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseUserInterfaceVO_>('/user-interface/', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** 修改用户接口调用关系 PUT /user-interface/ */
export async function updateUserInterfaceService(
  body: API.UserInterfaceUpdateRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseBoolean_>('/user-interface/', {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 创建用户接口调用关系 POST /user-interface/ */
export async function addUserInterfaceService(
  body: API.UserInterfaceAddRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseLong_>('/user-interface/', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 删除用户接口调用关系 DELETE /user-interface/ */
export async function deleteUserInterfaceService(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.deleteUserInterfaceServiceParams,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseBoolean_>('/user-interface/', {
    method: 'DELETE',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** 查询用户接口调用关系列表 POST /user-interface/search */
export async function getUserInterfaceListService(
  body: API.UserInterfaceQueryRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseUserInterfaceListVO_>('/user-interface/search', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 统计分析：接口调用次数排名（降序） GET /user-interface/statistic */
export async function interfaceStatisticService(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.interfaceStatisticServiceParams,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseListStatisticsVO_>('/user-interface/statistic', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}
