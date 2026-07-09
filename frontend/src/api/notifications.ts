import api from './axios';
import { Notification } from '../types';

export const notificationsApi = {
  getNotifications: () => api.get<Notification[]>('/notifications'),
  getUnreadCount: () => api.get<number>('/notifications/unread'),
  markAllAsRead: () => api.put('/notifications/read'),
};