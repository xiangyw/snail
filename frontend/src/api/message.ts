import request from '@/utils/request'

// 获取消息列表
export function getMessageList(params?: { type?: string; page?: number; size?: number }) {
  return request({
    url: '/api/messages',
    method: 'get',
    params
  })
}

// 标记已读
export function markAsRead(id: number) {
  return request({
    url: `/api/messages/${id}/read`,
    method: 'post'
  })
}

// 获取通知列表
export function getNotificationList(params?: { page?: number; size?: number }) {
  return request({
    url: '/api/notifications',
    method: 'get',
    params
  })
}
