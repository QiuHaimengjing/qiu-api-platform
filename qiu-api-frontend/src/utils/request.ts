import axios, { type AxiosResponse, type InternalAxiosRequestConfig } from 'axios'
import ResponseCode from '@/ts/constant/ResponseCode'
import { useUserStore } from '@/stores'
import router from '@/router'
import { Message } from '@arco-design/web-vue'

interface ApiResponse<T = any> {
  code: number
  data: T
  message: string
  description: string
}

const instance = axios.create({
  baseURL: '/api',
  timeout: 10000
})

// 添加请求拦截器
instance.interceptors.request.use(
  function (config: InternalAxiosRequestConfig) {
    // 在发送请求之前做些什么
    return config
  },
  function (error) {
    // 对请求错误做些什么
    return Promise.reject(error)
  }
)

// 添加响应拦截器
instance.interceptors.response.use(
  function (response: AxiosResponse<ApiResponse<any>>): Promise<any> {
    // 2xx 范围内的状态码都会触发该函数。
    // 对响应数据做点什么
    if (response?.data?.code !== ResponseCode.SUCCESS) {
      // 如果用户未登录清空本地存储
      if (response?.data?.code === ResponseCode.NOT_LOGIN) {
        const userStore = useUserStore()
        userStore.removeUserInfo()
        router.replace('/login')
      }
      if (response?.data?.description !== '') {
        Message.warning(response.data.description)
      } else if (response?.data?.message !== '') {
        Message.warning(response.data.message)
      } else {
        Message.warning('哎呀, 出错了')
      }
      return Promise.reject(response.data)
    }
    return Promise.resolve(response)
  },
  function (error) {
    // 超出 2xx 范围的状态码都会触发该函数。
    // 对响应错误做点什么
    Message.warning('哎呀, 出错了')
    return Promise.reject(error)
  }
)

export default instance
