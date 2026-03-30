<template>
  <div class="stats-dashboard">
    <van-nav-bar title="数据统计" />
    
    <!-- 数据概览 -->
    <van-grid :column-num="2" :border="false" class="stats-grid">
      <van-grid-item :value="stats.totalUsers" text="总用户数" icon="friends-o" />
      <van-grid-item :value="stats.totalTasks" text="总任务数" icon="todo-list-o" />
      <van-grid-item :value="stats.totalPoints" text="总积分" icon="gold-coin-o" />
      <van-grid-item :value="stats.totalRevenue" text="总收入" icon="money-o" />
    </van-grid>

    <!-- 用户趋势图 -->
    <van-cell-group title="用户增长趋势" class="mt-16">
      <div ref="userChartRef" class="chart"></div>
    </van-cell-group>

    <!-- 任务统计 -->
    <van-cell-group title="任务统计" class="mt-16">
      <div ref="taskChartRef" class="chart"></div>
    </van-cell-group>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { getDashboardStats, getUserStats, getTaskStats } from '@/api/stats'
import * as echarts from 'echarts'

const stats = reactive({
  totalUsers: 0,
  totalTasks: 0,
  totalPoints: 0,
  totalRevenue: 0
})

const userChartRef = ref(null)
const taskChartRef = ref(null)

onMounted(async () => {
  const data = await getDashboardStats()
  stats.totalUsers = data.totalUsers
  stats.totalTasks = data.totalTasks
  stats.totalPoints = data.totalPoints
  stats.totalRevenue = data.totalRevenue

  initCharts()
})

const initCharts = async () => {
  const userData = await getUserStats()
  const taskData = await getTaskStats()

  // 用户趋势图
  const userChart = echarts.init(userChartRef.value)
  userChart.setOption({
    xAxis: {
      type: 'category',
      data: userData.dates
    },
    yAxis: {
      type: 'value'
    },
    series: [{
      data: userData.counts,
      type: 'line',
      smooth: true
    }]
  })

  // 任务统计图
  const taskChart = echarts.init(taskChartRef.value)
  taskChart.setOption({
    xAxis: {
      type: 'category',
      data: taskData.statusLabels
    },
    yAxis: {
      type: 'value'
    },
    series: [{
      data: taskData.counts,
      type: 'bar'
    }]
  })
}
</script>

<style scoped>
.stats-grid {
  background: #fff;
}

.mt-16 {
  margin-top: 16px;
}

.chart {
  height: 300px;
  width: 100%;
}
</style>
