/**
 * MessageDetail 组件测试
 * @module tests/message/MessageDetail.test.js
 * @description 消息详情组件的完整测试覆盖
 */

import { describe, it, expect, vi, beforeEach } from 'vitest';
import { mount, flushPromises } from '@vue/test-utils';
import { createRouter, createWebHistory } from 'vue-router';
import { createPinia, setActivePinia } from 'pinia';
import MessageDetail from '../../src/views/Message/MessageDetail.vue';

// Mock API
vi.mock('../../src/api/message', () => ({
  getMessageDetail: vi.fn(),
  markAsRead: vi.fn()
}));

describe('MessageDetail', () => {
  let wrapper;
  let router;
  let pinia;

  const mockMessage = {
    id: 1,
    type: 'private',
    from: { 
      id: 1002, 
      nickname: '好友用户', 
      avatar: 'friend.jpg',
      online: true
    },
    content: '你好，这是一条测试消息内容',
    isRead: false,
    createTime: '2026-03-30 10:00:00',
    images: [],
    link: null
  };

  beforeEach(async () => {
    pinia = createPinia();
    setActivePinia(pinia);
    
    router = createRouter({
      history: createWebHistory(),
      routes: [
        { path: '/', component: { template: '<div>Home</div>' }, name: 'home' },
        { path: '/message/:id', component: { template: '<div>Message Detail</div>' }, name: 'message-detail' }
      ]
    });

    vi.clearAllMocks();
  });

  // MD-001: 详情正常渲染
  it('MD-001: 详情正常渲染', async () => {
    wrapper = mount(MessageDetail, {
      global: {
        plugins: [router, pinia]
      },
      props: {
        message: mockMessage
      }
    });

    await flushPromises();

    expect(wrapper.exists()).toBe(true);
    expect(wrapper.find('.message-detail').exists()).toBe(true);
    expect(wrapper.find('.message-content').exists()).toBe(true);
  });

  // MD-002: 发送者信息显示
  it('MD-002: 发送者信息显示', async () => {
    wrapper = mount(MessageDetail, {
      global: {
        plugins: [router, pinia]
      },
      props: {
        message: mockMessage
      }
    });

    await flushPromises();

    const senderSection = wrapper.find('.sender-info');
    expect(senderSection.exists()).toBe(true);
    
    const avatar = senderSection.find('.avatar');
    expect(avatar.exists()).toBe(true);
    expect(avatar.attributes('src')).toBe(mockMessage.from.avatar);
    
    const nickname = senderSection.find('.nickname');
    expect(nickname.text()).toBe(mockMessage.from.nickname);
  });

  // MD-003: 消息内容显示
  it('MD-003: 消息内容显示', async () => {
    wrapper = mount(MessageDetail, {
      global: {
        plugins: [router, pinia]
      },
      props: {
        message: mockMessage
      }
    });

    await flushPromises();

    const content = wrapper.find('.message-content');
    expect(content.exists()).toBe(true);
    expect(content.text()).toBe(mockMessage.content);
  });

  // MD-004: 时间格式化显示
  it('MD-004: 时间格式化显示', async () => {
    wrapper = mount(MessageDetail, {
      global: {
        plugins: [router, pinia]
      },
      props: {
        message: mockMessage
      }
    });

    await flushPromises();

    const time = wrapper.find('.message-time');
    expect(time.exists()).toBe(true);
    expect(time.text()).toContain('2026');
    expect(time.text()).toContain('03-30');
  });

  // MD-005: 回复功能
  it('MD-005: 回复功能', async () => {
    wrapper = mount(MessageDetail, {
      global: {
        plugins: [router, pinia]
      },
      props: {
        message: mockMessage
      }
    });

    await flushPromises();

    const replyBtn = wrapper.find('.reply-btn');
    await replyBtn.trigger('click');

    const emitted = wrapper.emitted('reply');
    expect(emitted).toBeTruthy();
    expect(emitted[0][0]).toBe(mockMessage.id);
  });

  // MD-006: 返回导航
  it('MD-006: 返回导航', async () => {
    await router.push('/message/1');
    
    wrapper = mount(MessageDetail, {
      global: {
        plugins: [router, pinia]
      },
      props: {
        message: mockMessage
      }
    });

    await flushPromises();

    const backBtn = wrapper.find('.back-btn');
    await backBtn.trigger('click');

    expect(router.currentRoute.value.path).toBe('/');
  });

  // MD-007: 图片消息渲染
  it('MD-007: 图片消息渲染', async () => {
    const messageWithImages = {
      ...mockMessage,
      content: '看看这些图片',
      images: [
        'https://example.com/img1.jpg',
        'https://example.com/img2.jpg'
      ]
    };

    wrapper = mount(MessageDetail, {
      global: {
        plugins: [router, pinia]
      },
      props: {
        message: messageWithImages
      }
    });

    await flushPromises();

    const imageContainer = wrapper.find('.message-images');
    expect(imageContainer.exists()).toBe(true);
    
    const images = imageContainer.findAll('img');
    expect(images.length).toBe(2);
    expect(images[0].attributes('src')).toBe(messageWithImages.images[0]);
  });

  // MD-008: 链接消息渲染
  it('MD-008: 链接消息渲染', async () => {
    const messageWithLink = {
      ...mockMessage,
      content: '查看这个链接：https://example.com',
      link: {
        url: 'https://example.com',
        title: '示例链接',
        description: '这是一个示例链接的描述',
        image: 'https://example.com/preview.jpg'
      }
    };

    wrapper = mount(MessageDetail, {
      global: {
        plugins: [router, pinia]
      },
      props: {
        message: messageWithLink
      }
    });

    await flushPromises();

    const linkCard = wrapper.find('.link-card');
    expect(linkCard.exists()).toBe(true);
    expect(linkCard.find('.link-title').text()).toBe(messageWithLink.link.title);
    expect(linkCard.find('.link-description').text()).toBe(messageWithLink.link.description);
  });

  // MD-009: 加载状态
  it('MD-009: 加载状态', async () => {
    const { getMessageDetail } = await import('../../src/api/message');
    getMessageDetail.mockImplementation(() => new Promise(resolve => setTimeout(resolve, 100)));

    wrapper = mount(MessageDetail, {
      global: {
        plugins: [router, pinia]
      },
      props: {
        messageId: 1
      }
    });

    // 加载中的状态
    expect(wrapper.find('.loading-spinner').exists()).toBe(true);

    await flushPromises();

    // 加载完成
    expect(wrapper.find('.loading-spinner').exists()).toBe(false);
    expect(wrapper.find('.message-content').exists()).toBe(true);
  });

  // MD-010: 错误处理
  it('MD-010: 错误处理', async () => {
    const { getMessageDetail } = await import('../../src/api/message');
    getMessageDetail.mockRejectedValue(new Error('Message not found'));

    wrapper = mount(MessageDetail, {
      global: {
        plugins: [router, pinia]
      },
      props: {
        messageId: 999
      }
    });

    await flushPromises();

    const errorState = wrapper.find('.error-state');
    expect(errorState.exists()).toBe(true);
    expect(errorState.text()).toContain('加载失败');
    
    // 检查重试按钮
    const retryBtn = errorState.find('.retry-btn');
    expect(retryBtn.exists()).toBe(true);
  });

  // 额外测试：在线状态显示
  it('显示发送者在线状态', async () => {
    wrapper = mount(MessageDetail, {
      global: {
        plugins: [router, pinia]
      },
      props: {
        message: mockMessage
      }
    });

    await flushPromises();

    const onlineIndicator = wrapper.find('.online-indicator');
    expect(onlineIndicator.exists()).toBe(true);
  });

  // 额外测试：消息类型标签
  it('显示消息类型标签', async () => {
    wrapper = mount(MessageDetail, {
      global: {
        plugins: [router, pinia]
      },
      props: {
        message: mockMessage
      }
    });

    await flushPromises();

    const typeBadge = wrapper.find('.type-badge');
    expect(typeBadge.exists()).toBe(true);
    expect(typeBadge.text()).toBe('私信');
  });

  // 额外测试：已读/未读状态
  it('显示消息已读状态', async () => {
    const unreadMessage = { ...mockMessage, isRead: false };
    
    wrapper = mount(MessageDetail, {
      global: {
        plugins: [router, pinia]
      },
      props: {
        message: unreadMessage
      }
    });

    await flushPromises();

    const readStatus = wrapper.find('.read-status');
    expect(readStatus.exists()).toBe(true);
    expect(readStatus.text()).toContain('未读');
  });

  // 额外测试：复制消息内容
  it('支持复制消息内容', async () => {
    // Mock navigator.clipboard
    const mockClipboard = {
      writeText: vi.fn().mockResolvedValue(undefined)
    };
    Object.assign(navigator, { clipboard: mockClipboard });

    wrapper = mount(MessageDetail, {
      global: {
        plugins: [router, pinia]
      },
      props: {
        message: mockMessage
      }
    });

    await flushPromises();

    const copyBtn = wrapper.find('.copy-btn');
    await copyBtn.trigger('click');

    expect(navigator.clipboard.writeText).toHaveBeenCalledWith(mockMessage.content);
  });

  // 额外测试：举报消息
  it('支持举报消息', async () => {
    wrapper = mount(MessageDetail, {
      global: {
        plugins: [router, pinia]
      },
      props: {
        message: mockMessage
      }
    });

    await flushPromises();

    const reportBtn = wrapper.find('.report-btn');
    await reportBtn.trigger('click');

    const emitted = wrapper.emitted('report');
    expect(emitted).toBeTruthy();
    expect(emitted[0][0]).toBe(mockMessage.id);
  });

  // 额外测试：拉黑用户
  it('支持拉黑发送者', async () => {
    wrapper = mount(MessageDetail, {
      global: {
        plugins: [router, pinia]
      },
      props: {
        message: mockMessage
      }
    });

    await flushPromises();

    const blockBtn = wrapper.find('.block-btn');
    await blockBtn.trigger('click');

    const emitted = wrapper.emitted('block-user');
    expect(emitted).toBeTruthy();
    expect(emitted[0][0]).toBe(mockMessage.from.id);
  });

  // 额外测试：消息转发
  it('支持转发消息', async () => {
    wrapper = mount(MessageDetail, {
      global: {
        plugins: [router, pinia]
      },
      props: {
        message: mockMessage
      }
    });

    await flushPromises();

    const forwardBtn = wrapper.find('.forward-btn');
    await forwardBtn.trigger('click');

    const emitted = wrapper.emitted('forward');
    expect(emitted).toBeTruthy();
    expect(emitted[0][0]).toEqual(mockMessage);
  });

  // 额外测试：语音消息播放
  it('支持语音消息播放', async () => {
    const voiceMessage = {
      ...mockMessage,
      type: 'voice',
      content: '语音消息',
      voiceUrl: 'https://example.com/voice.mp3',
      voiceDuration: 30
    };

    wrapper = mount(MessageDetail, {
      global: {
        plugins: [router, pinia]
      },
      props: {
        message: voiceMessage
      }
    });

    await flushPromises();

    const voicePlayer = wrapper.find('.voice-player');
    expect(voicePlayer.exists()).toBe(true);
    expect(wrapper.find('.duration').text()).toContain('30');
  });

  // 额外测试：视频消息播放
  it('支持视频消息播放', async () => {
    const videoMessage = {
      ...mockMessage,
      type: 'video',
      content: '视频消息',
      videoUrl: 'https://example.com/video.mp4',
      videoCover: 'https://example.com/cover.jpg',
      videoDuration: 120
    };

    wrapper = mount(MessageDetail, {
      global: {
        plugins: [router, pinia]
      },
      props: {
        message: videoMessage
      }
    });

    await flushPromises();

    const videoPlayer = wrapper.find('video');
    expect(videoPlayer.exists()).toBe(true);
    expect(videoPlayer.attributes('src')).toBe(videoMessage.videoUrl);
  });

  // 额外测试：红包消息
  it('支持红包消息显示', async () => {
    const redPacketMessage = {
      ...mockMessage,
      type: 'redpacket',
      content: '恭喜发财，大吉大利',
      redPacket: {
        amount: 10.00,
        type: 'random',
        total: 100.00,
        count: 10
      }
    };

    wrapper = mount(MessageDetail, {
      global: {
        plugins: [router, pinia]
      },
      props: {
        message: redPacketMessage
      }
    });

    await flushPromises();

    const redPacketCard = wrapper.find('.redpacket-card');
    expect(redPacketCard.exists()).toBe(true);
    expect(wrapper.find('.redpacket-amount').text()).toContain('10.00');
  });
});
