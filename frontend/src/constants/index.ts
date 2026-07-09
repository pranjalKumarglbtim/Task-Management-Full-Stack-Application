export const TASK_STATUS = {
  TODO: 'TODO',
  IN_PROGRESS: 'IN_PROGRESS',
  REVIEW: 'REVIEW',
  DONE: 'DONE',
} as const;

export const TASK_PRIORITY = {
  LOW: 'LOW',
  MEDIUM: 'MEDIUM',
  HIGH: 'HIGH',
  CRITICAL: 'CRITICAL',
} as const;

export const API_ENDPOINTS = {
  AUTH: '/auth',
  TASKS: '/tasks',
  COMMENTS: '/comments',
  NOTIFICATIONS: '/notifications',
  DASHBOARD: '/dashboard',
};

export const ROUTES = {
  LOGIN: '/login',
  REGISTER: '/register',
  DASHBOARD: '/dashboard',
  TASKS: '/tasks',
  TASK_BOARD: '/tasks/board',
  PROFILE: '/profile',
};

export const PAGINATION = {
  PAGE_SIZE: 10,
  PAGE_SIZES: [10, 20, 50],
};