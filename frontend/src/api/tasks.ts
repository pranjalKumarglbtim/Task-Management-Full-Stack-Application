import api from './axios';
import { Task, TaskCreateRequest, TaskUpdateRequest, PaginatedTasks } from '../types';

export const tasksApi = {
  getTasks: (params?: any) => api.get<PaginatedTasks>('/tasks', { params }),
  getTask: (id: number) => api.get<Task>(`/tasks/${id}`),
  createTask: (data: TaskCreateRequest) => api.post<Task>('/tasks', data),
  updateTask: (id: number, data: TaskUpdateRequest) => api.put<Task>(`/tasks/${id}`, data),
  deleteTask: (id: number) => api.delete(`/tasks/${id}`),
  updateStatus: (id: number, status: string) => api.put<Task>(`/tasks/${id}/status?status=${status}`),
  getActivity: () => api.get<{ activities: string[] }>('/tasks/activity'),
};