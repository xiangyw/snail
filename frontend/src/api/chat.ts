import request from '@/utils/request'

export interface ChatMessage {
  id: number
  roomId: number
  userId: number
  userName: string
  content: string
  avatar?: string
  type: 'USER' | 'SYSTEM' | 'GIFT'
  createdAt: string
}

export interface ChatHistoryParams {
  roomId: number
  page: number
  pageSize: number
}

/**
 * 获取聊天历史
 */
export function getChatHistory(roomId: number, params?: { page?: number; pageSize?: number }) {
  return request({
    url: `/api/chat/${roomId}/history`,
    method: 'get',
    params
  })
}

/**
 * 发送消息
 */
export function sendMessage(data: {
  roomId: number
  content: string
}) {
  return request({
    url: '/api/chat/message',
    method: 'post',
    data
  })
}

/**
 * 删除消息 (管理员)
 */
export function deleteMessage(messageId: number) {
  return request({
    url: `/api/chat/message/${messageId}`,
    method: 'delete'
  })
}

/**
 * 禁言用户 (管理员/主播)
 */
export function muteUser(roomId: number, userId: number, duration: number) {
  return request({
    url: `/api/chat/${roomId}/mute`,
    method: 'post',
    data: { userId, duration }
  })
}
