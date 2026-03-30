<template>
  <div class="tasks-page">
    <van-nav-bar title="任务中心" />
    
    <!-- 任务分类 -->
    <van-tabs v-model:active="activeTab" @change="onTabChange">
      <van-tab title="全部" name="all" />
      <van-tab title="进行中" name="doing" />
      <van-tab title="已完成" name="done" />
    </van-tabs>
    
    <!-- 任务列表 -->
    <van-list
      v-model:loading="loading"
      :finished="finished"
      finished-text="没有更多任务了"
      @load="onLoad"
    >
      <van-cell
        v-for="task in taskList"
        :key="task.id"
        :title="task.title"
        :label="task.description"
        :value="task.status"
        is-link
        @click="goToDetail(task.id)"
      >
        <template #right-icon>
          <van-tag :type="task.status === 'done' ? 'success' : 'primary'">
            {{ task.status === 'done' ? '已完成' : '进行中' }}
          </van-tag>
        </template>
      </van-cell>
    </van-list>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

const activeTab = ref('all')
const loading = ref(false)
const finished = ref(false)
const taskList = ref<any[]>([])

const onTabChange = (tab: string) => {
  activeTab.value = tab
  // 重新加载对应分类的任务
  taskList.value = []
  finished.value = false
  loading.value = true
}

const onLoad = () => {
  // TODO: 调用 API 加载任务列表
  setTimeout(() => {
    taskList.value.push(
      { id: 1, title: '每日签到', description: '完成每日签到任务', status: 'doing' },
      { id: 2, title: '观看直播', description: '观看直播 10 分钟', status: 'doing' },
      { id: 3, title: '分享商品', description: '分享商品到朋友圈', status: 'done' }
    )
    loading.value = false
    if (taskList.value.length >= 10) {
      finished.value = true
    }
  }, 1000)
}

const goToDetail = (id: number) => {
  router.push(`/tasks/${id}`)
}
</script>

<style scoped>
.tasks-page {
  min-height: 100vh;
  background: #f5f5f5;
}

.van-tabs {
  background: #fff;
}

.van-cell {
  margin-top: 8px;
  background: #fff;
}
</style>
