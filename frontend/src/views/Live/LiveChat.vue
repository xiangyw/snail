<template>
  <div class="live-chat">
    <div class="chat-messages" ref="messagesRef">
      <div
        v-for="message in messages"
        :key="message.id"
        class="chat-message"
        :class="{ 'chat-message--system': message.type === 'SYSTEM' }"
      >
        <van-image
          v-if="message.type !== 'SYSTEM'"
          round
          :src="message.avatar || '/default-avatar.png'"
          class="message-avatar"
        />
        <div class="message-content">
          <div v-if="message.type !== 'SYSTEM'" class="message-sender">
            {{ message.userName }}
          </div>
          <div class="message-text">{{ message.content }}</div>
        </div>
      </div>
    </div>
    
    <div class="chat-input">
      <input
        v-model="inputValue"
        type="text"
        placeholder="发送弹幕..."
        class="chat-input__field"
        @keyup.enter="sendMessage"
      />
      <button class="chat-input__btn" @click="sendMessage">
        <van-icon name="send-o" />
      </button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, onUnmounted, nextTick, watch } from 'vue'
import { showToast } from 'vant'
import { getChatHistory, connectChat, disconnectChat } from '@/api/chat'

interface ChatMessage {
  id: number
  roomId: number
  userId: number
  userName: string
  content: string
  avatar?: string
  type: 'USER' | 'SYSTEM' | 'GIFT'
  createdAt: string
}

const props = defineProps<{
  roomId: number
}>()

const emit = defineEmits<{
  sendMessage: [content: string]
}>()

const messages = ref<ChatMessage[]>([])
const inputValue = ref('')
const messagesRef = ref<HTMLElement>()
let ws: WebSocket | null = null

const loadChatHistory = async () => {
  try {
    const res = await getChatHistory(props.roomId)
    messages.value = res.data.list || []
    scrollToBottom()
  } catch (error) {
    console.error('加载聊天历史失败', error)
  }
}

const connectWebSocket = () => {
  try {
    const wsUrl = `ws://localhost:8080/ws/chat/${props.roomId}`
    ws = new WebSocket(wsUrl)
    
    ws.onopen = () => {
      console.log('WebSocket 连接成功')
    }
    
    ws.onmessage = (event) => {
      const message = JSON.parse(event.data)
      messages.value.push(message)
      scrollToBottom()
    }
    
    ws.onerror = (error) => {
      console.error('WebSocket 错误', error)
    }
    
    ws.onclose = () => {
      console.log('WebSocket 连接关闭')
      // 尝试重连
      setTimeout(connectWebSocket, 3000)
    }
  } catch (error) {
    console.error('WebSocket 连接失败', error)
  }
}

const sendMessage = () => {
  if (!inputValue.value.trim()) {
    showToast('请输入内容')
    return
  }
  
  emit('sendMessage', inputValue.value.trim())
  inputValue.value = ''
}

const scrollToBottom = () => {
  nextTick(() => {
    if (messagesRef.value) {
      messagesRef.value.scrollTop = messagesRef.value.scrollHeight
    }
  })
}

onMounted(() => {
  loadChatHistory()
  connectWebSocket()
})

onUnmounted(() => {
  if (ws) {
    ws.close()
  }
})
</script>

<style scoped>
.live-chat {
  display: flex;
  flex-direction: column;
  height: 300px;
  background: #fff;
}

.chat-messages {
  flex: 1;
  overflow-y: auto;
  padding: 12px;
}

.chat-message {
  display: flex;
  gap: 8px;
  margin-bottom: 12px;
}

.chat-message--system {
  justify-content: center;
}

.message-avatar {
  width: 32px;
  height: 32px;
  flex-shrink: 0;
}

.message-content {
  flex: 1;
  max-width: calc(100% - 40px);
}

.message-sender {
  font-size: 12px;
  color: #666;
  margin-bottom: 4px;
}

.message-text {
  font-size: 14px;
  color: #333;
  background: #f5f5f5;
  padding: 8px 12px;
  border-radius: 8px;
  word-wrap: break-word;
}

.chat-message--system .message-text {
  background: transparent;
  color: #999;
  font-size: 12px;
  padding: 4px;
}

.chat-input {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 12px;
  border-top: 1px solid #eee;
  background: #fff;
}

.chat-input__field {
  flex: 1;
  height: 36px;
  padding: 0 12px;
  border: 1px solid #ddd;
  border-radius: 18px;
  font-size: 14px;
  outline: none;
}

.chat-input__field:focus {
  border-color: #1989fa;
}

.chat-input__btn {
  width: 36px;
  height: 36px;
  border: none;
  background: #1989fa;
  color: #fff;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
}

.chat-input__btn:active {
  background: #1678dc;
}
</style>
