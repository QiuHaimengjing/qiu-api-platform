<script setup lang="ts">
import { getInterfaceListService } from '@/services/qiuapi-backend/interfaceInfo'
import type { API } from '@/services/qiuapi-backend/typings'
import ResponseCode from '@/ts/constant/ResponseCode'
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const loading = ref<boolean>(true)

// 分页
const total = ref<number>(0)
const queryParams = ref<API.InterfaceQueryRequest>({
  pageNO: 1,
  pageSize: 3
})
const pageNoChange = (current: number): void => {
  queryParams.value.pageNO = current
  getInrerfaceList()
}
const pageSizeChange = (pageSize: number): void => {
  queryParams.value.pageSize = pageSize
  getInrerfaceList()
}
// 获取接口列表
const interfaceList = ref<API.InterfaceVO[]>([])
const getInrerfaceList = async (): Promise<void> => {
  const res = await getInterfaceListService(queryParams.value)
  if (res.data.code !== ResponseCode.SUCCESS) {
    return
  }
  if (res.data.data.interfaceList) {
    interfaceList.value = res.data.data.interfaceList
  }
  if (res.data.data.total) {
    total.value = res.data.data.total
  }
  loading.value = false
}
getInrerfaceList()

const invokeInterface = (id: number | undefined) => {
  router.push(`/interface-invoke/${id}`)
}
</script>

<template>
  <div class="container">
    <h1>API 接口</h1>
    <!-- 骨架屏 -->
    <a-space direction="vertical" size="large" :style="{ width: '100%' }" v-if="loading">
      <a-skeleton :animation="true" :loading="loading">
        <a-space direction="vertical" :style="{ width: '100%' }" size="large">
          <a-skeleton-line :rows="10" />
        </a-space>
      </a-skeleton>
    </a-space>
    <div class="api-card" v-for="item in interfaceList" :key="item.id" v-else>
      <div class="api-name" style="display: inline">{{ item.name }}</div>
      <div class="api-description">
        {{ item.description }}
      </div>
      <a-button type="primary" @click="invokeInterface(item.id)">调用</a-button>
    </div>
    <a-pagination
      :total="total"
      :current="queryParams.pageNO"
      :page-size="queryParams.pageSize"
      :page-size-options="[3, 5, 10]"
      size="small"
      show-total
      show-jumper
      show-page-size
      @change="pageNoChange"
      @page-size-change="pageSizeChange"
      class="pagination"
    />
  </div>
</template>

<style scoped lang="less">
.container {
  max-width: 95%;
  min-height: 75vh;
  margin: 3.125rem auto;
  margin-top: 1rem;
  margin-bottom: 1rem;
  padding: 1.25rem;
  background-color: rgba(255, 255, 255, 0.8);
  border-radius: 0.3125rem;
  box-shadow: 0 0 0.625rem rgba(0, 0, 0, 0.1);
}
h1 {
  text-align: center;
}

.api-card {
  max-width: 90%;
  margin: 1.25rem auto;
  padding: 1.25rem;
  background-color: #fff;
  border-radius: 0.625rem;
  box-shadow: 0 0 0.625rem rgba(0, 0, 0, 0.1);
}
.api-name {
  font-size: 1.25rem;
  font-weight: bold;
  margin-bottom: 0.625rem;
}
.api-description {
  color: #666;
  margin-bottom: 1.25rem;
  margin-top: 1rem;
}
.pagination {
  margin-top: 1rem;
  margin-bottom: 2rem;
  display: flex;
  justify-content: center;
}
</style>
