<template>
  <div class="message-list">
    <van-list
      v-model:loading="loading"
      :finished="finished"
      finished-text="没有更多了"
      @load="onLoad"
    >
      <van-cell
        v-for="msg in messages"
        :key="msg.id"
        :title="msg.title"
        :label="msg.content"
        :value="msg.isRead ? '' : '新'"
        @click="markAsRead(msg.id)"
      >
        <template #label>
          <div class="message-info">
            <span>{{ msg.content }}</span>
            <span class="time">{{ formatTime(msg.createdAt) }}</span>
          </div>
        </template>
      </van-cell>
    </van-list>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { getMessageList, markAsRead as apiMarkAsRead } from '@/api/message'

const props = defineProps<{
  type: 'all' | 'unread' | 'system'
}>()

const messages = ref([])
const loading = ref(false)
const finished = ref(false)

const onLoad = async () => {
  const data = await getMessageList({ type: props.type })
  messages.value = [...messages.value, ...data.list]
  loading.value = false
  finished.value = data.finished
}

const markAsRead = async (id: number) => {
  await apiMarkAsRead(id)
}

const formatTime = (time: string) => {
  return new Date(time).toLocaleString('zh-CN')
}
</script>

<style scoped>
.message-info {
  display: flex;
  justify-content: space-between;
  font-size: 12px;
  color: #969799;
}

.time {
  font-size: 12px;
  color: #969799;
}
</style>
