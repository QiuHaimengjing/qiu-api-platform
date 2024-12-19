import { useUserStore } from '@/stores'
import { UserRole } from '@/ts/enums/UserRoleEnum'
import { Message } from '@arco-design/web-vue'
import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'layout',
      redirect: '/home',
      component: () => import('../views/layouts/LayoutPage.vue'),
      children: [
        {
          path: '/home',
          name: '主页',
          component: () => import('../views/HomePage.vue'),
          meta: { access: [UserRole.USER, UserRole.ADMIN] }
        },
        {
          path: '/login',
          name: 'login',
          component: () => import('../views/user/LoginPage.vue'),
          meta: { access: [UserRole.USER, UserRole.ADMIN] }
        },
        {
          path: '/interface-management',
          name: '接口管理',
          component: () => import('../views/interfaceInfo/InterfaceManagePage.vue'),
          meta: { access: [UserRole.ADMIN] }
        },
        {
          path: '/usercenter',
          name: '个人中心',
          component: () => import('../views/user/UserCenterPage.vue'),
          meta: { access: [UserRole.USER, UserRole.ADMIN] }
        },
        {
          path: '/statistic',
          name: '统计分析',
          component: () => import('../views/statistic/StatisticPage.vue'),
          meta: { access: [UserRole.ADMIN] }
        }
      ]
    },
    {
      path: '/interface-invoke/:id',
      name: '接口调用',
      component: () => import('../views/interfaceInfo/InterfaceInvokePage.vue')
    }
  ]
})

router.beforeEach(async (to, from) => {
  const userStore = useUserStore()
  // 需要拦截的页面数组
  const interceptPages = ['/interface-management']
  // 如果用户未登录且访问的是拦截页面数组中的页面，则重定向到登录页面
  if (!userStore.isLogin && interceptPages.includes(to.path)) {
    Message.error('你没有权限！')
    return '/login'
  }
})

export default router
