<script setup lang="ts">
import type { API } from '@/services/qiuapi-backend/typings'
import { interfaceStatisticService } from '@/services/qiuapi-backend/userInterface'
import ResponseCode from '@/ts/constant/ResponseCode'
import { inject, ref } from 'vue'

// 初始化图表
const echarts = inject<any>('echarts')
// 获取接口调用次数统计数据
const interfaceSta = ref<API.StatisticsVO[] | undefined>()
const getStatisticData = async (): Promise<void> => {
  const res = await interfaceStatisticService({ limit: 3 })
  if (res.data?.code !== ResponseCode.SUCCESS) {
    return
  }
  interfaceSta.value = res.data.data
  const option = {
    title: {
      text: '接口调用统计',
      left: 'center'
    },
    tooltip: {
      trigger: 'item'
    },
    legend: {
      orient: 'vertical',
      left: 'left'
    },
    series: [
      {
        name: 'Access From',
        type: 'pie',
        radius: '50%',
        data: interfaceSta.value?.map((item) => ({
          value: item.totalNum,
          name: item.name
        })),
        emphasis: {
          itemStyle: {
            shadowBlur: 10,
            shadowOffsetX: 0,
            shadowColor: 'rgba(0, 0, 0, 0.5)'
          }
        }
      }
    ]
  }
  const chart = echarts.init(document.getElementById('chart'))
  chart.setOption(option)
}
getStatisticData()
</script>

<template>
  <div class="app">
    <div class="container">
      <h1>统计分析</h1>
      <div class="charts-container">
        <div class="chart" id="chart"></div>
      </div>
    </div>
  </div>
</template>

<style scoped lang="less">
.app {
  min-height: 80vh;
  background-color: #f5f5f5;
}
.container {
  max-width: 95%;
  min-height: 70vh;
  margin: 0 auto;
  padding: 1.25rem;
  background-color: #fff;
  border-radius: 0.5rem;
  box-shadow: 0 0 0.625rem rgba(0, 0, 0, 0.1);
}
h1 {
  text-align: center;
}
.charts-container {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(18.75rem, 1fr));
  grid-gap: 1.25rem;
  justify-content: center;
  align-items: center;
  margin-top: 1.25rem;
}
.chart {
  height: 50vh;
  background-color: #fff;
  border-radius: 0.5rem;
  box-shadow: 0 0 0.3125rem rgba(0, 0, 0, 0.1);
  padding: 1.25rem;
}
</style>
