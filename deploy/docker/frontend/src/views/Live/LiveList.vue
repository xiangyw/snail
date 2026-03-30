<template>
  <div class="live-list">
    <van-nav-bar title="直播列表" />
    
    <van-list
      v-model:loading="loading"
      :finished="finished"
      finished-text="没有更多了"
      @load="onLoad"
    >
      <live-item
        v-for="live in liveList"
        :key="live.id"
        :live="live"
        @click="goToLiveRoom(live.id)"
      />
      
      <van-empty v-if="liveList.length === 0 && !loading" description="暂无直播" />
    </van-list>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { showToast } from 'vant'
import LiveItem from './LiveItem.vue'
import { getLiveList } from '@/api/live'

interface Live {
  id: number
  title: string
  userName: string
  previewUrl: string
  viewerCount: number
  status: 'PREPARING' | 'LIVE' | 'FINISHED' | 'CANCELLED'
  createdAt: string
}

const router = useRouter()
const loading = ref(false)
const finished = ref(false)
const liveList = ref<Live[]>([])
const page = ref(1)
const pageSize = 10

const onLoad = async () => {
  try {
    const res = await getLiveList({
      page: page.value,
      pageSize: pageSize.value,
      status: 'LIVE'
    })
    
    if (page.value === 1) {
      liveList.value = res.data.list
    } else {
      liveList.value = [...liveList.value, ...res.data.list]
    }
    
    page.value++
    loading.value = false
    
    if (liveList.value.length >= res.data.total) {
      finished.value = true
    }
  } catch (error) {
    showToast('加载失败')
    loading.value = false
    finished.value = true
  }
}

const goToLiveRoom = (id: number) => {
  router.push(`/live/${id}`)
}

const refresh = async () => {
  page.value = 1
  finished.value = false
  liveList.value = []
  await onLoad()
}

onMounted(() => {
  onLoad()
})

defineExpose({
  refresh
})
</script>

<style scoped>
.live-list {
  min-height: 100vh;
  background-color: #f5f5f5;
}
</style>
