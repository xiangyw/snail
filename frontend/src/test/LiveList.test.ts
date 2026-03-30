import { describe, it, expect, vi, beforeEach, afterEach } from 'vitest'
import { mount, flushPromises } from '@vue/test-utils'
import LiveList from '../views/Live/LiveList.vue'
import LiveItem from '../views/Live/LiveItem.vue'

// Mock API
vi.mock('@/api/live', () => ({
  getLiveList: vi.fn(),
}))

import { getLiveList } from '@/api/live'

describe('LiveList 组件测试', () => {
  const mockLiveList = [
    {
      id: 1,
      title: '测试直播 1',
      userName: '主播 A',
      previewUrl: '/preview1.jpg',
      viewerCount: 100,
      status: 'LIVE' as const,
      createdAt: '2026-03-30T10:00:00'
    },
    {
      id: 2,
      title: '测试直播 2',
      userName: '主播 B',
      previewUrl: '/preview2.jpg',
      viewerCount: 200,
      status: 'LIVE' as const,
      createdAt: '2026-03-30T11:00:00'
    }
  ]

  beforeEach(() => {
    vi.clearAllMocks()
  })

  it('应该正确渲染组件结构', () => {
    const wrapper = mount(LiveList, {
      global: {
        components: { LiveItem }
      }
    })
    
    expect(wrapper.find('.live-list').exists()).toBe(true)
    expect(wrapper.find('.van-nav-bar').exists()).toBe(true)
  })

  it('应该显示正确的标题', () => {
    const wrapper = mount(LiveList, {
      global: {
        components: { LiveItem }
      }
    })
    
    const navBar = wrapper.find('.van-nav-bar')
    expect(navBar.text()).toContain('直播列表')
  })

  it('初始状态应该正确', () => {
    const wrapper = mount(LiveList, {
      global: {
        components: { LiveItem }
      }
    })
    
    expect(wrapper.vm.loading).toBe(false)
    expect(wrapper.vm.finished).toBe(false)
    expect(wrapper.vm.liveList).toEqual([])
    expect(wrapper.vm.page).toBe(1)
  })

  it('onLoad 应该调用 API 获取直播列表', async () => {
    const mockResponse = {
      data: {
        list: mockLiveList,
        total: 2
      }
    }
    vi.mocked(getLiveList).mockResolvedValue(mockResponse as any)

    const wrapper = mount(LiveList, {
      global: {
        components: { LiveItem }
      }
    })

    // 触发 onLoad
    await wrapper.vm.onLoad()
    await flushPromises()

    expect(getLiveList).toHaveBeenCalledWith({
      page: 1,
      pageSize: 10,
      status: 'LIVE'
    })
  })

  it('加载成功应该更新列表数据', async () => {
    const mockResponse = {
      data: {
        list: mockLiveList,
        total: 2
      }
    }
    vi.mocked(getLiveList).mockResolvedValue(mockResponse as any)

    const wrapper = mount(LiveList, {
      global: {
        components: { LiveItem }
      }
    })

    await wrapper.vm.onLoad()
    await flushPromises()

    expect(wrapper.vm.liveList.length).toBe(2)
    expect(wrapper.vm.liveList[0].title).toBe('测试直播 1')
    expect(wrapper.vm.liveList[1].title).toBe('测试直播 2')
  })

  it('加载完成后应该增加页码', async () => {
    const mockResponse = {
      data: {
        list: mockLiveList,
        total: 20
      }
    }
    vi.mocked(getLiveList).mockResolvedValue(mockResponse as any)

    const wrapper = mount(LiveList, {
      global: {
        components: { LiveItem }
      }
    })

    const initialPage = wrapper.vm.page
    await wrapper.vm.onLoad()
    await flushPromises()

    expect(wrapper.vm.page).toBe(initialPage + 1)
  })

  it('当数据加载完毕应该设置 finished 状态', async () => {
    const mockResponse = {
      data: {
        list: mockLiveList,
        total: 2
      }
    }
    vi.mocked(getLiveList).mockResolvedValue(mockResponse as any)

    const wrapper = mount(LiveList, {
      global: {
        components: { LiveItem }
      }
    })

    await wrapper.vm.onLoad()
    await flushPromises()

    expect(wrapper.vm.finished).toBe(true)
  })

  it('加载更多应该追加数据', async () => {
    const mockPage1 = {
      data: {
        list: mockLiveList.slice(0, 1),
        total: 20
      }
    }
    const mockPage2 = {
      data: {
        list: mockLiveList.slice(1),
        total: 20
      }
    }
    
    vi.mocked(getLiveList)
      .mockResolvedValueOnce(mockPage1 as any)
      .mockResolvedValueOnce(mockPage2 as any)

    const wrapper = mount(LiveList, {
      global: {
        components: { LiveItem }
      }
    })

    // 第一页
    await wrapper.vm.onLoad()
    await flushPromises()
    expect(wrapper.vm.liveList.length).toBe(1)
    expect(wrapper.vm.page).toBe(2)

    // 第二页
    await wrapper.vm.onLoad()
    await flushPromises()
    expect(wrapper.vm.liveList.length).toBe(2)
    expect(wrapper.vm.page).toBe(3)
  })

  it('加载失败应该显示错误提示', async () => {
    vi.mocked(getLiveList).mockRejectedValue(new Error('Network Error'))

    const wrapper = mount(LiveList, {
      global: {
        components: { LiveItem }
      }
    })

    await wrapper.vm.onLoad()
    await flushPromises()

    expect(wrapper.vm.loading).toBe(false)
    expect(wrapper.vm.finished).toBe(true)
  })

  it('空列表应该显示 Empty 组件', async () => {
    const mockResponse = {
      data: {
        list: [],
        total: 0
      }
    }
    vi.mocked(getLiveList).mockResolvedValue(mockResponse as any)

    const wrapper = mount(LiveList, {
      global: {
        components: { LiveItem }
      }
    })

    await wrapper.vm.onLoad()
    await flushPromises()

    expect(wrapper.find('.van-empty').exists()).toBe(true)
  })

  it('goToLiveRoom 应该调用 router.push', async () => {
    const wrapper = mount(LiveList, {
      global: {
        components: { LiveItem }
      }
    })

    const pushSpy = vi.fn()
    wrapper.vm.$router.push = pushSpy

    wrapper.vm.goToLiveRoom(123)

    expect(pushSpy).toHaveBeenCalledWith('/live/123')
  })

  it('LiveItem 点击应该触发跳转', async () => {
    const wrapper = mount(LiveList, {
      global: {
        components: { LiveItem }
      }
    })

    wrapper.vm.liveList = mockLiveList
    await wrapper.vm.$nextTick()

    const pushSpy = vi.fn()
    wrapper.vm.$router.push = pushSpy

    const liveItem = wrapper.findComponent(LiveItem)
    liveItem.vm.$emit('click')

    expect(pushSpy).toHaveBeenCalledWith('/live/1')
  })

  it('refresh 方法应该重置数据并重新加载', async () => {
    const mockResponse = {
      data: {
        list: mockLiveList,
        total: 2
      }
    }
    vi.mocked(getLiveList).mockResolvedValue(mockResponse as any)

    const wrapper = mount(LiveList, {
      global: {
        components: { LiveItem }
      }
    })

    // 先加载一些数据
    wrapper.vm.liveList = mockLiveList
    wrapper.vm.page = 3
    wrapper.vm.finished = true

    // 刷新
    await wrapper.vm.refresh()
    await flushPromises()

    expect(wrapper.vm.page).toBe(2) // 加载后页码会增加
    expect(wrapper.vm.finished).toBe(true)
    expect(wrapper.vm.liveList.length).toBe(2)
  })

  it('应该渲染 LiveItem 组件', async () => {
    const mockResponse = {
      data: {
        list: mockLiveList,
        total: 2
      }
    }
    vi.mocked(getLiveList).mockResolvedValue(mockResponse as any)

    const wrapper = mount(LiveList, {
      global: {
        components: { LiveItem }
      }
    })

    await wrapper.vm.onLoad()
    await flushPromises()

    const liveItems = wrapper.findAllComponents(LiveItem)
    expect(liveItems.length).toBe(2)
  })

  it('应该传递正确的 props 给 LiveItem', async () => {
    const mockResponse = {
      data: {
        list: mockLiveList,
        total: 2
      }
    }
    vi.mocked(getLiveList).mockResolvedValue(mockResponse as any)

    const wrapper = mount(LiveList, {
      global: {
        components: { LiveItem }
      }
    })

    await wrapper.vm.onLoad()
    await flushPromises()

    const firstItem = wrapper.findComponent(LiveItem)
    expect(firstItem.props('live')).toEqual(mockLiveList[0])
  })
})
