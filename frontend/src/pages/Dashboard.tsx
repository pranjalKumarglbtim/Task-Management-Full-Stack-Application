import React from 'react';
import { Plus, Search, BarChart3, CheckCircle, AlertCircle, Target, Sparkles, Zap, Flame } from 'lucide-react';
import { useQuery, useQueryClient } from 'react-query';
import { dashboardApi } from '../api/dashboard';
import { tasksApi } from '../api/tasks';
import { TaskCard } from '../components/TaskCard';
import { Link } from 'react-router-dom';
import { useAuthStore } from '../store/authStore';
import { useForm } from 'react-hook-form';
import toast from 'react-hot-toast';

interface CreateTaskForm {
  title: string;
  description: string;
  priority: 'LOW' | 'MEDIUM' | 'HIGH' | 'CRITICAL';
}

export default function Dashboard() {
  const { user } = useAuthStore();
  const queryClient = useQueryClient();
  const [showCreateModal, setShowCreateModal] = React.useState(false);
  const [searchQuery, setSearchQuery] = React.useState('');
  
  const { register, handleSubmit, reset, formState: { errors } } = useForm<CreateTaskForm>({
    defaultValues: { priority: 'MEDIUM' }
  });

  const { data: tasksData } = useQuery('dashboard-tasks', () => tasksApi.getTasks().then(res => res.data));
  const { data: recentActivity } = useQuery('dashboard-activity', () => tasksApi.getActivity().then(res => res.data));

  const tasks = tasksData?.tasks || [];
  const filteredTasks = tasks.filter(t => t.title.toLowerCase().includes(searchQuery.toLowerCase()));
  const todoTasks = filteredTasks.filter(t => t.status === 'TODO').slice(0, 6);
  const inProgressTasks = filteredTasks.filter(t => t.status === 'IN_PROGRESS').slice(0, 6);
  const doneTasks = filteredTasks.filter(t => t.status === 'DONE').slice(0, 6);
  const activity = recentActivity?.activities || [];

  const statsData = {
    totalTasks: tasks.length,
    completedTasks: tasks.filter(t => t.status === 'DONE').length,
    pendingTasks: tasks.filter(t => t.status === 'TODO').length,
  };

  const onCreateTask = async (formData: CreateTaskForm) => {
    try {
      await tasksApi.createTask({
        title: formData.title,
        description: formData.description,
        priority: formData.priority,
      });
      queryClient.invalidateQueries('dashboard-tasks');
      toast.success('Task created!');
      setShowCreateModal(false);
      reset();
    } catch (error) {
      toast.error('Failed to create task');
    }
  };

  return (
    <div className="p-4 bg-gray-50 min-h-screen">
      <div className="max-w-7xl mx-auto">
        <div className="mb-6 p-6 rounded-3xl bg-purple-600 text-white">
          <div className="flex flex-col sm:flex-row justify-between items-start sm:items-center gap-4">
            <div>
              <h1 className="text-3xl font-bold mb-1 flex items-center gap-2">
                <Zap />
                TaskFlow Dashboard
              </h1>
              <p className="text-white/80">
                {user?.username ? `Welcome back, ${user.username}!` : 'Ready to be productive?'}
              </p>
            </div>
            <button 
              onClick={() => setShowCreateModal(true)} 
              className="flex items-center gap-2 px-4 py-2 bg-white/20 rounded-xl hover:bg-white/30 transition-colors"
            >
              <Plus size={18} />
              Create Task
            </button>
          </div>
        </div>

        <div className="grid grid-cols-2 lg:grid-cols-4 gap-4 mb-6">
          {[
            { value: statsData.totalTasks, label: 'Total', icon: BarChart3, color: 'bg-blue-500' },
            { value: statsData.completedTasks, label: 'Done', icon: CheckCircle, color: 'bg-green-500' },
            { value: statsData.pendingTasks, label: 'Pending', icon: AlertCircle, color: 'bg-amber-500' },
            { value: tasksData ? `${Math.round((statsData.completedTasks / statsData.totalTasks) * 100)}%` : '0%', label: 'Rate', icon: Target, color: 'bg-purple-500' },
          ].map((s, i) => (
            <div key={i} className="bg-white rounded-2xl p-4 shadow-sm border border-gray-100 hover:shadow-md transition-shadow">
              <div className="flex items-center gap-3">
                <div className={`w-10 h-10 rounded-lg flex items-center justify-center ${s.color}`}>
                  <s.icon size={20} className="text-white" />
                </div>
                <div>
                  <p className="text-xs text-gray-500">{s.label}</p>
                  <p className="text-xl font-bold text-gray-800">{s.value}</p>
                </div>
              </div>
            </div>
          ))}
        </div>

        <div className="relative mb-6">
          <Search className="absolute left-3 top-1/2 -translate-y-1/2 text-gray-400" size={18} />
          <input
            type="text"
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
            placeholder="Search tasks..."
            className="w-full pl-10 pr-4 py-3 border rounded-xl bg-white"
          />
        </div>

        <div className="grid grid-cols-1 lg:grid-cols-3 gap-4 mb-6">
          {[
            { title: 'TODO', tasks: todoTasks, color: 'blue' },
            { title: 'IN PROGRESS', tasks: inProgressTasks, color: 'amber' },
            { title: 'DONE', tasks: doneTasks, color: 'green' },
          ].map(col => (
            <div key={col.title} className="bg-white rounded-2xl p-4 shadow-sm border border-gray-200">
              <h3 className={`font-bold mb-3 text-${col.color}-600`}>{col.title} ({col.tasks.length})</h3>
              <div className="space-y-2 max-h-72 overflow-y-auto">
                {col.tasks.map(task => (
                  <Link key={task.id} to={`/tasks/${task.id}`}>
                    <TaskCard task={task} />
                  </Link>
                ))}
              </div>
            </div>
          ))}
        </div>

        <div className="bg-white rounded-2xl p-4 shadow-sm border border-gray-200">
          <h3 className="font-bold mb-3 flex items-center gap-2"><Sparkles size={18} className="text-purple-600" />Activity</h3>
          {activity.length > 0 ? (
            <div className="space-y-1 max-h-48 overflow-y-auto">
              {activity.slice(0, 6).map((act, i) => (
                <div key={i} className="text-sm text-gray-600 py-1 border-l-2 border-purple-200 pl-3">{act}</div>
              ))}
            </div>
          ) : <p className="text-gray-400 text-sm">No activity</p>}
        </div>

        {showCreateModal && (
          <div className="fixed inset-0 bg-black/50 flex items-center justify-center z-50 p-4">
            <div className="bg-white rounded-2xl p-6 w-full max-w-md">
              <div className="flex justify-between items-center mb-4">
                <h2 className="text-xl font-bold">Create Task</h2>
                <button onClick={() => setShowCreateModal(false)}><X size={24} /></button>
              </div>
              <form onSubmit={handleSubmit(onCreateTask)} className="space-y-4">
                <input {...register('title', { required: 'Required' })} placeholder="Title" className="w-full px-4 py-2 border rounded-xl" />
                <textarea {...register('description')} placeholder="Description" className="w-full px-4 py-2 border rounded-xl" rows={2} />
                <select {...register('priority')} className="w-full px-4 py-2 border rounded-xl">
                  <option value="MEDIUM">Medium Priority</option>
                  <option value="LOW">Low</option>
                  <option value="HIGH">High</option>
                  <option value="CRITICAL">Critical</option>
                </select>
                <div className="flex gap-2 justify-end">
                  <button type="button" onClick={() => setShowCreateModal(false)} className="px-4 py-2 bg-gray-200 rounded-xl">Cancel</button>
                  <button type="submit" className="px-4 py-2 bg-purple-600 text-white rounded-xl">Create</button>
                </div>
              </form>
            </div>
          </div>
        )}
      </div>
    </div>
  );
}