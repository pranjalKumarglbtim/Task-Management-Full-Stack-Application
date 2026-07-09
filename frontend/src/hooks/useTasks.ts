import { useQuery, useQueryClient } from 'react-query';
import { tasksApi } from '../api/tasks';
import { useTaskStore } from '../store/taskStore';

export const useTasks = (params?: any) => {
  const setTasks = useTaskStore((state) => state.setTasks);

  return useQuery(['tasks', params], () => tasksApi.getTasks(params), {
    onSuccess: (data) => {
      setTasks(data.data.tasks);
    },
  });
};