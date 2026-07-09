import api from './axios';
import { TaskComment } from '../types';

export const commentsApi = {
  getComments: (taskId: number) => api.get<TaskComment[]>(`/tasks/${taskId}/comments`),
  addComment: (taskId: number, data: { content: string }) => api.post<TaskComment>(`/tasks/${taskId}/comments`, data),
};