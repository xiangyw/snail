/**
 * 消息 API 接口测试
 * @module tests/message/MessageApi.test.js
 * @description 消息相关 API 的完整测试覆盖
 */

import { describe, it, expect, vi, beforeEach, afterEach } from 'vitest';
import axios from 'axios';
import MessageApi from '../../src/api/message';

// Mock axios
vi.mock('axios');
const mockedAxios = vi.mocked(axios, true);

describe('Message API', () => {
  const mockConfig = {
    baseURL: 'http://localhost:8080',
    timeout: 10000,
    headers: {
      'Content-Type': 'application/json',
      'Authorization': 'Bearer test-token'
    }
  };

  beforeEach(() => {
    mockedAxios.create.mockReturnValue(mockedAxios);
    mockedAxios.defaults = mockConfig;
    vi.clearAllMocks();
  });

  afterEach(() => {
    vi.resetAllMocks();
  });

  // MSG-API-001: 正常获取消息列表
  it('MSG-API-001: 正常获取消息列表', async () => {
    const mockResponse = {
      status: 200,
      data: {
        code: 200,
        message: 'success',
        data: {
          list: [
            { id: 1, type: 'private', content: '消息 1', isRead: false },
            { id: 2, type: 'system', content: '消息 2', isRead: true }
          ],
          total: 2,
          page: 1,
          size: 20
        }
      }
    };

    mockedAxios.get.mockResolvedValue(mockResponse);

    const result = await MessageApi.getMessageList({ page: 1, size: 20, type: 'all' });

    expect(mockedAxios.get).toHaveBeenCalledWith('/api/messages/list', {
      params: { page: 1, size: 20, type: 'all' }
    });
    expect(result.data.list).toHaveLength(2);
    expect(result.data.total).toBe(2);
  });

  // MSG-API-002: 分页参数测试
  it('MSG-API-002: 分页参数测试 (page=1,size=20)', async () => {
    const mockResponse = {
      status: 200,
      data: {
        code: 200,
        data: {
          list: Array(20).fill(null).map((_, i) => ({ id: i + 1, content: `消息${i + 1}` })),
          total: 100,
          page: 1,
          size: 20
        }
      }
    };

    mockedAxios.get.mockResolvedValue(mockResponse);

    const result = await MessageApi.getMessageList({ page: 1, size: 20 });

    expect(mockedAxios.get).toHaveBeenCalledWith('/api/messages/list', {
      params: { page: 1, size: 20 }
    });
    expect(result.data.list).toHaveLength(20);
  });

  // MSG-API-003: 按类型筛选 (private)
  it('MSG-API-003: 按类型筛选 (type=private)', async () => {
    const mockResponse = {
      status: 200,
      data: {
        code: 200,
        data: {
          list: [
            { id: 1, type: 'private', content: '私信 1' },
            { id: 2, type: 'private', content: '私信 2' }
          ],
          total: 2
        }
      }
    };

    mockedAxios.get.mockResolvedValue(mockResponse);

    const result = await MessageApi.getMessageList({ type: 'private' });

    expect(mockedAxios.get).toHaveBeenCalledWith('/api/messages/list', {
      params: { type: 'private' }
    });
    result.data.list.forEach(msg => {
      expect(msg.type).toBe('private');
    });
  });

  // MSG-API-004: 按类型筛选 (system)
  it('MSG-API-004: 按类型筛选 (type=system)', async () => {
    const mockResponse = {
      status: 200,
      data: {
        code: 200,
        data: {
          list: [
            { id: 1, type: 'system', content: '系统通知 1' },
            { id: 2, type: 'system', content: '系统通知 2' }
          ],
          total: 2
        }
      }
    };

    mockedAxios.get.mockResolvedValue(mockResponse);

    const result = await MessageApi.getMessageList({ type: 'system' });

    result.data.list.forEach(msg => {
      expect(msg.type).toBe('system');
    });
  });

  // MSG-API-005: 空列表
  it('MSG-API-005: 空列表', async () => {
    const mockResponse = {
      status: 200,
      data: {
        code: 200,
        message: 'success',
        data: {
          list: [],
          total: 0,
          page: 1,
          size: 20
        }
      }
    };

    mockedAxios.get.mockResolvedValue(mockResponse);

    const result = await MessageApi.getMessageList();

    expect(result.data.list).toHaveLength(0);
    expect(result.data.total).toBe(0);
  });

  // MSG-API-006: 未授权访问
  it('MSG-API-006: 未授权访问', async () => {
    mockedAxios.get.mockRejectedValue({
      response: {
        status: 401,
        data: {
          code: 401,
          message: 'Unauthorized'
        }
      }
    });

    await expect(MessageApi.getMessageList()).rejects.toThrow();
  });

  // MSG-API-007: 获取消息详情
  it('MSG-API-007: 获取消息详情', async () => {
    const mockResponse = {
      status: 200,
      data: {
        code: 200,
        data: {
          id: 1,
          type: 'private',
          from: { id: 1002, nickname: '好友', avatar: 'a.jpg' },
          content: '消息内容',
          isRead: false,
          createTime: '2026-03-30 10:00:00'
        }
      }
    };

    mockedAxios.get.mockResolvedValue(mockResponse);

    const result = await MessageApi.getMessageDetail(1);

    expect(mockedAxios.get).toHaveBeenCalledWith('/api/messages/1');
    expect(result.data.id).toBe(1);
    expect(result.data.content).toBe('消息内容');
  });

  // MSG-API-008: 消息不存在
  it('MSG-API-008: 消息不存在', async () => {
    mockedAxios.get.mockRejectedValue({
      response: {
        status: 404,
        data: {
          code: 404,
          message: 'Message not found'
        }
      }
    });

    await expect(MessageApi.getMessageDetail(999)).rejects.toThrow();
  });

  // MSG-API-009: 发送私信
  it('MSG-API-009: 发送私信', async () => {
    const mockResponse = {
      status: 200,
      data: {
        code: 200,
        message: '发送成功',
        data: {
          id: 1,
          type: 'private',
          content: '你好',
          createTime: '2026-03-30 12:00:00'
        }
      }
    };

    mockedAxios.post.mockResolvedValue(mockResponse);

    const result = await MessageApi.sendMessage({
      toUserId: 1002,
      content: '你好',
      type: 'text'
    });

    expect(mockedAxios.post).toHaveBeenCalledWith('/api/messages/send', {
      toUserId: 1002,
      content: '你好',
      type: 'text'
    });
    expect(result.data.content).toBe('你好');
  });

  // MSG-API-010: 发送系统消息 (仅管理员)
  it('MSG-API-010: 发送系统消息', async () => {
    const mockResponse = {
      status: 200,
      data: {
        code: 200,
        data: {
          id: 1,
          type: 'system',
          content: '系统通知'
        }
      }
    };

    mockedAxios.post.mockResolvedValue(mockResponse);

    const result = await MessageApi.sendSystemMessage({
      content: '系统通知',
      title: '重要通知'
    });

    expect(result.data.type).toBe('system');
  });

  // MSG-API-011: 内容为空
  it('MSG-API-011: 内容为空', async () => {
    mockedAxios.post.mockRejectedValue({
      response: {
        status: 400,
        data: {
          code: 400,
          message: '消息内容不能为空'
        }
      }
    });

    await expect(MessageApi.sendMessage({
      toUserId: 1002,
      content: '',
      type: 'text'
    })).rejects.toThrow();
  });

  // MSG-API-012: 接收用户不存在
  it('MSG-API-012: 接收用户不存在', async () => {
    mockedAxios.post.mockRejectedValue({
      response: {
        status: 404,
        data: {
          code: 404,
          message: '用户不存在'
        }
      }
    });

    await expect(MessageApi.sendMessage({
      toUserId: 999999,
      content: '你好',
      type: 'text'
    })).rejects.toThrow();
  });

  // MSG-API-013: 标记单条已读
  it('MSG-API-013: 标记单条已读', async () => {
    const mockResponse = {
      status: 200,
      data: {
        code: 200,
        message: 'success',
        data: {
          id: 1,
          isRead: true
        }
      }
    };

    mockedAxios.post.mockResolvedValue(mockResponse);

    const result = await MessageApi.markAsRead(1);

    expect(mockedAxios.post).toHaveBeenCalledWith('/api/messages/read', {
      messageId: 1
    });
    expect(result.data.isRead).toBe(true);
  });

  // MSG-API-014: 全部已读
  it('MSG-API-014: 全部已读', async () => {
    const mockResponse = {
      status: 200,
      data: {
        code: 200,
        message: 'success',
        data: {
          count: 5
        }
      }
    };

    mockedAxios.post.mockResolvedValue(mockResponse);

    const result = await MessageApi.markAllAsRead();

    expect(mockedAxios.post).toHaveBeenCalledWith('/api/messages/read-all');
    expect(result.data.count).toBe(5);
  });

  // MSG-API-015: 获取未读计数
  it('MSG-API-015: 获取未读计数', async () => {
    const mockResponse = {
      status: 200,
      data: {
        code: 200,
        data: {
          totalCount: 10,
          privateCount: 5,
          systemCount: 3,
          interactionCount: 2
        }
      }
    };

    mockedAxios.get.mockResolvedValue(mockResponse);

    const result = await MessageApi.getUnreadCount();

    expect(mockedAxios.get).toHaveBeenCalledWith('/api/messages/unread-count');
    expect(result.data.totalCount).toBe(10);
    expect(result.data.privateCount).toBe(5);
  });

  // MSG-API-016: 删除消息
  it('MSG-API-016: 删除消息', async () => {
    const mockResponse = {
      status: 200,
      data: {
        code: 200,
        message: '删除成功'
      }
    };

    mockedAxios.delete.mockResolvedValue(mockResponse);

    const result = await MessageApi.deleteMessage(1);

    expect(mockedAxios.delete).toHaveBeenCalledWith('/api/messages/1');
    expect(result.data.message).toBe('删除成功');
  });

  // 额外测试：批量标记已读
  it('支持批量标记已读', async () => {
    const mockResponse = {
      status: 200,
      data: {
        code: 200,
        data: {
          count: 3
        }
      }
    };

    mockedAxios.post.mockResolvedValue(mockResponse);

    const result = await MessageApi.markBatchAsRead([1, 2, 3]);

    expect(mockedAxios.post).toHaveBeenCalledWith('/api/messages/read-batch', {
      messageIds: [1, 2, 3]
    });
    expect(result.data.count).toBe(3);
  });

  // 额外测试：批量删除消息
  it('支持批量删除消息', async () => {
    const mockResponse = {
      status: 200,
      data: {
        code: 200,
        data: {
          count: 3
        }
      }
    };

    mockedAxios.post.mockResolvedValue(mockResponse);

    const result = await MessageApi.deleteBatchMessages([1, 2, 3]);

    expect(mockedAxios.post).toHaveBeenCalledWith('/api/messages/delete-batch', {
      messageIds: [1, 2, 3]
    });
    expect(result.data.count).toBe(3);
  });

  // 额外测试：搜索消息
  it('支持搜索消息', async () => {
    const mockResponse = {
      status: 200,
      data: {
        code: 200,
        data: {
          list: [
            { id: 1, content: '包含关键词的消息', highlight: '<em>关键词</em>' }
          ],
          total: 1
        }
      }
    };

    mockedAxios.get.mockResolvedValue(mockResponse);

    const result = await MessageApi.searchMessages({
      keyword: '关键词',
      page: 1,
      size: 20
    });

    expect(mockedAxios.get).toHaveBeenCalledWith('/api/messages/search', {
      params: { keyword: '关键词', page: 1, size: 20 }
    });
    expect(result.data.list).toHaveLength(1);
  });

  // 额外测试：网络错误处理
  it('网络错误处理', async () => {
    mockedAxios.get.mockRejectedValue(new Error('Network Error'));

    await expect(MessageApi.getMessageList()).rejects.toThrow('Network Error');
  });

  // 额外测试：超时处理
  it('超时处理', async () => {
    mockedAxios.get.mockRejectedValue({
      message: 'timeout of 10000ms exceeded'
    });

    await expect(MessageApi.getMessageList()).rejects.toThrow();
  });

  // 额外测试：服务器错误
  it('服务器错误 (500)', async () => {
    mockedAxios.get.mockRejectedValue({
      response: {
        status: 500,
        data: {
          code: 500,
          message: 'Internal Server Error'
        }
      }
    });

    await expect(MessageApi.getMessageList()).rejects.toThrow();
  });
});
