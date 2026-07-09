import { create } from 'zustand';
import { Task } from '../types';

interface TaskState {
  tasks: Task[];
  selectedTask: Task | null;
  setTasks: (tasks: Task[]) => void;
  addTask: (task: Task) => void;
  updateTask: (task: Task) => void;
  deleteTask: (taskId: number) => void;
  selectTask: (task: Task | null) => void;
}

export const useTaskStore = create<TaskState>((set) => ({
  tasks: [],
  selectedTask: null,
  setTasks: (tasks) => set({ tasks }),
  addTask: (task) => set((state) => ({ tasks: [...state.tasks, task] })),
  updateTask: (task) => set((state) => ({
    tasks: state.tasks.map(t => t.id === task.id ? task : t)
  })),
  deleteTask: (taskId) => set((state) => ({
    tasks: state.tasks.filter(t => t.id !== taskId)
  })),
  selectTask: (task) => set({ selectedTask: task }),
}));