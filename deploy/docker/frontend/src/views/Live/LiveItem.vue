<template>
  <div class="live-item" @click="$emit('click')">
    <div class="live-item__cover">
      <img :src="live.previewUrl || '/default-live.png'" :alt="live.title" />
      <van-tag v-if="live.status === 'LIVE'" type="danger" class="live-tag">直播中</van-tag>
      <van-tag v-else-if="live.status === 'PREPARING'" type="primary">准备中</van-tag>
    </div>
    <div class="live-item__info">
      <div class="live-item__title">{{ live.title }}</div>
      <div class="live-item__meta">
        <span class="live-item__author">{{ live.userName }}</span>
        <span class="live-item__viewers">
          <van-icon name="play-circle-o" /> {{ live.viewerCount }}
        </span>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
interface Live {
  id: number
  title: string
  userName: string
  previewUrl: string
  viewerCount: number
  status: string
}

defineProps<{
  live: Live
}>()

defineEmits<{
  click: []
}>()
</script>

<style scoped>
.live-item {
  background: #fff;
  margin-bottom: 12px;
  border-radius: 8px;
  overflow: hidden;
}

.live-item__cover {
  position: relative;
  height: 160px;
}

.live-item__cover img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.live-tag {
  position: absolute;
  top: 8px;
  left: 8px;
}

.live-item__info {
  padding: 12px;
}

.live-item__title {
  font-size: 14px;
  font-weight: 500;
  color: #333;
  margin-bottom: 8px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.live-item__meta {
  display: flex;
  justify-content: space-between;
  font-size: 12px;
  color: #999;
}

.live-item__viewers {
  display: flex;
  align-items: center;
  gap: 4px;
}
</style>
