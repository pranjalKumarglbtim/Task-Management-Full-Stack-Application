import api from './axios';
import { DashboardStats } from '../types';

export const dashboardApi = {
  getStats: () => api.get<DashboardStats>('/tasks/stats'),
};