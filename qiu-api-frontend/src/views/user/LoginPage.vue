<script setup lang="ts">
import type { API } from '@/services/qiuapi-backend/typings'
import ResponseCode from '@/ts/constant/ResponseCode'
import { Message } from '@arco-design/web-vue'
import { inject, ref } from 'vue'
import { useUserStore } from '@/stores'
import { useRouter } from 'vue-router'
import { userLoginService, userRegisterService } from '@/services/qiuapi-backend/user'

const reload = inject('reload') as Function
const router = useRouter()
const userStore = useUserStore()
const isLogin = ref<boolean>(true)
const loginParams = ref<API.UserLoginRequest>({
  userAccount: '',
  userPassword: ''
})
const registerParams = ref<API.UserRegisterRequest>({
  userAccount: '',
  userPassword: '',
  verifyPassword: ''
})
const rules = {
  userAccount: [
    {
      required: true,
      message: '用户名不为空'
    },
    {
      minLength: 4,
      message: '用户名长度至少为4位'
    },
    {
      match: /^[a-zA-Z0-9_]+$/,
      message: '用户名只能包含字母、数字和下划线'
    }
  ],
  userPassword: [
    {
      required: true,
      message: '密码不为空'
    },
    {
      minLength: 6,
      message: '密码长度至少为6位'
    }
  ],
  verifyPassword: [
    {
      required: true,
      message: '确认密码不为空'
    },
    {
      minLength: 6,
      message: '密码长度至少为6位'
    },
    {
      validator: (value: string, callback: (error?: string) => void) => {
        if (value !== registerParams.value.userPassword) {
          callback('两次输入密码不一致')
        } else {
          callback()
        }
      },
      message: '两次输入的密码不一致'
    }
  ]
}
// 用户登录
const userLogin = async ({ values, errors }: any): Promise<void> => {
  if (errors) {
    return
  }
  const res = await userLoginService(values)
  if (res.data.code !== ResponseCode.SUCCESS) {
    return
  }
  Message.success('登录成功')
  if (res.data.data) {
    userStore.setUserInfo(res.data.data)
  }
  router.push('/')
  reload()
}
// 用户注册
const userRegister = async ({ values, errors }: any): Promise<void> => {
  if (errors) {
    return
  }
  console.log(values)

  const res = await userRegisterService(values)
  if (res.data.code !== ResponseCode.SUCCESS) {
    return
  }
  Message.success('注册成功，请返回登录')
  isLogin.value = true
}
</script>

<template>
  <div
    :style="{
      display: 'flex',
      width: '50%',
      boxSizing: 'border-box',
      padding: '40px',
      backgroundColor: 'var(--color-fill-2)'
    }"
    class="box"
  >
    <a-card :style="{ width: '100%' }" :bordered="false" v-if="isLogin">
      <a-alert banner center>如果注册过邱的其他项目，可以使用原本账号登录</a-alert>
      <a-form
        ref="formRef"
        :rules="rules"
        :model="loginParams"
        :style="{ width: '37.5rem' }"
        @submit="userLogin"
        style="margin-top: 2rem; width: 100%"
      >
        <a-form-item field="userAccount" label="用户名" validate-trigger="blur" feedback>
          <a-input v-model="loginParams.userAccount" placeholder="请输入用户名" />
        </a-form-item>
        <a-form-item field="userPassword" label="密码" validate-trigger="blur">
          <a-input-password v-model="loginParams.userPassword" placeholder="请输入密码" />
        </a-form-item>
        <a-form-item>
          <a-space>
            <a-button type="primary" html-type="submit">登录</a-button>
            <a-button type="outline" @click="isLogin = false">去注册</a-button>
          </a-space>
        </a-form-item>
      </a-form>
    </a-card>

    <a-card :style="{ width: '100%' }" :bordered="false" v-if="!isLogin">
      <a-alert banner center>如果注册过邱的其他项目，可以使用原本账号登录</a-alert>
      <a-form
        ref="formRef"
        :rules="rules"
        :model="registerParams"
        :style="{ width: '37.5rem' }"
        @submit="userRegister"
        style="margin-top: 2rem; width: 100%"
      >
        <a-form-item field="userAccount" label="用户名" validate-trigger="blur" feedback>
          <a-input v-model="registerParams.userAccount" placeholder="请输入用户名" />
        </a-form-item>
        <a-form-item field="userPassword" label="密码" validate-trigger="blur">
          <a-input-password v-model="registerParams.userPassword" placeholder="请输入密码" />
        </a-form-item>
        <a-form-item field="verifyPassword" label="确认密码" validate-trigger="blur">
          <a-input-password v-model="registerParams.verifyPassword" placeholder="请输入密码" />
        </a-form-item>
        <a-form-item>
          <a-space>
            <a-button type="primary" html-type="submit">注册</a-button>
            <a-button type="outline" @click="isLogin = true">去登录</a-button>
          </a-space>
        </a-form-item>
      </a-form>
    </a-card>
  </div>
</template>

<style scoped lang="less">
.box {
  // 居中
  margin: 0 auto;
  margin-top: 10%;
  border-radius: 8px;
  box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.1);
}
</style>
