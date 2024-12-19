<script setup lang="ts">
import { nextTick, provide, ref } from 'vue'
import echarts from './config/echarts'

provide('echarts', echarts)

// 用于刷新路由
const isRouterAlive = ref<boolean>(true)
const reload = (): void => {
  isRouterAlive.value = false
  nextTick(() => {
    isRouterAlive.value = true
  })
}
provide('reload', reload)
</script>

<template>
  <RouterView v-if="isRouterAlive" />
</template>

<style scoped lang="less"></style>
