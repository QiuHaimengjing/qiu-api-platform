<script setup lang="ts">
import { inject, ref } from 'vue'
import { useUserStore } from '@/stores'
import type { API } from '@/services/qiuapi-backend/typings'
import ResponseCode from '@/ts/constant/ResponseCode'
import { Message, Modal, type FileItem } from '@arco-design/web-vue'
import {
  updateUserService,
  getKeyService,
  generateKeyService,
  uploadAvatarService
} from '@/services/qiuapi-backend/user'
import defaultAvatar from '@/assets/images/kiana.jpg'

const reload = inject('reload') as Function
const isEmpty = ref<boolean>(false)

// 获取个人信息
const userStore = useUserStore()
if (!userStore.isLogin) {
  isEmpty.value = true
  Message.warning('未登录')
}
const userInfo = ref<API.UserUpdateRequest>({
  id: 0,
  username: '',
  avatarUrl: '',
  email: '',
  gender: 0,
  phone: '',
  profile: ''
})
userInfo.value = { ...userStore.userInfo, id: userStore.userInfo.id ?? 0 }

// 更新用户信息
const updateUserInfo = async (): Promise<void> => {
  const res = await updateUserService(userInfo.value)
  if (res.data.code !== ResponseCode.SUCCESS) {
    return
  }
  Message.success('保存成功')
  await userStore.getCurrentUser()
  reload()
}

// 获取用户凭证
const accessKey = ref<string>()
const secretKey = ref<string>()
const getCertificate = async (): Promise<void> => {
  const res = await getKeyService()
  if (res.data.code !== ResponseCode.SUCCESS) {
    return
  }
  Message.success('获取签名认证成功')
  accessKey.value = res.data.data?.accessKey
  secretKey.value = res.data.data?.secretKey
}
// 生成凭证
const generateCertificate = (): void => {
  Modal.confirm({
    title: '生成凭证',
    content:
      '确定生成新的签名认证密钥吗？生成密钥后请妥善保管，注意防范泄露，生成成功后需要重新登录才可生效！',
    onOk: async (): Promise<void> => {
      const res = await generateKeyService()
      if (res.data.code !== ResponseCode.SUCCESS) {
        return
      }
      Message.success('生成成功！重新登录后生效')
      accessKey.value = res.data.data?.accessKey
      secretKey.value = res.data.data?.secretKey
    }
  })
}

// 上传头像
const fileList = ref<FileItem[] | undefined>([])
const onChange = (_: any, currentFile: any): void => {
  if (fileList.value === undefined) {
    fileList.value = []
  }
  fileList.value.push(currentFile)
  // 覆盖上一张
  fileList.value = fileList.value.slice(-1)
  userInfo.value.avatarUrl = currentFile.url
}
const uploadAvatar = (): void => {
  Modal.confirm({
    title: '保存头像',
    content: '确定保存头像吗？（每天最多修改5次）',
    onOk: async (): Promise<void> => {
      if (fileList.value === undefined) {
        return
      }
      const file = fileList.value[0]
      const res = await uploadAvatarService(file)
      if (res.data.code !== ResponseCode.SUCCESS) {
        return
      }
      Message.success('保存头像成功')
      await userStore.getCurrentUser()
      reload()
    }
  })
}
</script>

<template>
  <div class="container">
    <a-empty v-if="isEmpty" />
    <div class="container-left" v-if="!isEmpty">
      <div class="profile-header">
        <a-avatar :size="120">
          <img :src="userInfo.avatarUrl || defaultAvatar" />
        </a-avatar>
        <div style="margin-top: 1rem">
          <a-upload
            :auto-upload="false"
            :show-file-list="false"
            @change="onChange"
            :fileList="fileList"
            :limit="2"
          />
          <a-button
            type="primary"
            status="success"
            style="margin-left: 1rem"
            @click="uploadAvatar"
            v-if="fileList && fileList.length > 0"
            >保存头像</a-button
          >
        </div>
      </div>
      <div class="profile-info">
        <div class="profile-name">
          <a-typography-paragraph editable v-model:editText="userInfo.username">
            {{ userInfo.username }}
          </a-typography-paragraph>
        </div>
        <div class="profile-account">
          <span style="color: #87909c">登录账号：</span>
          {{ userStore.userInfo.userAccount }}
        </div>
        <div class="profile-description">
          <a-typography-paragraph editable v-model:editText="userInfo.profile">
            <span style="color: #87909c">简介：</span>
            {{ userInfo.profile }}
          </a-typography-paragraph>
        </div>
        <div class="profile-phone">
          <a-typography-paragraph editable v-model:editText="userInfo.phone">
            <span style="color: #87909c">手机号：</span>
            {{ userInfo.phone }}
          </a-typography-paragraph>
        </div>
        <div class="profile-email">
          <a-typography-paragraph editable v-model:editText="userInfo.email">
            <span style="color: #87909c">邮箱：</span>
            {{ userInfo.email }}
          </a-typography-paragraph>
        </div>
        <a-button type="primary" @click="updateUserInfo">保存</a-button>
      </div>
    </div>
    <div class="container-right" v-if="!isEmpty">
      <a-card title="API签名认证密钥" class="card">
        <template #extra>
          <a-button type="text" @click="getCertificate">查看凭证</a-button>
          <a-button type="text" @click="generateCertificate">生成凭证</a-button>
        </template>
        <span>Access Key：</span>
        <a-typography-paragraph copyable class="certificate">
          {{ accessKey }}
        </a-typography-paragraph>
        <a-divider />
        <span>Secret Key：</span>
        <a-typography-paragraph copyable class="certificate">
          {{ secretKey }}
        </a-typography-paragraph>
      </a-card>
    </div>
  </div>
</template>

<style scoped lang="less">
.container {
  display: flex;
  max-width: 95%;
  min-height: 70vh;
  margin: 1.25rem auto;
  padding: 1.25rem;
  border: 0.0625rem solid #ccc;
  border-radius: 0.3125rem;
  box-shadow: 0 0 0.625rem rgba(0, 0, 0, 0.1);
  .container-left {
    display: flex;
    flex-direction: column;
    justify-content: center;
    width: 60%;
    padding: 1.25rem;
    border-right: 0.0625rem solid #ccc;
  }
  .container-right {
    width: 40%;
    margin-left: 2rem;
    .card {
      margin-top: 2rem;
    }
    .certificate {
      display: inline;
      font-size: 0.8rem;
      color: #1890ff;
    }
  }
}
.profile-header {
  margin-bottom: 1.25rem;
}
.profile-name {
  font-size: 1.5rem;
  margin-bottom: 1rem;
  width: 50%;
}
.profile-account {
  color: #666;
  margin-bottom: 1rem;
  width: 50%;
}
.profile-description {
  color: #666;
  margin-bottom: 1rem;
  width: 50%;
}
.profile-phone {
  color: #666;
  margin-bottom: 1rem;
  width: 50%;
}
.profile-email {
  color: #666;
  width: 50%;
}
</style>
