export * from './auth';
export * from './task';
export * from './store';

export interface User {
  id: number;
  username: string;
  email: string;
  role: string;
}

export interface Task {
  id: number;
  title: string;
  description: string;
  status: 'TODO' | 'IN_PROGRESS' | 'REVIEW' | 'DONE';
  priority: 'LOW' | 'MEDIUM' | 'HIGH' | 'CRITICAL';
  dueDate: string | null;
  assignee: User | null;
  creator: User;
  createdAt: string;
  updatedAt: string | null;
  commentCount: number;
}

export interface Notification {
  id: number;
  title: string;
  message: string;
  type: string;
  taskId: number | null;
  read: boolean;
  createdAt: string;
}

export interface AuthTokens {
  accessToken: string;
  refreshToken: string;
}

export interface PaginatedTasks {
  tasks: Task[];
  totalElements: number;
  totalPages: number;
  currentPage: number;
  size: number;
  hasNext: boolean;
  hasPrevious: boolean;
}

export interface DashboardStats {
  totalTasks: number;
  completedTasks: number;
  pendingTasks: number;
  overdueTasks: number;
  recentActivities: Array<{
    taskId: number;
    title: string;
    action: string;
    username: string;
    timestamp: string;
  }>;
  upcomingDeadlines: Array<{
    taskId: number;
    title: string;
    assignee: string;
    dueDate: string;
  }>;
  productivityMetrics: {
    completionRate: number;
    tasksCompletedThisWeek: number;
    tasksCreatedThisWeek: number;
  };
}