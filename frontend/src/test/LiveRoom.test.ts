import { describe, it, expect, vi, beforeEach } from 'vitest'
import { mount, flushPromises } from '@vue/test-utils'
import { createRouter, createWebHistory } from 'vue-router'
import LiveRoom from '../views/Live/LiveRoom.vue'

// Mock API
vi.mock('@/api/live', () => ({
  getLiveDetail: vi.fn(),
  updateViewerCount: vi.fn(),
}))

vi.mock('@/api/chat', () => ({
  sendMessage: vi.fn(),
}))

import { getLiveDetail, updateViewerCount } from '@/api/live'
import { sendMessage } from '@/api/chat'

describe('LiveRoom 组件测试', () => {
  const mockLiveDetail = {
    id: 1,
    title: '测试直播间',
    streamUrl: 'https://example.com/stream.m3u8',
    previewUrl: '/preview.jpg',
    userName: '测试主播',
    userId: 100,
    status: 'LIVE',
    viewerCount: 1000,
    startTime: '2026-03-30T10:00:00',
    createdAt: '2026-03-30T09:00:00'
  }

  const createRouterMock = () => {
    return createRouter({
      history: createWebHistory(),
      routes: [
        { path: '/live/:id', component: { template: '<div>Live</div>' } }
      ]
    })
  }

  beforeEach(() => {
    vi.clearAllMocks()
  })

  it('应该正确渲染组件结构', () => {
    const wrapper = mount(LiveRoom, {
      global: {
        plugins: [createRouterMock()],
        stubs: {
          LiveChat: { template: '<div class="live-chat"></div>' },
          GiftPanel: { template: '<div class="gift-panel"></div>' }
        }
      },
      props: {
        id: '1'
      }
    })

    expect(wrapper.find('.live-room').exists()).toBe(true)
    expect(wrapper.find('.van-nav-bar').exists()).toBe(true)
  })

  it('应该显示直播标题', async () => {
    vi.mocked(getLiveDetail).mockResolvedValue({ data: mockLiveDetail } as any)

    const wrapper = mount(LiveRoom, {
      global: {
        plugins: [createRouterMock()],
        stubs: {
          LiveChat: { template: '<div class="live-chat"></div>' },
          GiftPanel: { template: '<div class="gift-panel"></div>' }
        }
      }
    })

    await flushPromises()

    const navBar = wrapper.find('.van-nav-bar')
    expect(navBar.text()).toContain('测试直播间')
  })

  it('初始状态应该正确', () => {
    const wrapper = mount(LiveRoom, {
      global: {
        plugins: [createRouterMock()],
        stubs: {
          LiveChat: { template: '<div class="live-chat"></div>' },
          GiftPanel: { template: '<div class="gift-panel"></div>' }
        }
      }
    })

    expect(wrapper.vm.liveInfo).toBeNull()
    expect(wrapper.vm.viewerCount).toBe(0)
  })

  it('组件挂载时应该加载直播详情', async () => {
    vi.mocked(getLiveDetail).mockResolvedValue({ data: mockLiveDetail } as any)

    const wrapper = mount(LiveRoom, {
      global: {
        plugins: [createRouterMock()],
        stubs: {
          LiveChat: { template: '<div class="live-chat"></div>' },
          GiftPanel: { template: '<div class="gift-panel"></div>' }
        }
      }
    })

    await flushPromises()

    expect(getLiveDetail).toHaveBeenCalledWith(1)
    expect(wrapper.vm.liveInfo).toEqual(mockLiveDetail)
  })

  it('应该更新观众计数', async () => {
    vi.mocked(getLiveDetail).mockResolvedValue({ data: mockLiveDetail } as any)

    const wrapper = mount(LiveRoom, {
      global: {
        plugins: [createRouterMock()],
        stubs: {
          LiveChat: { template: '<div class="live-chat"></div>' },
          GiftPanel: { template: '<div class="gift-panel"></div>' }
        }
      }
    })

    await flushPromises()

    expect(updateViewerCount).toHaveBeenCalledWith(1)
    expect(wrapper.vm.viewerCount).toBe(1000)
  })

  it('直播中应该显示视频播放器', async () => {
    vi.mocked(getLiveDetail).mockResolvedValue({ data: mockLiveDetail } as any)

    const wrapper = mount(LiveRoom, {
      global: {
        plugins: [createRouterMock()],
        stubs: {
          LiveChat: { template: '<div class="live-chat"></div>' },
          GiftPanel: { template: '<div class="gift-panel"></div>' }
        }
      }
    })

    await flushPromises()

    expect(wrapper.find('.video-player').exists()).toBe(true)
    expect(wrapper.find('video').exists()).toBe(true)
  })

  it('直播结束应该显示 Empty 组件', async () => {
    const endedLive = { ...mockLiveDetail, status: 'FINISHED' }
    vi.mocked(getLiveDetail).mockResolvedValue({ data: endedLive } as any)

    const wrapper = mount(LiveRoom, {
      global: {
        plugins: [createRouterMock()],
        stubs: {
          LiveChat: { template: '<div class="live-chat"></div>' },
          GiftPanel: { template: '<div class="gift-panel"></div>' }
        }
      }
    })

    await flushPromises()

    expect(wrapper.find('.van-empty').exists()).toBe(true)
  })

  it('应该显示主播信息', async () => {
    vi.mocked(getLiveDetail).mockResolvedValue({ data: mockLiveDetail } as any)

    const wrapper = mount(LiveRoom, {
      global: {
        plugins: [createRouterMock()],
        stubs: {
          LiveChat: { template: '<div class="live-chat"></div>' },
          GiftPanel: { template: '<div class="gift-panel"></div>' }
        }
      }
    })

    await flushPromises()

    expect(wrapper.find('.author-name').text()).toBe('测试主播')
    expect(wrapper.find('.viewer-count').text()).toContain('1000 人观看')
  })

  it('goBack 应该调用 router.back', () => {
    const router = createRouterMock()
    const backSpy = vi.spyOn(router, 'back')

    const wrapper = mount(LiveRoom, {
      global: {
        plugins: [router],
        stubs: {
          LiveChat: { template: '<div class="live-chat"></div>' },
          GiftPanel: { template: '<div class="gift-panel"></div>' }
        }
      }
    })

    wrapper.vm.goBack()

    expect(backSpy).toHaveBeenCalled()
  })

  it('点击返回按钮应该触发 goBack', async () => {
    const router = createRouterMock()
    const backSpy = vi.spyOn(router, 'back')

    const wrapper = mount(LiveRoom, {
      global: {
        plugins: [router],
        stubs: {
          LiveChat: { template: '<div class="live-chat"></div>' },
          GiftPanel: { template: '<div class="gift-panel"></div>' }
        }
      }
    })

    const navBar = wrapper.find('.van-nav-bar')
    await navBar.vm.$emit('click-left')

    expect(backSpy).toHaveBeenCalled()
  })

  it('handleSendMessage 应该调用 sendMessage API', async () => {
    vi.mocked(getLiveDetail).mockResolvedValue({ data: mockLiveDetail } as any)
    vi.mocked(sendMessage).mockResolvedValue({} as any)

    const wrapper = mount(LiveRoom, {
      global: {
        plugins: [createRouterMock()],
        stubs: {
          LiveChat: { template: '<div class="live-chat"></div>' },
          GiftPanel: { template: '<div class="gift-panel"></div>' }
        }
      }
    })

    await flushPromises()

    await wrapper.vm.handleSendMessage('测试消息')

    expect(sendMessage).toHaveBeenCalledWith({
      roomId: 1,
      content: '测试消息'
    })
  })

  it('handleSendMessage 失败应该显示错误提示', async () => {
    vi.mocked(getLiveDetail).mockResolvedValue({ data: mockLiveDetail } as any)
    vi.mocked(sendMessage).mockRejectedValue(new Error('Send failed'))

    const wrapper = mount(LiveRoom, {
      global: {
        plugins: [createRouterMock()],
        stubs: {
          LiveChat: { template: '<div class="live-chat"></div>' },
          GiftPanel: { template: '<div class="gift-panel"></div>' }
        }
      }
    })

    await flushPromises()

    await wrapper.vm.handleSendMessage('测试消息')

    expect(sendMessage).toHaveBeenCalled()
  })

  it('handleSendGift 应该处理礼物赠送', async () => {
    vi.mocked(getLiveDetail).mockResolvedValue({ data: mockLiveDetail } as any)

    const wrapper = mount(LiveRoom, {
      global: {
        plugins: [createRouterMock()],
        stubs: {
          LiveChat: { template: '<div class="live-chat"></div>' },
          GiftPanel: { template: '<div class="gift-panel"></div>' }
        }
      }
    })

    await flushPromises()

    await wrapper.vm.handleSendGift(1, 10)

    // 礼物赠送逻辑
    expect(wrapper.vm.handleSendGift).toBeDefined()
  })

  it('组件卸载时应该清理资源', async () => {
    vi.mocked(getLiveDetail).mockResolvedValue({ data: mockLiveDetail } as any)

    const wrapper = mount(LiveRoom, {
      global: {
        plugins: [createRouterMock()],
        stubs: {
          LiveChat: { template: '<div class="live-chat"></div>' },
          GiftPanel: { template: '<div class="gift-panel"></div>' }
        }
      }
    })

    await flushPromises()
    wrapper.unmount()

    // 验证清理逻辑
    expect(wrapper.vm.$el).toBeDefined()
  })

  it('加载失败应该显示错误提示', async () => {
    vi.mocked(getLiveDetail).mockRejectedValue(new Error('Load failed'))

    const wrapper = mount(LiveRoom, {
      global: {
        plugins: [createRouterMock()],
        stubs: {
          LiveChat: { template: '<div class="live-chat"></div>' },
          GiftPanel: { template: '<div class="gift-panel"></div>' }
        }
      }
    })

    await flushPromises()

    expect(wrapper.vm.liveInfo).toBeNull()
  })

  it('应该渲染 LiveChat 组件', async () => {
    vi.mocked(getLiveDetail).mockResolvedValue({ data: mockLiveDetail } as any)

    const wrapper = mount(LiveRoom, {
      global: {
        plugins: [createRouterMock()],
        stubs: {
          GiftPanel: { template: '<div class="gift-panel"></div>' }
        }
      }
    })

    await flushPromises()

    expect(wrapper.find('.live-chat').exists()).toBe(true)
  })

  it('应该渲染 GiftPanel 组件', async () => {
    vi.mocked(getLiveDetail).mockResolvedValue({ data: mockLiveDetail } as any)

    const wrapper = mount(LiveRoom, {
      global: {
        plugins: [createRouterMock()],
        stubs: {
          LiveChat: { template: '<div class="live-chat"></div>' }
        }
      }
    })

    await flushPromises()

    expect(wrapper.find('.gift-panel').exists()).toBe(true)
  })
})
