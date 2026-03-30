/**
 * WebSocket 实时推送测试
 * @module tests/message/WebSocket.test.js
 * @description WebSocket 连接和消息推送的完整测试覆盖
 */

import { describe, it, expect, vi, beforeEach, afterEach } from 'vitest';
import WebSocketService from '../../src/utils/websocket';

describe('WebSocket Service', () => {
  let wsService;
  let mockWebSocket;
  let mockWsInstance;

  beforeEach(() => {
    vi.clearAllMocks();
    vi.useFakeTimers();

    // Mock WebSocket
    mockWsInstance = {
      send: vi.fn(),
      close: vi.fn(),
      onopen: null,
      onclose: null,
      onmessage: null,
      onerror: null,
      readyState: 1 // OPEN
    };

    mockWebSocket = vi.fn(() => mockWsInstance);
    global.WebSocket = mockWebSocket;

    wsService = new WebSocketService();
  });

  afterEach(() => {
    vi.useRealTimers();
    wsService.disconnect();
    delete global.WebSocket;
  });

  // WS-001: 建立连接
  it('WS-001: 建立连接', () => {
    wsService.connect('ws://localhost:8080/ws', { userId: 1, token: 'test-token' });

    expect(mockWebSocket).toHaveBeenCalledWith('ws://localhost:8080/ws?userId=1&token=test-token');
    expect(wsService.isConnected()).toBe(true);
  });

  // WS-002: 接收新消息
  it('WS-002: 接收新消息', (done) => {
    const testMessage = {
      type: 'MESSAGE',
      data: {
        id: 1,
        content: '新消息',
        from: { id: 1002, nickname: '好友' }
      }
    };

    wsService.on('message', (message) => {
      expect(message).toEqual(testMessage);
      done();
    });

    wsService.connect('ws://localhost:8080/ws', { userId: 1 });
    
    // 模拟连接成功
    mockWsInstance.onopen();
    
    // 模拟接收消息
    mockWsInstance.onmessage({
      data: JSON.stringify(testMessage)
    });
  });

  // WS-003: 接收系统通知
  it('WS-003: 接收系统通知', (done) => {
    const testNotification = {
      type: 'NOTIFICATION',
      data: {
        id: 1,
        title: '系统通知',
        content: '欢迎使用 Snail'
      }
    };

    wsService.on('notification', (notification) => {
      expect(notification).toEqual(testNotification);
      done();
    });

    wsService.connect('ws://localhost:8080/ws', { userId: 1 });
    mockWsInstance.onopen();
    
    mockWsInstance.onmessage({
      data: JSON.stringify(testNotification)
    });
  });

  // WS-004: 消息类型识别
  it('WS-004: 消息类型识别', () => {
    const messageTypes = ['MESSAGE', 'NOTIFICATION', 'SYSTEM', 'CHAT', 'GIFT'];
    const receivedTypes = [];

    messageTypes.forEach(type => {
      wsService.on(type.toLowerCase(), (data) => {
        receivedTypes.push(type);
      });
    });

    wsService.connect('ws://localhost:8080/ws', { userId: 1 });
    mockWsInstance.onopen();

    messageTypes.forEach(type => {
      mockWsInstance.onmessage({
        data: JSON.stringify({ type, data: {} })
      });
    });

    expect(receivedTypes).toEqual(messageTypes);
  });

  // WS-005: 消息内容解析
  it('WS-005: 消息内容解析', (done) => {
    const complexMessage = {
      type: 'MESSAGE',
      data: {
        id: 1,
        content: '测试消息',
        from: { id: 1002, nickname: '好友', avatar: 'a.jpg' },
        images: ['url1', 'url2'],
        timestamp: Date.now()
      }
    };

    wsService.on('message', (message) => {
      expect(message.data.id).toBe(1);
      expect(message.data.content).toBe('测试消息');
      expect(message.data.from.nickname).toBe('好友');
      expect(message.data.images).toHaveLength(2);
      expect(typeof message.data.timestamp).toBe('number');
      done();
    });

    wsService.connect('ws://localhost:8080/ws', { userId: 1 });
    mockWsInstance.onopen();
    
    mockWsInstance.onmessage({
      data: JSON.stringify(complexMessage)
    });
  });

  // WS-006: 推送消息展示
  it('WS-006: 推送消息展示', (done) => {
    const newMessage = {
      type: 'MESSAGE',
      data: {
        id: 1,
        content: '新消息',
        isRead: false
      }
    };

    wsService.on('message', (message) => {
      // 验证消息会被传递到 UI 层
      expect(message.data.isRead).toBe(false);
      expect(message.data.content).toBe('新消息');
      done();
    });

    wsService.connect('ws://localhost:8080/ws', { userId: 1 });
    mockWsInstance.onopen();
    mockWsInstance.onmessage({ data: JSON.stringify(newMessage) });
  });

  // WS-007: 未读计数更新
  it('WS-007: 未读计数更新', (done) => {
    const unreadUpdate = {
      type: 'UNREAD_COUNT',
      data: {
        totalCount: 5,
        messageCount: 3,
        notificationCount: 2
      }
    };

    wsService.on('unread-count', (count) => {
      expect(count.totalCount).toBe(5);
      expect(count.messageCount).toBe(3);
      expect(count.notificationCount).toBe(2);
      done();
    });

    wsService.connect('ws://localhost:8080/ws', { userId: 1 });
    mockWsInstance.onopen();
    mockWsInstance.onmessage({ data: JSON.stringify(unreadUpdate) });
  });

  // WS-008: 多消息并发
  it('WS-008: 多消息并发', (done) => {
    const messages = Array(10).fill(null).map((_, i) => ({
      type: 'MESSAGE',
      data: { id: i + 1, content: `消息${i + 1}` }
    }));

    let receivedCount = 0;

    wsService.on('message', () => {
      receivedCount++;
      if (receivedCount === 10) {
        expect(receivedCount).toBe(10);
        done();
      }
    });

    wsService.connect('ws://localhost:8080/ws', { userId: 1 });
    mockWsInstance.onopen();

    messages.forEach(msg => {
      mockWsInstance.onmessage({ data: JSON.stringify(msg) });
    });
  });

  // WS-009: 大消息处理
  it('WS-009: 大消息处理', (done) => {
    const largeMessage = {
      type: 'MESSAGE',
      data: {
        id: 1,
        content: 'a'.repeat(10000), // 10KB 内容
        images: Array(20).fill('url')
      }
    };

    wsService.on('message', (message) => {
      expect(message.data.content.length).toBe(10000);
      expect(message.data.images).toHaveLength(20);
      done();
    });

    wsService.connect('ws://localhost:8080/ws', { userId: 1 });
    mockWsInstance.onopen();
    mockWsInstance.onmessage({ data: JSON.stringify(largeMessage) });
  });

  // WS-010: 特殊字符处理
  it('WS-010: 特殊字符处理', (done) => {
    const specialMessage = {
      type: 'MESSAGE',
      data: {
        id: 1,
        content: '特殊字符：<>""\'\'&\n\t\r 表情😀🎉'
      }
    };

    wsService.on('message', (message) => {
      expect(message.data.content).toContain('<>');
      expect(message.data.content).toContain('表情');
      done();
    });

    wsService.connect('ws://localhost:8080/ws', { userId: 1 });
    mockWsInstance.onopen();
    mockWsInstance.onmessage({ data: JSON.stringify(specialMessage) });
  });

  // WS-011: 断线重连
  it('WS-011: 断线重连', () => {
    const reconnectSpy = vi.fn();
    wsService.on('reconnecting', reconnectSpy);

    wsService.connect('ws://localhost:8080/ws', { userId: 1 });
    mockWsInstance.onopen();

    // 模拟断开
    mockWsInstance.readyState = 3; // CLOSED
    mockWsInstance.onclose({ code: 1006, reason: 'Abnormal Closure' });

    vi.advanceTimersByTime(1000); // 等待重连

    expect(reconnectSpy).toHaveBeenCalled();
    expect(mockWebSocket).toHaveBeenCalledTimes(2); // 初始连接 + 重连
  });

  // WS-012: 心跳检测
  it('WS-012: 心跳检测', () => {
    wsService.connect('ws://localhost:8080/ws', { userId: 1 }, { heartbeatInterval: 30000 });
    mockWsInstance.onopen();

    vi.advanceTimersByTime(30000);

    expect(mockWsInstance.send).toHaveBeenCalledWith(JSON.stringify({
      type: 'PING',
      timestamp: expect.any(Number)
    }));
  });

  // WS-013: 心跳超时处理
  it('WS-013: 心跳超时处理', () => {
    const disconnectSpy = vi.fn();
    wsService.on('disconnect', disconnectSpy);

    wsService.connect('ws://localhost:8080/ws', { userId: 1 }, {
      heartbeatInterval: 10000,
      heartbeatTimeout: 5000
    });
    mockWsInstance.onopen();

    // 发送心跳
    vi.advanceTimersByTime(10000);
    
    // 不响应 PONG，等待超时
    vi.advanceTimersByTime(5000);

    expect(disconnectSpy).toHaveBeenCalled();
  });

  // WS-014: 网络切换
  it('WS-014: 网络切换处理', () => {
    wsService.connect('ws://localhost:8080/ws', { userId: 1 });
    mockWsInstance.onopen();

    // 模拟网络断开
    mockWsInstance.readyState = 3;
    mockWsInstance.onclose({ code: 1006 });

    // 模拟网络恢复
    global.dispatchEvent(new Event('online'));

    vi.advanceTimersByTime(1000);

    expect(mockWebSocket).toHaveBeenCalledTimes(2);
  });

  // WS-015: 连接超时
  it('WS-015: 连接超时', (done) => {
    wsService.on('error', (error) => {
      expect(error.message).toContain('timeout');
      done();
    });

    wsService.connect('ws://localhost:8080/ws', { userId: 1 }, { timeout: 5000 });
    
    // 模拟超时
    vi.advanceTimersByTime(5000);
  });

  // WS-016: 错误处理
  it('WS-016: 错误处理', (done) => {
    wsService.on('error', (error) => {
      expect(error).toBeDefined();
      done();
    });

    wsService.connect('ws://localhost:8080/ws', { userId: 1 });
    
    mockWsInstance.onerror(new Error('Connection failed'));
  });

  // WS-017: 重连间隔
  it('WS-017: 重连间隔递增', () => {
    wsService.connect('ws://localhost:8080/ws', { userId: 1 }, {
      reconnectInterval: 1000,
      maxReconnectInterval: 10000
    });
    mockWsInstance.onopen();

    // 第一次重连
    mockWsInstance.readyState = 3;
    mockWsInstance.onclose({ code: 1006 });
    vi.advanceTimersByTime(1000);
    expect(mockWebSocket).toHaveBeenCalledTimes(2);

    // 第二次重连
    mockWsInstance.readyState = 3;
    mockWsInstance.onclose({ code: 1006 });
    vi.advanceTimersByTime(2000);
    expect(mockWebSocket).toHaveBeenCalledTimes(3);

    // 第三次重连
    mockWsInstance.readyState = 3;
    mockWsInstance.onclose({ code: 1006 });
    vi.advanceTimersByTime(4000);
    expect(mockWebSocket).toHaveBeenCalledTimes(4);
  });

  // WS-018: 页面关闭清理
  it('WS-018: 页面关闭清理', () => {
    wsService.connect('ws://localhost:8080/ws', { userId: 1 });
    mockWsInstance.onopen();

    // 模拟页面关闭
    global.dispatchEvent(new Event('beforeunload'));

    expect(mockWsInstance.close).toHaveBeenCalled();
  });

  // 额外测试：发送消息
  it('支持发送消息', () => {
    wsService.connect('ws://localhost:8080/ws', { userId: 1 });
    mockWsInstance.onopen();

    const message = {
      type: 'CHAT',
      data: { content: 'Hello', toUserId: 1002 }
    };

    wsService.send(message);

    expect(mockWsInstance.send).toHaveBeenCalledWith(JSON.stringify(message));
  });

  // 额外测试：订阅主题
  it('支持订阅主题', () => {
    wsService.connect('ws://localhost:8080/ws', { userId: 1 });
    mockWsInstance.onopen();

    wsService.subscribe(['message:1002', 'notification:system']);

    expect(mockWsInstance.send).toHaveBeenCalledWith(JSON.stringify({
      type: 'SUBSCRIBE',
      channels: ['message:1002', 'notification:system']
    }));
  });

  // 额外测试：取消订阅
  it('支持取消订阅', () => {
    wsService.connect('ws://localhost:8080/ws', { userId: 1 });
    mockWsInstance.onopen();

    wsService.unsubscribe(['message:1002']);

    expect(mockWsInstance.send).toHaveBeenCalledWith(JSON.stringify({
      type: 'UNSUBSCRIBE',
      channels: ['message:1002']
    }));
  });

  // 额外测试：连接状态监听
  it('支持连接状态监听', () => {
    const connectSpy = vi.fn();
    const disconnectSpy = vi.fn();

    wsService.on('connected', connectSpy);
    wsService.on('disconnected', disconnectSpy);

    wsService.connect('ws://localhost:8080/ws', { userId: 1 });
    mockWsInstance.onopen();

    expect(connectSpy).toHaveBeenCalled();

    mockWsInstance.onclose({ code: 1000 });
    expect(disconnectSpy).toHaveBeenCalled();
  });

  // 额外测试：重连次数限制
  it('重连次数限制', () => {
    wsService.connect('ws://localhost:8080/ws', { userId: 1 }, {
      maxReconnectAttempts: 3
    });
    mockWsInstance.onopen();

    // 连续断开 3 次
    for (let i = 0; i < 3; i++) {
      mockWsInstance.readyState = 3;
      mockWsInstance.onclose({ code: 1006 });
      vi.advanceTimersByTime(1000 * Math.pow(2, i));
    }

    // 第 4 次断开后不再重连
    mockWsInstance.readyState = 3;
    mockWsInstance.onclose({ code: 1006 });
    vi.advanceTimersByTime(8000);

    expect(mockWebSocket).toHaveBeenCalledTimes(4); // 初始 + 3 次重连
  });

  // 额外测试：手动断开
  it('支持手动断开', () => {
    wsService.connect('ws://localhost:8080/ws', { userId: 1 });
    mockWsInstance.onopen();

    wsService.disconnect();

    expect(mockWsInstance.close).toHaveBeenCalledWith(1000, 'Normal Closure');
    expect(wsService.isConnected()).toBe(false);
  });
});
