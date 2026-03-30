import request from '@/utils/request'

// 获取数据看板
export function getDashboardStats() {
  return request({
    url: '/api/stats/dashboard',
    method: 'get'
  })
}

// 获取用户统计
export function getUserStats(params?: { days?: number }) {
  return request({
    url: '/api/stats/users',
    method: 'get',
    params
  })
}

// 获取任务统计
export function getTaskStats() {
  return request({
    url: '/api/stats/tasks',
    method: 'get'
  })
}
