import request from '@/utils/request'

// 获取邀请码
export function getInviteCode() {
  return request({
    url: '/api/invite/code',
    method: 'get'
  })
}

// 获取邀请记录
export function getInviteRecords(params?: { page?: number; size?: number }) {
  return request({
    url: '/api/invite/records',
    method: 'get',
    params
  })
}

// 获取推广统计
export function getPromoStats() {
  return request({
    url: '/api/promo/stats',
    method: 'get'
  })
}
