/**
 * MessageCenter 组件测试
 * @module tests/message/MessageCenter.test.js
 * @description 消息中心组件的完整测试覆盖
 */

import { describe, it, expect, vi, beforeEach } from 'vitest';
import { mount, flushPromises } from '@vue/test-utils';
import MessageCenter from '../../src/views/Message/MessageCenter.vue';
import MessageList from '../../src/views/Message/MessageList.vue';
import { createRouter, createWebHistory } from 'vue-router';
import { createPinia, setActivePinia } from 'pinia';

// Mock API
vi.mock('../../src/api/message', () => ({
  getMessageList: vi.fn(),
  getMessageDetail: vi.fn(),
  markAsRead: vi.fn(),
  markAllAsRead: vi.fn(),
  getUnreadCount: vi.fn(),
  deleteMessage: vi.fn()
}));

// Mock WebSocket
vi.mock('../../src/utils/websocket', () => ({
  createWebSocket: vi.fn(),
  closeWebSocket: vi.fn()
}));

describe('MessageCenter', () => {
  let wrapper;
  let router;
  let pinia;

  const mockMessages = [
    {
      id: 1,
      type: 'private',
      from: { id: 1002, nickname: '好友用户', avatar: 'friend.jpg' },
      content: '你好，这是一条测试消息',
      isRead: false,
      createTime: '2026-03-30 10:00:00'
    },
    {
      id: 2,
      type: 'system',
      from: { id: 0, nickname: '系统通知', avatar: 'system.png' },
      content: '系统通知：欢迎使用 Snail',
      isRead: true,
      createTime: '2026-03-30 09:00:00'
    },
    {
      id: 3,
      type: 'interaction',
      from: { id: 1003, nickname: '点赞用户', avatar: 'liker.jpg' },
      content: '点赞了你的动态',
      isRead: false,
      createTime: '2026-03-30 11:00:00'
    }
  ];

  beforeEach(async () => {
    pinia = createPinia();
    setActivePinia(pinia);
    
    router = createRouter({
      history: createWebHistory(),
      routes: [
        { path: '/', component: { template: '<div>Home</div>' } },
        { path: '/message/:id', component: { template: '<div>Message Detail</div>' }, name: 'message-detail' }
      ]
    });

    vi.clearAllMocks();
  });

  // MC-001: 组件正常渲染
  it('MC-001: 组件正常渲染', async () => {
    wrapper = mount(MessageCenter, {
      global: {
        plugins: [router, pinia],
        stubs: ['MessageList', 'router-link']
      }
    });

    await flushPromises();

    expect(wrapper.exists()).toBe(true);
    expect(wrapper.find('.message-center').exists()).toBe(true);
    expect(wrapper.find('.message-tabs').exists()).toBe(true);
    expect(wrapper.findComponent(MessageList).exists()).toBe(true);
  });

  // MC-002: 默认显示全部消息
  it('MC-002: 默认显示全部消息', async () => {
    wrapper = mount(MessageCenter, {
      global: {
        plugins: [router, pinia],
        stubs: ['MessageList']
      },
      props: {
        messages: mockMessages
      }
    });

    await flushPromises();

    const activeTab = wrapper.find('.tab-item.active');
    expect(activeTab.text()).toBe('全部');
  });

  // MC-003: 切换到私信标签
  it('MC-003: 切换到私信标签', async () => {
    wrapper = mount(MessageCenter, {
      global: {
        plugins: [router, pinia],
        stubs: ['MessageList']
      },
      props: {
        messages: mockMessages
      }
    });

    await flushPromises();

    const privateTab = wrapper.findAll('.tab-item')[1];
    await privateTab.trigger('click');
    await flushPromises();

    expect(privateTab.classes()).toContain('active');
    const emitted = wrapper.emitted('filter-change');
    expect(emitted).toBeTruthy();
    expect(emitted[0]).toEqual(['private']);
  });

  // MC-004: 切换到互动标签
  it('MC-004: 切换到互动标签', async () => {
    wrapper = mount(MessageCenter, {
      global: {
        plugins: [router, pinia],
        stubs: ['MessageList']
      },
      props: {
        messages: mockMessages
      }
    });

    await flushPromises();

    const interactionTab = wrapper.findAll('.tab-item')[2];
    await interactionTab.trigger('click');
    await flushPromises();

    expect(interactionTab.classes()).toContain('active');
  });

  // MC-005: 切换到系统标签
  it('MC-005: 切换到系统标签', async () => {
    wrapper = mount(MessageCenter, {
      global: {
        plugins: [router, pinia],
        stubs: ['MessageList']
      },
      props: {
        messages: mockMessages
      }
    });

    await flushPromises();

    const systemTab = wrapper.findAll('.tab-item')[3];
    await systemTab.trigger('click');
    await flushPromises();

    expect(systemTab.classes()).toContain('active');
  });

  // MC-006: 未读计数显示
  it('MC-006: 未读计数显示', async () => {
    wrapper = mount(MessageCenter, {
      global: {
        plugins: [router, pinia],
        stubs: ['MessageList']
      },
      props: {
        messages: mockMessages,
        unreadCount: {
          all: 2,
          private: 1,
          interaction: 1,
          system: 0
        }
      }
    });

    await flushPromises();

    const tabs = wrapper.findAll('.tab-item');
    expect(tabs[0].text()).toContain('全部(2)');
    expect(tabs[1].text()).toContain('私信(1)');
    expect(tabs[2].text()).toContain('互动(1)');
    expect(tabs[3].text()).toContain('系统');
  });

  // MC-007: 未读计数归零
  it('MC-007: 未读计数归零', async () => {
    const { markAllAsRead } = await import('../../src/api/message');
    markAllAsRead.mockResolvedValue({ success: true });

    wrapper = mount(MessageCenter, {
      global: {
        plugins: [router, pinia],
        stubs: ['MessageList']
      },
      props: {
        messages: mockMessages,
        unreadCount: { all: 2, private: 1, interaction: 1, system: 0 }
      }
    });

    await flushPromises();

    const markAllBtn = wrapper.find('.mark-all-read');
    await markAllBtn.trigger('click');
    await flushPromises();

    expect(markAllAsRead).toHaveBeenCalled();
    const emitted = wrapper.emitted('unread-change');
    expect(emitted).toBeTruthy();
  });

  // MC-008: 消息列表加载
  it('MC-008: 消息列表加载', async () => {
    const { getMessageList } = await import('../../src/api/message');
    getMessageList.mockResolvedValue({ 
      data: { list: mockMessages, total: 3 } 
    });

    wrapper = mount(MessageCenter, {
      global: {
        plugins: [router, pinia],
        stubs: ['MessageList']
      }
    });

    await flushPromises();

    expect(getMessageList).toHaveBeenCalledWith({ page: 1, size: 20, type: 'all' });
  });

  // MC-009: 消息详情跳转
  it('MC-009: 消息详情跳转', async () => {
    wrapper = mount(MessageCenter, {
      global: {
        plugins: [router, pinia],
        stubs: ['MessageList']
      },
      props: {
        messages: mockMessages
      }
    });

    await flushPromises();

    const messageList = wrapper.findComponent(MessageList);
    messageList.vm.$emit('message-click', mockMessages[0]);
    await flushPromises();

    expect(router.currentRoute.value.path).toBe(`/message/${mockMessages[0].id}`);
  });

  // MC-010: 下拉刷新功能
  it('MC-010: 下拉刷新功能', async () => {
    const { getMessageList } = await import('../../src/api/message');
    getMessageList.mockResolvedValue({ 
      data: { list: mockMessages, total: 3 } 
    });

    wrapper = mount(MessageCenter, {
      global: {
        plugins: [router, pinia],
        stubs: ['MessageList']
      }
    });

    await flushPromises();

    const messageList = wrapper.findComponent(MessageList);
    messageList.vm.$emit('refresh');
    await flushPromises();

    expect(getMessageList).toHaveBeenCalledTimes(2);
  });

  // MC-011: 空状态显示
  it('MC-011: 空状态显示', async () => {
    wrapper = mount(MessageCenter, {
      global: {
        plugins: [router, pinia],
        stubs: ['MessageList']
      },
      props: {
        messages: []
      }
    });

    await flushPromises();

    const emptyState = wrapper.find('.empty-state');
    expect(emptyState.exists()).toBe(true);
    expect(emptyState.text()).toContain('暂无消息');
  });

  // MC-012: 加载更多
  it('MC-012: 加载更多', async () => {
    const { getMessageList } = await import('../../src/api/message');
    getMessageList.mockResolvedValueOnce({ 
      data: { list: mockMessages.slice(0, 2), total: 5 } 
    }).mockResolvedValueOnce({ 
      data: { list: mockMessages.slice(2), total: 5 } 
    });

    wrapper = mount(MessageCenter, {
      global: {
        plugins: [router, pinia],
        stubs: ['MessageList']
      }
    });

    await flushPromises();

    const messageList = wrapper.findComponent(MessageList);
    messageList.vm.$emit('load-more');
    await flushPromises();

    expect(getMessageList).toHaveBeenCalledWith({ page: 2, size: 20, type: 'all' });
  });

  // MC-013: 全部已读功能
  it('MC-013: 全部已读功能', async () => {
    const { markAllAsRead } = await import('../../src/api/message');
    markAllAsRead.mockResolvedValue({ success: true });

    wrapper = mount(MessageCenter, {
      global: {
        plugins: [router, pinia],
        stubs: ['MessageList']
      },
      props: {
        messages: mockMessages
      }
    });

    await flushPromises();

    const markAllBtn = wrapper.find('.mark-all-read');
    await markAllBtn.trigger('click');
    await flushPromises();

    expect(markAllAsRead).toHaveBeenCalled();
  });

  // MC-014: 错误处理
  it('MC-014: 错误处理', async () => {
    const { getMessageList } = await import('../../src/api/message');
    getMessageList.mockRejectedValue(new Error('Network Error'));

    wrapper = mount(MessageCenter, {
      global: {
        plugins: [router, pinia],
        stubs: ['MessageList']
      }
    });

    await flushPromises();

    const errorState = wrapper.find('.error-state');
    expect(errorState.exists()).toBe(true);
    expect(errorState.text()).toContain('加载失败');
  });

  // MC-015: WebSocket 消息接收
  it('MC-015: WebSocket 消息接收', async () => {
    const newMessage = {
      id: 4,
      type: 'private',
      from: { id: 1004, nickname: '新用户', avatar: 'new.jpg' },
      content: '新消息',
      isRead: false,
      createTime: '2026-03-30 12:00:00'
    };

    wrapper = mount(MessageCenter, {
      global: {
        plugins: [router, pinia],
        stubs: ['MessageList']
      },
      props: {
        messages: mockMessages
      }
    });

    await flushPromises();

    // 模拟 WebSocket 推送
    wrapper.vm.handleWebSocketMessage(newMessage);
    await flushPromises();

    const emitted = wrapper.emitted('message-add');
    expect(emitted).toBeTruthy();
    expect(emitted[0][0]).toEqual(newMessage);
  });
});
