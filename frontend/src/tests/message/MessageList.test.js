/**
 * MessageList 组件测试
 * @module tests/message/MessageList.test.js
 * @description 消息列表组件的完整测试覆盖
 */

import { describe, it, expect, vi, beforeEach } from 'vitest';
import { mount, flushPromises } from '@vue/test-utils';
import MessageList from '../../src/views/Message/MessageList.vue';
import MessageItem from '../../src/views/Message/MessageItem.vue';

describe('MessageList', () => {
  let wrapper;

  const mockMessages = [
    {
      id: 1,
      type: 'private',
      from: { id: 1002, nickname: '好友用户', avatar: 'friend.jpg' },
      content: '你好，这是一条测试消息',
      isRead: false,
      createTime: new Date().toISOString()
    },
    {
      id: 2,
      type: 'system',
      from: { id: 0, nickname: '系统通知', avatar: 'system.png' },
      content: '系统通知：欢迎使用 Snail',
      isRead: true,
      createTime: new Date(Date.now() - 3600000).toISOString()
    },
    {
      id: 3,
      type: 'interaction',
      from: { id: 1003, nickname: '点赞用户', avatar: 'liker.jpg' },
      content: '点赞了你的动态',
      isRead: false,
      createTime: new Date(Date.now() - 7200000).toISOString()
    }
  ];

  beforeEach(() => {
    vi.clearAllMocks();
  });

  // ML-001: 列表正常渲染
  it('ML-001: 列表正常渲染', () => {
    wrapper = mount(MessageList, {
      props: {
        messages: mockMessages
      }
    });

    expect(wrapper.exists()).toBe(true);
    expect(wrapper.find('.message-list').exists()).toBe(true);
    const items = wrapper.findAllComponents(MessageItem);
    expect(items.length).toBe(3);
  });

  // ML-002: 消息项点击
  it('ML-002: 消息项点击', async () => {
    wrapper = mount(MessageList, {
      props: {
        messages: mockMessages
      }
    });

    const firstItem = wrapper.findComponent(MessageItem);
    await firstItem.trigger('click');

    const emitted = wrapper.emitted('message-click');
    expect(emitted).toBeTruthy();
    expect(emitted[0][0]).toEqual(mockMessages[0]);
  });

  // ML-003: 未读标记显示
  it('ML-003: 未读标记显示', () => {
    wrapper = mount(MessageList, {
      props: {
        messages: [mockMessages[0]] // 未读消息
      }
    });

    const item = wrapper.findComponent(MessageItem);
    const unreadBadge = item.find('.unread-badge');
    expect(unreadBadge.exists()).toBe(true);
  });

  // ML-004: 已读标记显示
  it('ML-004: 已读标记显示', () => {
    wrapper = mount(MessageList, {
      props: {
        messages: [mockMessages[1]] // 已读消息
      }
    });

    const item = wrapper.findComponent(MessageItem);
    const unreadBadge = item.find('.unread-badge');
    expect(unreadBadge.exists()).toBe(false);
    expect(item.classes()).not.toContain('unread');
  });

  // ML-005: 时间格式化
  it('ML-005: 时间格式化', () => {
    wrapper = mount(MessageList, {
      props: {
        messages: mockMessages
      }
    });

    const items = wrapper.findAllComponents(MessageItem);
    
    // 刚刚的消息
    expect(items[0].find('.time').text()).toMatch(/刚刚|\d+分钟前/);
    
    // 1 小时前的消息
    expect(items[1].find('.time').text()).toMatch(/\d+小时前/);
    
    // 2 小时前的消息
    expect(items[2].find('.time').text()).toMatch(/\d+小时前/);
  });

  // ML-006: 头像显示
  it('ML-006: 头像显示', () => {
    wrapper = mount(MessageList, {
      props: {
        messages: mockMessages
      }
    });

    const items = wrapper.findAllComponents(MessageItem);
    items.forEach((item, index) => {
      const avatar = item.find('.avatar');
      expect(avatar.exists()).toBe(true);
      expect(avatar.attributes('src')).toBe(mockMessages[index].from.avatar);
    });
  });

  // ML-007: 昵称显示
  it('ML-007: 昵称显示', () => {
    wrapper = mount(MessageList, {
      props: {
        messages: mockMessages
      }
    });

    const items = wrapper.findAllComponents(MessageItem);
    items.forEach((item, index) => {
      const nickname = item.find('.nickname');
      expect(nickname.text()).toBe(mockMessages[index].from.nickname);
    });
  });

  // ML-008: 消息预览截断
  it('ML-008: 消息预览截断', () => {
    const longMessage = {
      id: 4,
      type: 'private',
      from: { id: 1002, nickname: '好友用户', avatar: 'friend.jpg' },
      content: '这是一条非常非常非常非常非常非常非常非常非常非常非常非常非常非常非常长的消息',
      isRead: false,
      createTime: new Date().toISOString()
    };

    wrapper = mount(MessageList, {
      props: {
        messages: [longMessage]
      }
    });

    const item = wrapper.findComponent(MessageItem);
    const content = item.find('.content');
    expect(content.exists()).toBe(true);
    // 检查是否有截断样式
    expect(content.classes()).toContain('truncate');
  });

  // ML-009: 消息类型图标
  it('ML-009: 消息类型图标', () => {
    wrapper = mount(MessageList, {
      props: {
        messages: mockMessages
      }
    });

    const items = wrapper.findAllComponents(MessageItem);
    
    // 私信图标
    expect(items[0].find('.type-icon').exists()).toBe(true);
    
    // 系统消息图标
    expect(items[1].find('.type-icon').exists()).toBe(true);
    
    // 互动消息图标
    expect(items[2].find('.type-icon').exists()).toBe(true);
  });

  // ML-010: 加载更多状态
  it('ML-010: 加载更多状态', () => {
    wrapper = mount(MessageList, {
      props: {
        messages: mockMessages,
        loading: true
      }
    });

    const loader = wrapper.find('.loading-spinner');
    expect(loader.exists()).toBe(true);
  });

  // ML-011: 空列表处理
  it('ML-011: 空列表处理', () => {
    wrapper = mount(MessageList, {
      props: {
        messages: []
      }
    });

    const emptyState = wrapper.find('.empty-state');
    expect(emptyState.exists()).toBe(true);
    expect(emptyState.text()).toContain('暂无消息');
  });

  // ML-012: 删除消息功能
  it('ML-012: 删除消息功能', async () => {
    wrapper = mount(MessageList, {
      props: {
        messages: mockMessages,
        showDelete: true
      }
    });

    const firstItem = wrapper.findComponent(MessageItem);
    const deleteBtn = firstItem.find('.delete-btn');
    await deleteBtn.trigger('click');

    const emitted = wrapper.emitted('message-delete');
    expect(emitted).toBeTruthy();
    expect(emitted[0][0]).toBe(mockMessages[0].id);
  });

  // 额外测试：消息分组显示
  it('支持按日期分组显示', () => {
    const messagesWithDates = [
      {
        id: 1,
        type: 'private',
        from: { id: 1002, nickname: '好友', avatar: 'a.jpg' },
        content: '今天的消息',
        isRead: false,
        createTime: new Date().toISOString()
      },
      {
        id: 2,
        type: 'private',
        from: { id: 1002, nickname: '好友', avatar: 'a.jpg' },
        content: '昨天的消息',
        isRead: true,
        createTime: new Date(Date.now() - 86400000).toISOString()
      }
    ];

    wrapper = mount(MessageList, {
      props: {
        messages: messagesWithDates,
        groupByDate: true
      }
    });

    expect(wrapper.find('.date-group').exists()).toBe(true);
  });

  // 额外测试：滑动删除
  it('支持滑动删除手势', async () => {
    wrapper = mount(MessageList, {
      props: {
        messages: mockMessages,
        enableSwipeDelete: true
      }
    });

    const item = wrapper.findComponent(MessageItem);
    
    // 模拟滑动
    await item.trigger('touchstart', { touches: [{ clientX: 100 }] });
    await item.trigger('touchmove', { touches: [{ clientX: 50 }] });
    await item.trigger('touchend', { changedTouches: [{ clientX: 50 }] });

    // 检查是否显示删除按钮
    expect(item.find('.delete-action').exists()).toBe(true);
  });

  // 额外测试：长按操作
  it('支持长按弹出操作菜单', async () => {
    vi.useFakeTimers();

    wrapper = mount(MessageList, {
      props: {
        messages: mockMessages,
        enableLongPress: true
      }
    });

    const item = wrapper.findComponent(MessageItem);
    await item.trigger('mousedown');
    vi.advanceTimersByTime(500); // 长按 500ms
    await item.trigger('mouseup');

    const emitted = wrapper.emitted('show-actions');
    expect(emitted).toBeTruthy();

    vi.useRealTimers();
  });

  // 额外测试：批量选择模式
  it('支持批量选择模式', async () => {
    wrapper = mount(MessageList, {
      props: {
        messages: mockMessages,
        selectMode: true
      }
    });

    const firstItem = wrapper.findComponent(MessageItem);
    await firstItem.trigger('click');

    const emitted = wrapper.emitted('selection-change');
    expect(emitted).toBeTruthy();
    expect(emitted[0][0]).toContain(mockMessages[0].id);
  });

  // 额外测试：消息搜索高亮
  it('支持搜索关键词高亮', () => {
    wrapper = mount(MessageList, {
      props: {
        messages: mockMessages,
        searchKeyword: '测试'
      }
    });

    const item = wrapper.findComponent(MessageItem);
    const highlighted = item.find('.highlight');
    expect(highlighted.exists()).toBe(true);
    expect(highlighted.text()).toBe('测试');
  });

  // 额外测试：虚拟滚动
  it('支持大数据量虚拟滚动', () => {
    const largeDataSet = Array.from({ length: 1000 }, (_, i) => ({
      id: i + 1,
      type: 'private',
      from: { id: 1002, nickname: '好友', avatar: 'a.jpg' },
      content: `消息 ${i + 1}`,
      isRead: false,
      createTime: new Date().toISOString()
    }));

    wrapper = mount(MessageList, {
      props: {
        messages: largeDataSet,
        useVirtualScroll: true,
        itemHeight: 80,
        visibleCount: 10
      }
    });

    // 检查是否只渲染可见区域的项目
    const items = wrapper.findAllComponents(MessageItem);
    expect(items.length).toBeLessThan(20); // 只渲染可见区域 + 缓冲
  });
});
