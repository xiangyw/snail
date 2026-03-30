import { request } from '@/utils/request';

export interface Content {
  id?: number;
  userId?: number;
  title: string;
  content: string; // 注意：统一字段名为content而不是body
  type: 'TEXT' | 'IMAGE' | 'VIDEO' | 'AUDIO' | 'LINK';
  visibility: 'DRAFT' | 'PUBLISHED' | 'ARCHIVED' | 'DELETED'; // 注意：统一字段名为visibility而不是status
  viewCount?: number;
  likeCount?: number;
  commentCount?: number;
  createdAt?: string;
  updatedAt?: string;
  publishedAt?: string;
}

// 获取所有内容列表
export const getContentList = () => {
  return request.get<Content[]>('/api/contents');
};

// 获取我的内容列表
export const getMyContentList = () => {
  return request.get<Content[]>('/api/contents/my');
};

// 根据ID获取内容详情
export const getContentById = (id: number) => {
  return request.get<Content>(`/api/contents/${id}`);
};

// 创建内容
export const createContent = (data: Omit<Content, 'id'>) => {
  return request.post<Content>('/api/contents', data);
};

// 更新内容
export const updateContent = (id: number, data: Partial<Content>) => {
  return request.put<Content>(`/api/contents/${id}`, data);
};

// 删除内容
export const deleteContent = (id: number) => {
  return request.delete(`/api/contents/${id}`);
};

// 上传文件
export const uploadFile = (file: File, type: 'image' | 'video') => {
  const formData = new FormData();
  formData.append('file', file);
  formData.append('type', type);
  
  return request.post<{ url: string; filename: string; size: number }>('/api/upload', formData, {
    headers: {
      'Content-Type': 'multipart/form-data',
    },
  });
};