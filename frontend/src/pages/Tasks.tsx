import React, { useState, useMemo } from 'react';
import { useQuery, useQueryClient } from 'react-query';
import { Plus, Grid3x3, List } from 'lucide-react';
import { tasksApi } from '../api/tasks';
import { TaskCard } from '../components/TaskCard';
import toast from 'react-hot-toast';

type ViewMode = 'grid' | 'list';

export default function Tasks() {
  const queryClient = useQueryClient();
  const [showCreateModal, setShowCreateModal] = useState(false);
  const [viewMode, setViewMode] = useState<ViewMode>('grid');
  const [searchQuery, setSearchQuery] = useState('');
  const [statusFilter, setStatusFilter] = useState<string>('all');
  const [formData, setFormData] = useState({ title: '', description: '', priority: 'MEDIUM' });
  
  const { data, isLoading } = useQuery('tasks', () => tasksApi.getTasks().then(res => res.data));

  const filteredTasks = useMemo(() => {
    if (!data?.tasks) return [];
    return data.tasks.filter(task => {
      const matchesSearch = task.title.toLowerCase().includes(searchQuery.toLowerCase()) ||
                           task.description?.toLowerCase().includes(searchQuery.toLowerCase());
      const matchesStatus = statusFilter === 'all' || task.status === statusFilter;
      return matchesSearch && matchesStatus;
    });
  }, [data?.tasks, searchQuery, statusFilter]);

  const handleCreateTask = async () => {
    try {
      await tasksApi.createTask(formData);
      queryClient.invalidateQueries('tasks');
      toast.success('Task created!');
      setShowCreateModal(false);
      setFormData({ title: '', description: '', priority: 'MEDIUM' });
    } catch (error) {
      toast.error('Failed to create task');
    }
  };

  if (isLoading) return (
    <div className="p-4">
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-3">
        {[1, 2, 3].map(i => (
          <div key={i} className="h-24 bg-gray-200 rounded-xl animate-pulse"></div>
        ))}
      </div>
    </div>
  );

  return (
    <div className="p-4 bg-gray-50 min-h-screen">
      <div className="flex flex-col sm:flex-row justify-between items-start sm:items-center mb-4 gap-4">
        <h1 className="text-xl font-bold">Tasks ({filteredTasks.length})</h1>
        <button 
          onClick={() => setShowCreateModal(true)} 
          className="flex items-center gap-2 px-4 py-2 bg-purple-600 text-white rounded-xl"
        >
          <Plus size={16} /> Add
        </button>
      </div>

      <div className="flex flex-col sm:flex-row gap-4 mb-4">
        <div className="flex-1 flex items-center gap-2">
          <input
            type="text"
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
            placeholder="Search..."
            className="flex-1 px-3 py-2 border rounded-xl"
          />
          <select
            value={statusFilter}
            onChange={(e) => setStatusFilter(e.target.value)}
            className="px-3 py-2 border rounded-xl"
          >
            <option value="all">All</option>
            <option value="TODO">Todo</option>
            <option value="IN_PROGRESS">Progress</option>
            <option value="DONE">Done</option>
          </select>
        </div>
        <div className="flex bg-white p-1 rounded-lg">
          <button onClick={() => setViewMode('grid')} className={`p-1.5 rounded ${viewMode === 'grid' ? 'bg-purple-100' : ''}`}>
            <Grid3x3 size={16} />
          </button>
          <button onClick={() => setViewMode('list')} className={`p-1.5 rounded ${viewMode === 'list' ? 'bg-purple-100' : ''}`}>
            <List size={16} />
          </button>
        </div>
      </div>

      <div className={viewMode === 'grid' ? "grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-3" : "space-y-2"}>
        {filteredTasks.map((task) => (
          <TaskCard key={task.id} task={task} />
        ))}
      </div>

      {showCreateModal && (
        <div className="fixed inset-0 bg-black/50 flex items-center justify-center z-50 p-4">
          <div className="bg-white rounded-2xl p-6 w-full max-w-sm">
            <div className="flex justify-between mb-4">
              <h2 className="font-bold">New Task</h2>
              <button onClick={() => setShowCreateModal(false)}>X</button>
            </div>
            <div className="space-y-3">
              <input 
                value={formData.title}
                onChange={(e) => setFormData({...formData, title: e.target.value})}
                placeholder="Title" 
                className="w-full px-3 py-2 border rounded-xl" 
              />
              <textarea 
                value={formData.description}
                onChange={(e) => setFormData({...formData, description: e.target.value})}
                placeholder="Description" 
                className="w-full px-3 py-2 border rounded-xl" 
                rows={2} 
              />
              <select 
                value={formData.priority}
                onChange={(e) => setFormData({...formData, priority: e.target.value as any})}
                className="w-full px-3 py-2 border rounded-xl"
              >
                <option value="MEDIUM">Medium</option>
                <option value="LOW">Low</option>
                <option value="HIGH">High</option>
                <option value="CRITICAL">Critical</option>
              </select>
              <div className="flex gap-2 justify-end">
                <button onClick={() => setShowCreateModal(false)} className="px-4 py-2 bg-gray-200 rounded-xl">Cancel</button>
                <button onClick={handleCreateTask} className="px-4 py-2 bg-purple-600 text-white rounded-xl">Create</button>
              </div>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}