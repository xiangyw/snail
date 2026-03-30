/**
 * 通知 API 接口测试
 * @module tests/message/NotificationApi.test.js
 * @description 通知相关 API 的完整测试覆盖
 */

import { describe, it, expect, vi, beforeEach, afterEach } from 'vitest';
import axios from 'axios';
import NotificationApi from '../../src/api/notification';

// Mock axios
vi.mock('axios');
const mockedAxios = vi.mocked(axios, true);

describe('Notification API', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  afterEach(() => {
    vi.resetAllMocks();
  });

  // NOTIF-API-001: 获取通知列表
  it('NOTIF-API-001: 获取通知列表', async () => {
    const mockResponse = {
      status: 200,
      data: {
        code: 200,
        message: 'success',
        data: {
          list: [
            {
              id: 1,
              type: 'system',
              title: '系统通知',
              content: '欢迎使用 Snail',
              isRead: false,
              createTime: '2026-03-30 10:00:00'
            },
            {
              id: 2,
              type: 'live',
              title: '开播提醒',
              content: '关注的主播开播了',
              isRead: true,
              createTime: '2026-03-30 09:00:00'
            }
          ],
          total: 2,
          unreadCount: 1
        }
      }
    };

    mockedAxios.get.mockResolvedValue(mockResponse);

    const result = await NotificationApi.getNotifications({ page: 1, size: 20 });

    expect(mockedAxios.get).toHaveBeenCalledWith('/api/notifications', {
      params: { page: 1, size: 20 }
    });
    expect(result.data.list).toHaveLength(2);
    expect(result.data.unreadCount).toBe(1);
  });

  // NOTIF-API-002: 分页测试
  it('NOTIF-API-002: 分页测试', async () => {
    const mockResponse = {
      status: 200,
      data: {
        code: 200,
        data: {
          list: Array(20).fill(null).map((_, i) => ({ id: i + 1, title: `通知${i + 1}` })),
          total: 100,
          page: 1,
          size: 20
        }
      }
    };

    mockedAxios.get.mockResolvedValue(mockResponse);

    const result = await NotificationApi.getNotifications({ page: 1, size: 20 });

    expect(result.data.list).toHaveLength(20);
    expect(result.data.total).toBe(100);
  });

  // NOTIF-API-003: 按类型筛选
  it('NOTIF-API-003: 按类型筛选', async () => {
    const mockResponse = {
      status: 200,
      data: {
        code: 200,
        data: {
          list: [
            { id: 1, type: 'live', title: '直播通知 1' },
            { id: 2, type: 'live', title: '直播通知 2' }
          ],
          total: 2
        }
      }
    };

    mockedAxios.get.mockResolvedValue(mockResponse);

    const result = await NotificationApi.getNotifications({ type: 'live' });

    expect(mockedAxios.get).toHaveBeenCalledWith('/api/notifications', {
      params: { type: 'live' }
    });
    result.data.list.forEach(notif => {
      expect(notif.type).toBe('live');
    });
  });

  // NOTIF-API-004: 获取通知详情
  it('NOTIF-API-004: 获取通知详情', async () => {
    const mockResponse = {
      status: 200,
      data: {
        code: 200,
        data: {
          id: 1,
          type: 'system',
          title: '系统通知',
          content: '详细内容',
          extra: { link: 'https://example.com' },
          isRead: false,
          createTime: '2026-03-30 10:00:00'
        }
      }
    };

    mockedAxios.get.mockResolvedValue(mockResponse);

    const result = await NotificationApi.getNotificationDetail(1);

    expect(mockedAxios.get).toHaveBeenCalledWith('/api/notifications/1');
    expect(result.data.id).toBe(1);
    expect(result.data.content).toBe('详细内容');
  });

  // NOTIF-API-005: 通知不存在
  it('NOTIF-API-005: 通知不存在', async () => {
    mockedAxios.get.mockRejectedValue({
      response: {
        status: 404,
        data: {
          code: 404,
          message: 'Notification not found'
        }
      }
    });

    await expect(NotificationApi.getNotificationDetail(999)).rejects.toThrow();
  });

  // NOTIF-API-006: 标记单条已读
  it('NOTIF-API-006: 标记单条已读', async () => {
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

    const result = await NotificationApi.markAsRead(1);

    expect(mockedAxios.post).toHaveBeenCalledWith('/api/notifications/read', {
      notificationId: 1
    });
    expect(result.data.isRead).toBe(true);
  });

  // NOTIF-API-007: 全部已读
  it('NOTIF-API-007: 全部已读', async () => {
    const mockResponse = {
      status: 200,
      data: {
        code: 200,
        message: 'success',
        data: {
          count: 10
        }
      }
    };

    mockedAxios.post.mockResolvedValue(mockResponse);

    const result = await NotificationApi.markAllAsRead();

    expect(mockedAxios.post).toHaveBeenCalledWith('/api/notifications/read-all');
    expect(result.data.count).toBe(10);
  });

  // NOTIF-API-008: 获取未读计数
  it('NOTIF-API-008: 获取未读计数', async () => {
    const mockResponse = {
      status: 200,
      data: {
        code: 200,
        data: {
          totalCount: 15,
          systemCount: 5,
          liveCount: 7,
          orderCount: 3
        }
      }
    };

    mockedAxios.get.mockResolvedValue(mockResponse);

    const result = await NotificationApi.getUnreadCount();

    expect(mockedAxios.get).toHaveBeenCalledWith('/api/notifications/unread-count');
    expect(result.data.totalCount).toBe(15);
    expect(result.data.liveCount).toBe(7);
  });

  // NOTIF-API-009: 未授权访问
  it('NOTIF-API-009: 未授权访问', async () => {
    mockedAxios.get.mockRejectedValue({
      response: {
        status: 401,
        data: {
          code: 401,
          message: 'Unauthorized'
        }
      }
    });

    await expect(NotificationApi.getNotifications()).rejects.toThrow();
  });

  // NOTIF-API-010: 空列表
  it('NOTIF-API-010: 空列表', async () => {
    const mockResponse = {
      status: 200,
      data: {
        code: 200,
        message: 'success',
        data: {
          list: [],
          total: 0,
          unreadCount: 0
        }
      }
    };

    mockedAxios.get.mockResolvedValue(mockResponse);

    const result = await NotificationApi.getNotifications();

    expect(result.data.list).toHaveLength(0);
    expect(result.data.unreadCount).toBe(0);
  });

  // 额外测试：批量标记已读
  it('支持批量标记已读', async () => {
    const mockResponse = {
      status: 200,
      data: {
        code: 200,
        data: {
          count: 5
        }
      }
    };

    mockedAxios.post.mockResolvedValue(mockResponse);

    const result = await NotificationApi.markBatchAsRead([1, 2, 3, 4, 5]);

    expect(mockedAxios.post).toHaveBeenCalledWith('/api/notifications/read-batch', {
      notificationIds: [1, 2, 3, 4, 5]
    });
    expect(result.data.count).toBe(5);
  });

  // 额外测试：删除通知
  it('支持删除通知', async () => {
    const mockResponse = {
      status: 200,
      data: {
        code: 200,
        message: '删除成功'
      }
    };

    mockedAxios.delete.mockResolvedValue(mockResponse);

    const result = await NotificationApi.deleteNotification(1);

    expect(mockedAxios.delete).toHaveBeenCalledWith('/api/notifications/1');
    expect(result.data.message).toBe('删除成功');
  });

  // 额外测试：批量删除通知
  it('支持批量删除通知', async () => {
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

    const result = await NotificationApi.deleteBatchNotifications([1, 2, 3]);

    expect(mockedAxios.post).toHaveBeenCalledWith('/api/notifications/delete-batch', {
      notificationIds: [1, 2, 3]
    });
    expect(result.data.count).toBe(3);
  });

  // 额外测试：清空所有通知
  it('支持清空所有通知', async () => {
    const mockResponse = {
      status: 200,
      data: {
        code: 200,
        data: {
          count: 50
        }
      }
    };

    mockedAxios.post.mockResolvedValue(mockResponse);

    const result = await NotificationApi.clearAll();

    expect(mockedAxios.post).toHaveBeenCalledWith('/api/notifications/clear-all');
    expect(result.data.count).toBe(50);
  });

  // 额外测试：按类型清空
  it('支持按类型清空', async () => {
    const mockResponse = {
      status: 200,
      data: {
        code: 200,
        data: {
          count: 20
        }
      }
    };

    mockedAxios.post.mockResolvedValue(mockResponse);

    const result = await NotificationApi.clearByType('live');

    expect(mockedAxios.post).toHaveBeenCalledWith('/api/notifications/clear-by-type', {
      type: 'live'
    });
    expect(result.data.count).toBe(20);
  });

  // 额外测试：通知设置获取
  it('获取通知设置', async () => {
    const mockResponse = {
      status: 200,
      data: {
        code: 200,
        data: {
          enableSystem: true,
          enableLive: true,
          enableOrder: true,
          enableSound: true,
          enableVibrate: false
        }
      }
    };

    mockedAxios.get.mockResolvedValue(mockResponse);

    const result = await NotificationApi.getSettings();

    expect(mockedAxios.get).toHaveBeenCalledWith('/api/notifications/settings');
    expect(result.data.enableSystem).toBe(true);
  });

  // 额外测试：更新通知设置
  it('更新通知设置', async () => {
    const mockResponse = {
      status: 200,
      data: {
        code: 200,
        message: '设置已更新'
      }
    };

    mockedAxios.post.mockResolvedValue(mockResponse);

    const result = await NotificationApi.updateSettings({
      enableSystem: true,
      enableLive: false,
      enableSound: false
    });

    expect(mockedAxios.post).toHaveBeenCalledWith('/api/notifications/settings', {
      enableSystem: true,
      enableLive: false,
      enableSound: false
    });
  });

  // 额外测试：网络错误
  it('网络错误处理', async () => {
    mockedAxios.get.mockRejectedValue(new Error('Network Error'));

    await expect(NotificationApi.getNotifications()).rejects.toThrow('Network Error');
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

    await expect(NotificationApi.getNotifications()).rejects.toThrow();
  });
});
