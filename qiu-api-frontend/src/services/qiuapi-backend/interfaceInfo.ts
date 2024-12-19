// @ts-ignore
/* eslint-disable */
import request from '@/utils/request';
import type { API } from './typings';

/** 根据id查询接口 GET /interface/ */
export async function getInterfaceByIdService(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getInterfaceByIdServiceParams,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseInterfaceVO_>('/interface/', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** 修改接口 PUT /interface/ */
export async function updateInterfaceService(
  body: API.InterfaceUpdateRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseBoolean_>('/interface/', {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 创建接口 POST /interface/ */
export async function addInterfaceService(
  body: API.InterfaceAddRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseLong_>('/interface/', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 调用接口 POST /interface/invoke */
export async function invokeInterfaceService(
  body: API.InterfaceInvokeRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseObject_>('/interface/invoke', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 删除接口 DELETE /interface/items */
export async function deleteInterfaceService(body: number[], options?: { [key: string]: any }) {
  return request<API.BaseResponseBoolean_>('/interface/items', {
    method: 'DELETE',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 下线接口 POST /interface/offline */
export async function offlineInterfaceService(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.offlineInterfaceServiceParams,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseBoolean_>('/interface/offline', {
    method: 'POST',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** 上线接口 POST /interface/online */
export async function onlineInterfaceService(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.onlineInterfaceServiceParams,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseBoolean_>('/interface/online', {
    method: 'POST',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** 查询接口列表 POST /interface/search */
export async function getInterfaceListService(
  body: API.InterfaceQueryRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseInterfaceListVO_>('/interface/search', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}
