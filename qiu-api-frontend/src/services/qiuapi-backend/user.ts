// @ts-ignore
/* eslint-disable */
import request from '@/utils/request';
import type { API } from './typings';

/** 用户更新个人信息 PUT /user/ */
export async function updateUserService(
  body: API.UserUpdateRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseBoolean_>('/user/', {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 用户注册 POST /user/ */
export async function userRegisterService(
  body: API.UserRegisterRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseBoolean_>('/user/', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 删除用户 DELETE /user/ */
export async function deleteUserService(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.deleteUserServiceParams,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseBoolean_>('/user/', {
    method: 'DELETE',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** 管理员更新用户信息 PUT /user/admin */
export async function updateUserByAdminService(body: API.User, options?: { [key: string]: any }) {
  return request<API.BaseResponseBoolean_>('/user/admin', {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 用户上传头像 POST /user/avatar */
export async function uploadAvatarService(body: {}, file?: File, options?: { [key: string]: any }) {
  const formData = new FormData();

  if (file) {
    formData.append('file', file);
  }

  Object.keys(body).forEach((ele) => {
    const item = (body as any)[ele];

    if (item !== undefined && item !== null) {
      if (typeof item === 'object' && !(item instanceof File)) {
        if (item instanceof Array) {
          item.forEach((f) => formData.append(ele, f || ''));
        } else {
          formData.append(ele, JSON.stringify(item));
        }
      } else {
        formData.append(ele, item);
      }
    }
  });

  return request<API.BaseResponseString_>('/user/avatar', {
    method: 'POST',
    data: formData,
    ...(options || {}),
  });
}

/** 用户获取密钥对 GET /user/key */
export async function getKeyService(options?: { [key: string]: any }) {
  return request<API.BaseResponseUserKeyVO_>('/user/key', {
    method: 'GET',
    ...(options || {}),
  });
}

/** 生成密钥认证 POST /user/key */
export async function generateKeyService(options?: { [key: string]: any }) {
  return request<API.BaseResponseUserKeyVO_>('/user/key', {
    method: 'POST',
    ...(options || {}),
  });
}

/** 获取用户登录态 GET /user/me */
export async function userGetCurrentService(options?: { [key: string]: any }) {
  return request<API.BaseResponseUserVO_>('/user/me', {
    method: 'GET',
    ...(options || {}),
  });
}

/** 查询用户列表 POST /user/search */
export async function getUserListService(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getUserListServiceParams,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseUserListVO_>('/user/search', {
    method: 'POST',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** 用户登录 POST /user/session */
export async function userLoginService(
  body: API.UserLoginRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseUserVO_>('/user/session', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 用户注销 DELETE /user/session */
export async function userLogoutService(options?: { [key: string]: any }) {
  return request<API.BaseResponseBoolean_>('/user/session', {
    method: 'DELETE',
    ...(options || {}),
  });
}
