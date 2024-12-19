<script setup lang="ts">
import type { API } from '@/services/qiuapi-backend/typings'
import {
  invokeInterfaceService,
  getInterfaceByIdService
} from '@/services/qiuapi-backend/interfaceInfo'
import {
  addUserInterfaceService,
  getUserInterfaceByIdService
} from '@/services/qiuapi-backend/userInterface'
import ResponseCode from '@/ts/constant/ResponseCode'
import { Message } from '@arco-design/web-vue'
import { ref } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { dateFormat } from '@/utils/dateFormat'
import { useUserStore } from '@/stores'

const userStore = useUserStore()
const router = useRouter()
const route = useRoute()
const loading = ref<boolean>(true)

// 接口信息
const requestParams = ref<string | undefined>('')
const interfaceInfo = ref<API.InterfaceVO>({
  id: 0,
  name: '',
  description: '',
  url: '',
  requestParams: '',
  method: '',
  requestHeader: '',
  responseHeader: '',
  status: 0
})
let data: any = []

// 获取用户与接口的关系
const userInterfaceInfo = ref<API.UserInterfaceVO>()
const getUserInterfaceInfo = async (): Promise<void> => {
  const id = { id: Number(route.params.id) }
  const res = await getUserInterfaceByIdService(id)
  if (res.data.data == null) {
    return
  }
  if (res.data.code !== ResponseCode.SUCCESS) {
    return
  }
  userInterfaceInfo.value = res.data.data
  userInterfaceInfo.value.createTime = dateFormat(userInterfaceInfo.value.createTime)
  userInterfaceInfo.value.updateTime = dateFormat(userInterfaceInfo.value.updateTime)
}

// 获取接口信息
const getInterfaceInfo = async (): Promise<void> => {
  const res = await getInterfaceByIdService({ id: Number(route.params.id) })
  if (res.data.code !== ResponseCode.SUCCESS) {
    return
  }
  if (res.data.data) {
    interfaceInfo.value = res.data.data
    interfaceInfo.value.createTime = dateFormat(interfaceInfo.value.createTime)
    data = [
      {
        label: '接口状态',
        value: interfaceInfo.value.status === 1 ? '启用' : '关闭'
      },
      {
        label: '描述',
        value: interfaceInfo.value.description
      },
      {
        label: '请求地址',
        value: interfaceInfo.value.url
      },
      {
        label: '请求方法',
        value: interfaceInfo.value.method
      },
      {
        label: '请求参数',
        value: interfaceInfo.value.requestParams
      },
      {
        label: '请求头',
        value: interfaceInfo.value.requestHeader
      },
      {
        label: '响应头',
        value: interfaceInfo.value.responseHeader
      },
      {
        label: '创建时间',
        value: interfaceInfo.value.createTime
      }
    ]
  }
  await getUserInterfaceInfo()
  requestParams.value = interfaceInfo.value.requestParams
  loading.value = false
}
getInterfaceInfo()

// 调用接口
const responseResult = ref<any>()
// 重置接口请求参数
const resetParams = (): void => {
  requestParams.value = interfaceInfo.value.requestParams
}
// 调用接口
const responseLoading = ref<boolean>(false)
const interfaceInvokeRequest = ref<API.InterfaceInvokeRequest>({
  id: 0,
  requestParams: ''
})
const invokeInterface = async (): Promise<void> => {
  responseLoading.value = true
  interfaceInvokeRequest.value.id = interfaceInfo.value.id ?? 0
  interfaceInvokeRequest.value.requestParams = requestParams.value
  const res = await invokeInterfaceService(interfaceInvokeRequest.value)
  if (res.data.code !== ResponseCode.SUCCESS) {
    return
  }
  responseLoading.value = false
  Message.success('调用成功')
  await getUserInterfaceInfo()
  responseResult.value = res.data.data
}

// 获取权限
const getInvokePermissions = async (): Promise<void> => {
  const params: API.UserInterfaceAddRequest = {
    interfaceId: Number(route.params.id),
    userId: userStore.userInfo.id ?? 0
  }
  const res = await addUserInterfaceService(params)
  if (res.data.code !== ResponseCode.SUCCESS) {
    return
  }
  Message.success('获取权限成功')
  await getUserInterfaceInfo()
}
</script>

<template>
  <a-spin v-if="loading" class="loading" :size="25" />
  <div class="app" v-else>
    <div :style="{ background: 'var(--color-fill-2)', padding: '20px' }">
      <a-page-header
        :style="{ background: 'var(--color-bg-2)' }"
        title="接口文档"
        :subtitle="interfaceInfo.name"
        @back="router.back()"
      >
      </a-page-header>
    </div>
    <div class="user-interface">
      <div class="interface">
        <a-descriptions style="margin-top: 1.25rem" :data="data" size="large" :column="1">
          <template #title>
            <div class="title">{{ interfaceInfo.name }}</div>
          </template>
        </a-descriptions>
      </div>
      <a-divider direction="vertical" style="margin-top: 1rem" />
      <div class="user">
        <div class="user-interface-card">
          <div class="title">接口授权信息</div>
          <a-descriptions style="margin-top: 20px" :column="1">
            <a-descriptions-item>
              <template #label>权限：</template>
              <a-badge
                status="normal"
                text="无权限"
                v-if="userInterfaceInfo?.status === 1 || userInterfaceInfo?.status === undefined"
              />
              <a-badge status="success" text="已授权" v-else />
            </a-descriptions-item>
            <a-descriptions-item v-if="userInterfaceInfo?.status === 0">
              <template #label>总调用次数</template>
              {{ userInterfaceInfo?.totalNum }}
            </a-descriptions-item>
            <a-descriptions-item v-if="userInterfaceInfo?.status === 0">
              <template #label>剩余调用次数</template>
              {{ userInterfaceInfo?.leftNum }}
            </a-descriptions-item>
            <a-descriptions-item v-if="userInterfaceInfo?.status === 0">
              <template #label>首次调用</template>
              {{ userInterfaceInfo?.createTime }}
            </a-descriptions-item>
            <a-descriptions-item v-if="userInterfaceInfo?.status === 0">
              <template #label>最后一次调用</template>
              {{ userInterfaceInfo?.updateTime }}
            </a-descriptions-item>
          </a-descriptions>
          <a-button
            type="primary"
            v-if="userInterfaceInfo?.status === 1 || userInterfaceInfo?.status === undefined"
            @click="getInvokePermissions"
            >获取接口权限</a-button
          >
          <a-alert
            closable
            style="margin-top: 1rem"
            v-if="userInterfaceInfo?.status === 1 || userInterfaceInfo?.status === undefined"
            >获取接口权限前你需要先取得API签名认证
            <template #action>
              <a-button size="small" type="primary" @click="router.push('/usercenter')"
                >去获取</a-button
              >
            </template>
          </a-alert>
        </div>
      </div>
    </div>
    <div class="request">
      <div class="textarea">
        <div class="label">请求参数</div>
        <a-textarea
          default-value=""
          v-model="requestParams"
          :auto-size="{
            minRows: 5,
            maxRows: 10
          }"
        />
      </div>
      <a-button type="primary" class="invoke" @click="invokeInterface">调用</a-button>
      <a-button @click="resetParams" style="margin-left: 1rem">重置</a-button>
      <div class="textarea">
        <div class="label">响应结果</div>
        <a-spin
          :loading="true"
          tip="请求中..."
          class="response-loading"
          v-if="responseLoading"
        ></a-spin>
        <a-textarea
          v-else
          default-value=""
          v-model="responseResult"
          :auto-size="{
            minRows: 5,
            maxRows: 10
          }"
          class="response-textarea"
        />
      </div>
    </div>
  </div>
</template>

<style scoped lang="less">
.loading {
  position: absolute;
  top: 50%;
  left: 50%;
}
.user-interface {
  display: flex;
}
.interface {
  margin-left: 8rem;
  width: 40%;
  .title {
    font-size: 1.2rem;
    font-weight: 700;
  }
}
.user {
  margin-left: 2rem;
  width: 60%;
  .title {
    font-size: 1.2rem;
    font-weight: 700;
  }
}
.user-interface-card {
  width: 80%;
  margin-top: 2rem;
  padding: 1.25rem;
  background-color: #fff;
  border-radius: 0.625rem;
  box-shadow: 0 0.125rem 0.25rem rgba(0, 0, 0, 0.1);
  .title {
    font-size: 1.125rem;
    font-weight: bold;
    margin-bottom: 0.625rem;
    color: #333;
  }
}
.textarea {
  margin-top: 1.25rem;
  margin-left: 8rem;
  width: 50%;
  .label {
    font-weight: 700;
    margin-bottom: 1rem;
  }
}
.invoke {
  margin-left: 8rem;
  margin-top: 0.5rem;
}
.response-textarea {
  margin-bottom: 4rem;
}
.response-loading {
  position: relative;
  top: 50%;
  left: 50%;
  margin-bottom: 4rem;
}
</style>
