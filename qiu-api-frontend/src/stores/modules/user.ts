import { ref } from 'vue'
import { defineStore } from 'pinia'
import { Message } from '@arco-design/web-vue'
import ResponseCode from '@/ts/constant/ResponseCode'
import type { API } from '@/services/qiuapi-backend/typings'
import { userGetCurrentService } from '@/services/qiuapi-backend/user'

export const useUserStore = defineStore(
  'user',
  () => {
    // 用户是否登录
    const isLogin = ref<boolean>(false)
    // 当前用户信息
    const userInfo = ref<API.UserVO>({})

    // 登录后设置用户信息
    const setUserInfo = (newUserInfo: API.UserVO) => {
      userInfo.value = newUserInfo
      isLogin.value = true
    }
    // 获取用户信息
    const getUserInfo = () => {
      return userInfo.value
    }
    // 退出登录清空用户信息
    const removeUserInfo = () => {
      userInfo.value = {}
      isLogin.value = false
    }
    // 登录请求
    const getCurrentUser = async () => {
      const res = await userGetCurrentService()
      if (res.data.code !== ResponseCode.SUCCESS) {
        Message.warning('获取用户信息失败')
        return
      }
      if (!res.data.data) {
        Message.warning('获取用户信息失败')
        return
      }
      setUserInfo(res.data.data)
    }
    return {
      userInfo,
      isLogin,
      setUserInfo,
      getUserInfo,
      removeUserInfo,
      getCurrentUser
    }
  },
  // 持久化
  {
    persist: {
      paths: ['isLogin', 'userInfo']
    }
  }
)
