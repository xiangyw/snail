import request from '@/utils/request'

export interface LiveListParams {
  page: number
  pageSize: number
  status?: string
  userId?: number
}

export interface LiveDetail {
  id: number
  title: string
  streamKey: string
  streamUrl: string
  previewUrl: string
  userId: number
  userName: string
  status: string
  viewerCount: number
  startTime: string
  endTime: string
  createdAt: string
}

/**
 * 获取直播列表
 */
export function getLiveList(params: LiveListParams) {
  return request({
    url: '/api/live',
    method: 'get',
    params
  })
}

/**
 * 获取直播详情
 */
export function getLiveDetail(id: number) {
  return request({
    url: `/api/live/${id}`,
    method: 'get'
  })
}

/**
 * 创建直播
 */
export function createLive(data: {
  title: string
  streamUrl: string
  previewUrl?: string
}) {
  return request({
    url: '/api/live',
    method: 'post',
    data
  })
}

/**
 * 更新直播状态
 */
export function updateLiveStatus(id: number, status: string) {
  return request({
    url: `/api/live/${id}/status`,
    method: 'put',
    data: { status }
  })
}

/**
 * 更新观众计数
 */
export function updateViewerCount(id: number) {
  return request({
    url: `/api/live/${id}/viewer`,
    method: 'post'
  })
}

/**
 * 删除直播
 */
export function deleteLive(id: number) {
  return request({
    url: `/api/live/${id}`,
    method: 'delete'
  })
}

/**
 * 获取我的直播
 */
export function getMyLives(params: LiveListParams) {
  return request({
    url: '/api/live/my',
    method: 'get',
    params
  })
}
