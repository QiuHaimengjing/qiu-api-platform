<script setup lang="ts">
import LayoutFooter from './LayoutFooter.vue'
import { useRouter, useRoute } from 'vue-router'
import { useUserStore } from '@/stores'
import ResponseCode from '@/ts/constant/ResponseCode'
import { Message } from '@arco-design/web-vue'
import { inject, ref, watch } from 'vue'
import { UserRole } from '@/ts/enums/UserRoleEnum'
import { userLogoutService } from '@/services/qiuapi-backend/user'
import backgroundImg from '@/assets/images/backimg.jpg'
import defaultAvatar from '@/assets/images/kiana.jpg'

const reload = inject('reload') as Function
const userStore = useUserStore()
const router = useRouter()
const route = useRoute()

const logout = async (): Promise<void> => {
  const res = await userLogoutService()
  if (res.data.code !== ResponseCode.SUCCESS) {
    return
  }
  userStore.removeUserInfo()
  Message.success('退出登录成功')
  reload()
}

type MenuList = {
  path: string
  name?: string
  access?: number[]
}

// 获取菜单列表
const menuList = ref<MenuList[] | void>()
const getMenuList = () => {
  menuList.value = route.matched[0].children
    .map((item) => {
      return {
        path: item.path,
        name: item.name?.toString(),
        access: item.meta?.access as number[] | undefined
      }
    })
    .filter((item) => item.path !== '/login')
  menuList.value = menuList.value?.filter((item) => {
    if (!item.access?.includes(UserRole.USER) && userStore.userInfo.userRole !== UserRole.ADMIN) {
      return false
    }
    return true
  })
}
getMenuList()

// 监控当前路由
const currentRoute = ref<string[]>([route.name as string])
watch(route, () => {
  currentRoute.value[0] = route.name as string
})
</script>

<template>
  <div class="menu-demo">
    <a-menu
      mode="horizontal"
      :default-selected-keys="route.path === '/login' ? [''] : currentRoute"
      :selected-keys="route.path === '/login' ? [''] : currentRoute"
    >
      <a-menu-item
        key="0"
        :style="{ padding: 0, marginRight: '38px' }"
        style="pointer-events: none"
      >
        <a-image width="60" height="30" style="pointer-events: none" :src="backgroundImg" />
        <span class="title">邱海梦旌的API平台</span>
      </a-menu-item>
      <a-menu-item v-for="item in menuList" :key="item.name" @click="router.push(item.path)">{{
        item.name
      }}</a-menu-item>
      <a-menu-item class="right" disabled style="cursor: pointer">
        <a-button type="primary" @click="router.push('/login')" v-if="!userStore.isLogin"
          >登录</a-button
        >
        <div v-else>
          <a-popover position="bottom" style="width: 10rem">
            <a-avatar :size="30">
              <img :src="userStore.userInfo.avatarUrl || defaultAvatar" />
            </a-avatar>
            <template #title>
              <p class="username">{{ userStore.userInfo?.username }}</p>
            </template>
            <template #content>
              <a-divider />
              <a-button style="width: 100%" @click="logout">退出登录</a-button>
            </template>
          </a-popover>
        </div>
      </a-menu-item>
    </a-menu>
  </div>
  <RouterView />
  <LayoutFooter />
</template>

<style scoped lang="less">
.menu-demo {
  box-sizing: border-box;
  width: 100%;
  padding: 1rem;
  background-color: var(--color-neutral-2);
}
.title {
  font-size: 1rem;
  margin-left: 0.5rem;
  color: var(--color-neutral-10);
  font-weight: bold;
}
.right {
  float: right;
  margin-right: 2rem;
}
.username {
  text-align: center;
  text-overflow: ellipsis;
  overflow: hidden;
  word-break: break-all;
  white-space: nowrap;
}
</style>
