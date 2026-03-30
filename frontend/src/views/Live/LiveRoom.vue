<template>
  <div class="live-room">
    <van-nav-bar
      :title="liveInfo?.title || '直播间'"
      left-arrow
      @click-left="goBack"
    />
    
    <div class="live-room__video">
      <div v-if="liveInfo?.status === 'LIVE'" class="video-player">
        <video
          ref="videoRef"
          :src="liveInfo?.streamUrl"
          autoplay
          playsinline
          class="video-element"
        />
      </div>
      <van-empty v-else description="直播已结束" />
    </div>
    
    <div class="live-room__info">
      <div class="live-room__author">
        <van-image round :src="authorAvatar" class="author-avatar" />
        <div class="author-info">
          <div class="author-name">{{ liveInfo?.userName }}</div>
          <div class="viewer-count">
            <van-icon name="play-circle-o" /> {{ viewerCount }} 人观看
          </div>
        </div>
      </div>
    </div>
    
    <live-chat
      ref="chatRef"
      :room-id="liveId"
      @send-message="handleSendMessage"
    />
    
    <gift-panel
      :room-id="liveId"
      @send-gift="handleSendGift"
    />
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, onUnmounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { showToast } from 'vant'
import LiveChat from './LiveChat.vue'
import GiftPanel from './GiftPanel.vue'
import { getLiveDetail, updateViewerCount } from '@/api/live'
import { sendMessage } from '@/api/chat'

const router = useRouter()
const route = useRoute()
const liveId = ref<number>(Number(route.params.id))

const liveInfo = ref<any>(null)
const viewerCount = ref(0)
const videoRef = ref<HTMLVideoElement>()
const chatRef = ref<InstanceType<typeof LiveChat>>()
const authorAvatar = '/default-avatar.png'

const loadLiveDetail = async () => {
  try {
    const res = await getLiveDetail(liveId.value)
    liveInfo.value = res.data
    viewerCount.value = res.data.viewerCount || 0
  } catch (error) {
    showToast('加载失败')
  }
}

const handleSendMessage = async (content: string) => {
  try {
    await sendMessage({
      roomId: liveId.value,
      content
    })
  } catch (error) {
    showToast('发送失败')
  }
}

const handleSendGift = async (giftId: number, quantity: number) => {
  try {
    // 调用礼物 API
    showToast('礼物赠送成功')
  } catch (error) {
    showToast('赠送失败')
  }
}

const goBack = () => {
  router.back()
}

onMounted(() => {
  loadLiveDetail()
  // 更新观众计数
  updateViewerCount(liveId.value)
})

onUnmounted(() => {
  // 清理资源
})
</script>

<style scoped>
.live-room {
  min-height: 100vh;
  background: #000;
}

.live-room__video {
  width: 100%;
  background: #000;
}

.video-player {
  width: 100%;
  aspect-ratio: 16/9;
}

.video-element {
  width: 100%;
  height: 100%;
  object-fit: contain;
}

.live-room__info {
  background: #fff;
  padding: 12px;
}

.live-room__author {
  display: flex;
  align-items: center;
  gap: 12px;
}

.author-avatar {
  width: 48px;
  height: 48px;
}

.author-info {
  flex: 1;
}

.author-name {
  font-size: 14px;
  font-weight: 500;
  color: #333;
  margin-bottom: 4px;
}

.viewer-count {
  font-size: 12px;
  color: #999;
  display: flex;
  align-items: center;
  gap: 4px;
}
</style>
