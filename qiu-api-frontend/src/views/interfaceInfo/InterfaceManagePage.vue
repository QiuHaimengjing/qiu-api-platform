<script setup lang="ts">
import {
  addInterfaceService,
  deleteInterfaceService,
  getInterfaceListService,
  updateInterfaceService
} from '@/services/qiuapi-backend/interfaceInfo'
import type { API } from '@/services/qiuapi-backend/typings'
import ResponseCode from '@/ts/constant/ResponseCode'
import { reactive, ref } from 'vue'
import { InterfaceStatus } from '@/ts/enums/InterfaceStatusEnum'
import { dateFormat } from '@/utils/dateFormat'
import { Message } from '@arco-design/web-vue'
import type { TableRowSelection } from '@arco-design/web-vue'

const loading = ref<boolean>(true)
// 分页
const queryParams = ref<API.InterfaceQueryRequest>({
  pageNO: 1,
  pageSize: 10
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
const total = ref<number>(0)
const interfaceList = ref<API.InterfaceVO[]>([])
const getInrerfaceList = async (): Promise<void> => {
  loading.value = true
  const res = await getInterfaceListService(queryParams.value)
  if (res.data.code !== ResponseCode.SUCCESS) {
    return
  }
  if (res.data.data) {
    interfaceList.value = res.data.data.interfaceList || []
    total.value = res.data.data.total ?? 0
    interfaceList.value.forEach((item) => {
      item.createTime = dateFormat(item.createTime)
      item.updateTime = dateFormat(item.updateTime)
    })
  }
  loading.value = false
}
getInrerfaceList()

// 接口更新信息
const visible = ref<boolean>(false)
const defaultForm = ref<API.InterfaceUpdateRequest>({
  id: 0,
  name: '',
  description: '',
  host: '',
  url: '',
  requestParams: '',
  method: '',
  requestHeader: '',
  responseHeader: '',
  status: 0
})
const interfaceInfo = ref<API.InterfaceUpdateRequest>({ ...defaultForm.value })
const handleUpdate = async (): Promise<void> => {
  // requestParams 转 json
  try {
    interfaceInfo.value.requestParams = JSON.stringify(
      JSON.parse(interfaceInfo.value.requestParams || '{}')
    )
  } catch (e) {
    Message.error('请求参数格式错误')
    return
  }
  const res = await updateInterfaceService(interfaceInfo.value)
  if (res.data.code !== ResponseCode.SUCCESS) {
    return
  }
  Message.success('更新成功')
  await getInrerfaceList()
  visible.value = false
}
const queryInterface = (record: any): void => {
  interfaceInfo.value = { ...record }
  visible.value = true
}

// 删除接口
const ids = ref<number[]>()
const showDelete = ref<boolean>(false)
// 显示弹框
const deleteInterface = (id: number): void => {
  showDelete.value = true
  // 放入数组
  ids.value = [id]
}
// 取消删除
const cancelDelete = (): void => {
  ids.value = []
  showDelete.value = false
}
// 执行删除
const handleDelete = async (): Promise<void> => {
  if (!ids.value) {
    Message.error('删除失败')
    ids.value = []
    showDelete.value = false
    return
  }
  const res = await deleteInterfaceService(ids.value)
  if (res.data?.code !== ResponseCode.SUCCESS) {
    Message.error('删除失败')
    ids.value = []
    showDelete.value = false
    return
  }
  getInrerfaceList()
  ids.value = []
  Message.success('删除成功')
  showDelete.value = false
}

// 选择器
const selectedKeys = ref([])

const rowSelection: TableRowSelection = reactive({
  type: 'checkbox',
  showCheckedAll: true
})

// 创建接口
const showAddInterface = ref<boolean>(false)
const defaultAddInterfaceParams = ref<API.InterfaceAddRequest>({
  description: '',
  host: '',
  method: '',
  name: '',
  requestHeader: '',
  requestParams: '',
  responseHeader: '',
  status: 0,
  url: ''
})
const addInterfaceParams = ref<API.InterfaceAddRequest>({ ...defaultAddInterfaceParams.value })
const addInterface = async (): Promise<void> => {
  const res = await addInterfaceService(addInterfaceParams.value)
  if (res.data.code !== ResponseCode.SUCCESS) {
    return
  }
  Message.success('创建成功')
  await getInrerfaceList()
  showAddInterface.value = false
}
</script>

<template>
  <div class="app">
    <div class="operation">
      <span><a-button type="primary" @click="showAddInterface = true">创建接口</a-button></span>
    </div>
    <!-- 骨架屏 -->
    <a-space direction="vertical" size="large" :style="{ width: '100%' }" v-if="loading">
      <a-skeleton :animation="true" :loading="loading">
        <a-space direction="vertical" :style="{ width: '100%' }" size="large">
          <a-skeleton-line :rows="10" />
        </a-space>
      </a-skeleton>
    </a-space>
    <!-- 表格 -->
    <a-table
      v-else
      :pagination="false"
      row-key="id"
      :data="interfaceList"
      column-resizable
      :row-selection="rowSelection"
      v-model:selectedKeys="selectedKeys"
    >
      <template #columns>
        <a-table-column
          title="接口名称"
          data-index="name"
          align="center"
          :width="100"
        ></a-table-column>
        <a-table-column
          title="接口描述"
          data-index="description"
          ellipsis
          :width="200"
        ></a-table-column>
        <a-table-column title="服务器地址" data-index="host" ellipsis :width="200"></a-table-column>
        <a-table-column title="接口地址" data-index="url" ellipsis :width="200"></a-table-column>
        <a-table-column
          title="请求参数"
          data-index="requestParams"
          ellipsis
          :width="200"
        ></a-table-column>
        <a-table-column
          title="接口类型"
          data-index="method"
          align="center"
          :width="100"
        ></a-table-column>
        <a-table-column
          title="请求头"
          data-index="requestHeader"
          ellipsis
          :width="120"
        ></a-table-column>
        <a-table-column
          title="响应头"
          data-index="responseHeader"
          ellipsis
          :width="120"
        ></a-table-column>
        <a-table-column title="接口状态" data-index="status" :width="90">
          <template #cell="{ record }">
            <a-badge status="normal" text="关闭" v-if="record.status === InterfaceStatus.OFFLINE" />
            <a-badge status="success" text="启用" v-else />
          </template>
        </a-table-column>
        <a-table-column title="创建时间" data-index="createTime" :width="180"></a-table-column>
        <a-table-column title="更新时间" data-index="updateTime" :width="180"></a-table-column>
        <a-table-column title="" align="center" :width="50" fixed="right">
          <template #cell="{ record }">
            <a-button @click="queryInterface(record)">
              <template #icon>
                <icon-search />
              </template>
            </a-button>
          </template>
        </a-table-column>
        <a-table-column title="" :width="80" fixed="right">
          <template #cell="{ record }">
            <a-button type="primary" @click="deleteInterface(record.id)">
              <template #icon>
                <icon-delete />
              </template>
            </a-button>
          </template>
        </a-table-column>
      </template>
    </a-table>
    <a-pagination
      :total="total"
      :current="queryParams.pageNO"
      :page-size="queryParams.pageSize"
      size="small"
      show-total
      show-jumper
      show-page-size
      @change="pageNoChange"
      @page-size-change="pageSizeChange"
      class="pagination"
    />
    <!-- 弹出层表单 -->
    <a-modal
      v-model:visible="visible"
      title="接口信息"
      @cancel="visible = false"
      @before-ok="handleUpdate"
    >
      <a-form :model="interfaceInfo">
        <a-form-item field="name" label="接口名称">
          <a-input v-model="interfaceInfo.name" />
        </a-form-item>
        <a-form-item field="description" label="接口描述">
          <a-input v-model="interfaceInfo.description" />
        </a-form-item>
        <a-form-item field="host" label="服务器地址">
          <a-input v-model="interfaceInfo.host" />
        </a-form-item>
        <a-form-item field="url" label="接口地址">
          <a-input v-model="interfaceInfo.url" />
        </a-form-item>
        <a-form-item field="requestParams" label="请求参数">
          <a-textarea
            v-model="interfaceInfo.requestParams"
            placeholder="请输入请求参数"
            allow-clear
            :auto-size="{
              minRows: 2,
              maxRows: 5
            }"
          />
        </a-form-item>
        <a-form-item field="method" label="接口类型">
          <a-input v-model="interfaceInfo.method" />
        </a-form-item>
        <a-form-item field="requestHeader" label="请求头">
          <a-textarea
            v-model="interfaceInfo.requestHeader"
            placeholder="请输入请求头"
            allow-clear
            :auto-size="{
              minRows: 2,
              maxRows: 5
            }"
          />
        </a-form-item>
        <a-form-item field="responseHeader" label="响应头">
          <a-textarea
            v-model="interfaceInfo.responseHeader"
            placeholder="请输入响应头"
            allow-clear
            :auto-size="{
              minRows: 2,
              maxRows: 5
            }"
          />
        </a-form-item>
        <a-form-item field="status" label="接口状态">
          <a-switch
            v-model="interfaceInfo.status"
            :checked-value="InterfaceStatus.ONLINE"
            :unchecked-value="InterfaceStatus.OFFLINE"
          />
        </a-form-item>
      </a-form>
    </a-modal>
    <!-- 删除提示框 -->
    <a-modal
      :visible="showDelete"
      @ok="handleDelete"
      @cancel="cancelDelete"
      okText="确认"
      cancelText="取消"
      unmountOnClose
    >
      <template #title> 删除接口 </template>
      <div>你确定要删除该接口吗？此操作不可逆</div>
    </a-modal>
    <!-- 创建接口 -->
    <a-modal
      v-model:visible="showAddInterface"
      title="接口信息"
      @cancel="showAddInterface = false"
      @before-ok="addInterface"
    >
      <a-form :model="addInterfaceParams">
        <a-form-item field="name" label="接口名称">
          <a-input v-model="addInterfaceParams.name" />
        </a-form-item>
        <a-form-item field="description" label="接口描述">
          <a-input v-model="addInterfaceParams.description" />
        </a-form-item>
        <a-form-item field="host" label="服务器地址">
          <a-input v-model="addInterfaceParams.host" />
        </a-form-item>
        <a-form-item field="url" label="接口地址">
          <a-input v-model="addInterfaceParams.url" />
        </a-form-item>
        <a-form-item field="requestParams" label="请求参数">
          <a-textarea
            v-model="addInterfaceParams.requestParams"
            placeholder="请输入请求参数"
            allow-clear
            :auto-size="{
              minRows: 2,
              maxRows: 5
            }"
          />
        </a-form-item>
        <a-form-item field="method" label="接口类型">
          <a-input v-model="addInterfaceParams.method" />
        </a-form-item>
        <a-form-item field="requestHeader" label="请求头">
          <a-textarea
            v-model="addInterfaceParams.requestHeader"
            placeholder="请输入请求头"
            allow-clear
            :auto-size="{
              minRows: 2,
              maxRows: 5
            }"
          />
        </a-form-item>
        <a-form-item field="responseHeader" label="响应头">
          <a-textarea
            v-model="addInterfaceParams.responseHeader"
            placeholder="请输入响应头"
            allow-clear
            :auto-size="{
              minRows: 2,
              maxRows: 5
            }"
          />
        </a-form-item>
        <a-form-item field="status" label="接口状态">
          <a-switch
            v-model="addInterfaceParams.status"
            :checked-value="InterfaceStatus.ONLINE"
            :unchecked-value="InterfaceStatus.OFFLINE"
          />
        </a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>

<style scoped lang="less">
.app {
  margin-top: 1rem;
  margin-left: 2rem;
  margin-right: 2rem;
  min-height: 80vh;
}
.operation {
  margin-bottom: 1rem;
  display: flex;
  justify-content: flex-end;
}
.pagination {
  margin-top: 1rem;
  margin-bottom: 2rem;
  display: flex;
  justify-content: center;
}
</style>
