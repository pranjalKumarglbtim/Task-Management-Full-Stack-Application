export interface TaskCreateRequest {
  title: string;
  description: string;
  priority: 'LOW' | 'MEDIUM' | 'HIGH' | 'CRITICAL';
  dueDate?: string;
  assigneeId?: number;
}

export interface TaskUpdateRequest {
  title?: string;
  description?: string;
  status?: 'TODO' | 'IN_PROGRESS' | 'REVIEW' | 'DONE';
  priority?: 'LOW' | 'MEDIUM' | 'HIGH' | 'CRITICAL';
  dueDate?: string | null;
  assigneeId?: number | null;
}

export interface TaskComment {
  id: number;
  content: string;
  taskId: number;
  author: {
    id: number;
    username: string;
  };
  createdAt: string;
}