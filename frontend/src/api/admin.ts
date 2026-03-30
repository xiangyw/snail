import request from '@/utils/request'

// 获取用户列表
export function getUserList(params?: { page?: number; size?: number; keyword?: string }) {
  return request({
    url: '/api/admin/users',
    method: 'get',
    params
  })
}

// 审核内容
export function auditContent(id: number, action: 'PASS' | 'REJECT') {
  return request({
    url: `/api/admin/contents/${id}/audit`,
    method: 'post',
    data: { action }
  })
}

// 获取统计数据
export function getStats() {
  return request({
    url: '/api/admin/stats',
    method: 'get'
  })
}

// 获取内容列表
export function getContentList(params?: { page?: number; size?: number; status?: string }) {
  return request({
    url: '/api/admin/contents',
    method: 'get',
    params
  })
}
