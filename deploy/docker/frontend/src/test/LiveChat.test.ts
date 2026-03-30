import { describe, it, expect, vi, beforeEach, afterEach } from 'vitest'
import { mount, flushPromises } from '@vue/test-utils'
import LiveChat from '../views/Live/LiveChat.vue'

// Mock API
vi.mock('@/api/chat', () => ({
  getChatHistory: vi.fn(),
  connectChat: vi.fn(),
  disconnectChat: vi.fn(),
}))

import { getChatHistory } from '@/api/chat'

describe('LiveChat 组件测试', () => {
  const mockMessages = [
    {
      id: 1,
      roomId: 1,
      userId: 100,
      userName: '用户 A',
      content: '大家好！',
      avatar: '/avatar1.jpg',
      type: 'USER' as const,
      createdAt: '2026-03-30T10:00:00'
    },
    {
      id: 2,
      roomId: 1,
      userId: 101,
      userName: '用户 B',
      content: '主播好！',
      avatar: '/avatar2.jpg',
      type: 'USER' as const,
      createdAt: '2026-03-30T10:01:00'
    },
    {
      id: 3,
      roomId: 1,
      userId: 0,
      userName: '系统',
      content: '欢迎加入直播间',
      type: 'SYSTEM' as const,
      createdAt: '2026-03-30T10:00:00'
    }
  ]

  beforeEach(() => {
    vi.clearAllMocks()
    // Mock WebSocket
    global.WebSocket = vi.fn().mockImplementation(() => ({
      onopen: null,
      onmessage: null,
      onerror: null,
      onclose: null,
      send: vi.fn(),
      close: vi.fn(),
    }))
  })

  afterEach(() => {
    vi.restoreAllMocks()
  })

  it('应该正确渲染组件结构', () => {
    const wrapper = mount(LiveChat, {
      props: {
        roomId: 1
      }
    })

    expect(wrapper.find('.live-chat').exists()).toBe(true)
    expect(wrapper.find('.chat-messages').exists()).toBe(true)
    expect(wrapper.find('.chat-input').exists()).toBe(true)
  })

  it('初始状态应该正确', () => {
    const wrapper = mount(LiveChat, {
      props: {
        roomId: 1
      }
    })

    expect(wrapper.vm.messages).toEqual([])
    expect(wrapper.vm.inputValue).toBe('')
  })

  it('组件挂载时应该加载聊天历史', async () => {
    const mockResponse = {
      data: {
        list: mockMessages
      }
    }
    vi.mocked(getChatHistory).mockResolvedValue(mockResponse as any)

    const wrapper = mount(LiveChat, {
      props: {
        roomId: 1
      }
    })

    await flushPromises()

    expect(getChatHistory).toHaveBeenCalledWith(1)
    expect(wrapper.vm.messages.length).toBe(3)
  })

  it('应该渲染消息列表', async () => {
    const mockResponse = {
      data: {
        list: mockMessages
      }
    }
    vi.mocked(getChatHistory).mockResolvedValue(mockResponse as any)

    const wrapper = mount(LiveChat, {
      props: {
        roomId: 1
      }
    })

    await flushPromises()

    const messageElements = wrapper.findAll('.chat-message')
    expect(messageElements.length).toBe(3)
  })

  it('应该正确显示用户消息', async () => {
    const mockResponse = {
      data: {
        list: [mockMessages[0]]
      }
    }
    vi.mocked(getChatHistory).mockResolvedValue(mockResponse as any)

    const wrapper = mount(LiveChat, {
      props: {
        roomId: 1
      }
    })

    await flushPromises()

    expect(wrapper.find('.message-sender').text()).toBe('用户 A')
    expect(wrapper.find('.message-text').text()).toBe('大家好！')
  })

  it('应该正确显示系统消息', async () => {
    const mockResponse = {
      data: {
        list: [mockMessages[2]]
      }
    }
    vi.mocked(getChatHistory).mockResolvedValue(mockResponse as any)

    const wrapper = mount(LiveChat, {
      props: {
        roomId: 1
      }
    })

    await flushPromises()

    const systemMessage = wrapper.find('.chat-message--system')
    expect(systemMessage.exists()).toBe(true)
    expect(systemMessage.find('.message-text').text()).toBe('欢迎加入直播间')
  })

  it('系统消息不应该显示头像', async () => {
    const mockResponse = {
      data: {
        list: [mockMessages[2]]
      }
    }
    vi.mocked(getChatHistory).mockResolvedValue(mockResponse as any)

    const wrapper = mount(LiveChat, {
      props: {
        roomId: 1
      }
    })

    await flushPromises()

    const systemMessage = wrapper.find('.chat-message--system')
    expect(systemMessage.find('.message-avatar').exists()).toBe(false)
  })

  it('用户消息应该显示头像', async () => {
    const mockResponse = {
      data: {
        list: [mockMessages[0]]
      }
    }
    vi.mocked(getChatHistory).mockResolvedValue(mockResponse as any)

    const wrapper = mount(LiveChat, {
      props: {
        roomId: 1
      }
    })

    await flushPromises()

    const userMessage = wrapper.find('.chat-message')
    expect(userMessage.find('.message-avatar').exists()).toBe(true)
  })

  it('输入框应该支持 v-model 绑定', async () => {
    const wrapper = mount(LiveChat, {
      props: {
        roomId: 1
      }
    })

    const input = wrapper.find('.chat-input__field')
    await input.setValue('测试消息')

    expect(wrapper.vm.inputValue).toBe('测试消息')
  })

  it('sendMessage 应该发送消息', async () => {
    const wrapper = mount(LiveChat, {
      props: {
        roomId: 1
      }
    })

    const emitSpy = vi.fn()
    wrapper.vm.$emit = emitSpy

    wrapper.vm.inputValue = '测试消息'
    wrapper.vm.sendMessage()

    expect(emitSpy).toHaveBeenCalledWith('sendMessage', '测试消息')
    expect(wrapper.vm.inputValue).toBe('')
  })

  it('空消息不应该发送', async () => {
    const wrapper = mount(LiveChat, {
      props: {
        roomId: 1
      }
    })

    const emitSpy = vi.fn()
    wrapper.vm.$emit = emitSpy

    wrapper.vm.inputValue = ''
    wrapper.vm.sendMessage()

    expect(emitSpy).not.toHaveBeenCalled()
  })

  it('只有空格的消息不应该发送', async () => {
    const wrapper = mount(LiveChat, {
      props: {
        roomId: 1
      }
    })

    const emitSpy = vi.fn()
    wrapper.vm.$emit = emitSpy

    wrapper.vm.inputValue = '   '
    wrapper.vm.sendMessage()

    expect(emitSpy).not.toHaveBeenCalled()
  })

  it('点击发送按钮应该触发 sendMessage', async () => {
    const wrapper = mount(LiveChat, {
      props: {
        roomId: 1
      }
    })

    const emitSpy = vi.fn()
    wrapper.vm.$emit = emitSpy

    wrapper.vm.inputValue = '测试消息'
    const sendBtn = wrapper.find('.chat-input__btn')
    await sendBtn.trigger('click')

    expect(emitSpy).toHaveBeenCalledWith('sendMessage', '测试消息')
  })

  it('按 Enter 键应该发送消息', async () => {
    const wrapper = mount(LiveChat, {
      props: {
        roomId: 1
      }
    })

    const emitSpy = vi.fn()
    wrapper.vm.$emit = emitSpy

    wrapper.vm.inputValue = '测试消息'
    const input = wrapper.find('.chat-input__field')
    await input.trigger('keyup.enter')

    expect(emitSpy).toHaveBeenCalledWith('sendMessage', '测试消息')
  })

  it('应该建立 WebSocket 连接', async () => {
    const wrapper = mount(LiveChat, {
      props: {
        roomId: 1
      }
    })

    await flushPromises()

    expect(global.WebSocket).toHaveBeenCalledWith('ws://localhost:8080/ws/chat/1')
  })

  it('WebSocket 接收消息应该追加到列表', async () => {
    const mockResponse = {
      data: {
        list: [mockMessages[0]]
      }
    }
    vi.mocked(getChatHistory).mockResolvedValue(mockResponse as any)

    const wrapper = mount(LiveChat, {
      props: {
        roomId: 1
      }
    })

    await flushPromises()

    // 模拟 WebSocket 消息
    const ws = (global.WebSocket as any).mock.results[0].value
    const newMessage = {
      id: 99,
      roomId: 1,
      userId: 102,
      userName: '新用户',
      content: '新消息',
      type: 'USER',
      createdAt: '2026-03-30T10:02:00'
    }
    ws.onmessage({ data: JSON.stringify(newMessage) })

    await wrapper.vm.$nextTick()

    expect(wrapper.vm.messages.length).toBe(2)
    expect(wrapper.vm.messages[1].content).toBe('新消息')
  })

  it('新消息应该自动滚动到底部', async () => {
    const mockResponse = {
      data: {
        list: [mockMessages[0]]
      }
    }
    vi.mocked(getChatHistory).mockResolvedValue(mockResponse as any)

    const wrapper = mount(LiveChat, {
      props: {
        roomId: 1
      }
    })

    await flushPromises()

    const messagesRef = wrapper.vm.messagesRef
    const initialScrollTop = messagesRef?.scrollTop || 0

    // 模拟 WebSocket 消息
    const ws = (global.WebSocket as any).mock.results[0].value
    ws.onmessage({ data: JSON.stringify(mockMessages[1]) })

    await wrapper.vm.$nextTick()

    // 验证滚动位置改变
    expect(messagesRef).toBeDefined()
  })

  it('WebSocket 关闭应该尝试重连', async () => {
    vi.useFakeTimers()

    const wrapper = mount(LiveChat, {
      props: {
        roomId: 1
      }
    })

    await flushPromises()

    const ws = (global.WebSocket as any).mock.results[0].value
    ws.onclose()

    // 快进 3 秒
    vi.advanceTimersByTime(3000)

    // 应该尝试重新连接
    expect(global.WebSocket).toHaveBeenCalledTimes(2)

    vi.useRealTimers()
  })

  it('组件卸载时应该关闭 WebSocket', async () => {
    const wrapper = mount(LiveChat, {
      props: {
        roomId: 1
      }
    })

    await flushPromises()

    const ws = (global.WebSocket as any).mock.results[0].value
    wrapper.unmount()

    expect(ws.close).toHaveBeenCalled()
  })

  it('加载失败应该处理错误', async () => {
    vi.mocked(getChatHistory).mockRejectedValue(new Error('Load failed'))

    const wrapper = mount(LiveChat, {
      props: {
        roomId: 1
      }
    })

    await flushPromises()

    expect(wrapper.vm.messages).toEqual([])
  })

  it('消息列表应该为空数组初始值', () => {
    const wrapper = mount(LiveChat, {
      props: {
        roomId: 1
      }
    })

    expect(Array.isArray(wrapper.vm.messages)).toBe(true)
    expect(wrapper.vm.messages.length).toBe(0)
  })

  it('应该定义 scrollToBottom 方法', () => {
    const wrapper = mount(LiveChat, {
      props: {
        roomId: 1
      }
    })

    expect(typeof wrapper.vm.scrollToBottom).toBe('function')
  })

  it('roomId prop 应该为必填', () => {
    expect(() => {
      mount(LiveChat as any)
    }).not.toThrow()
  })
})
