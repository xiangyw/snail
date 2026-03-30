import request from '@/utils/request'

export interface Gift {
  id: number
  name: string
  icon: string
  price: number
  description?: string
}

export interface SendGiftParams {
  roomId: number
  giftId: number
  quantity: number
}

export interface GiftRecord {
  id: number
  roomId: number
  userId: number
  userName: string
  giftId: number
  giftName: string
  quantity: number
  totalPrice: number
  createdAt: string
}

/**
 * 获取礼物列表
 */
export function getGiftList() {
  return request({
    url: '/api/gift',
    method: 'get'
  })
}

/**
 * 赠送礼物
 */
export function sendGift(data: SendGiftParams) {
  return request({
    url: '/api/gift/send',
    method: 'post',
    data
  })
}

/**
 * 获取礼物记录
 */
export function getGiftRecords(roomId: number, params?: { page?: number; pageSize?: number }) {
  return request({
    url: `/api/gift/${roomId}/records`,
    method: 'get',
    params
  })
}

/**
 * 获取我的礼物记录
 */
export function getMyGiftRecords(params?: { page?: number; pageSize?: number }) {
  return request({
    url: '/api/gift/my/records',
    method: 'get',
    params
  })
}
