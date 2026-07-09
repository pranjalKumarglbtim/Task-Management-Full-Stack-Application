import React from 'react';
import { useParams, Link } from 'react-router-dom';
import { useQuery } from 'react-query';
import { tasksApi } from '../api/tasks';
import { ArrowLeft, Calendar, Flag, User, MessageSquare, Edit2 } from 'lucide-react';

export default function TaskDetail() {
  const { id } = useParams();
  const { data } = useQuery(['task', id], () => tasksApi.getTask(Number(id)));

  if (!data?.data) return <div className="p-6">Loading...</div>;

  const priorityColors: Record<string, string> = {
    LOW: 'bg-green-100 text-green-700',
    MEDIUM: 'bg-yellow-100 text-yellow-700',
    HIGH: 'bg-orange-100 text-orange-700',
    CRITICAL: 'bg-red-100 text-red-700',
  };

  return (
    <div className="p-6 bg-gray-50 min-h-screen">
      <div className="max-w-3xl mx-auto">
        <Link to="/tasks" className="inline-flex items-center gap-2 text-gray-600 hover:text-purple-600 mb-4">
          <ArrowLeft size={16} />
          Back to Tasks
        </Link>

        <div className="bg-white rounded-2xl shadow-xl p-8 border border-gray-200">
          <div className="flex justify-between items-start mb-6">
            <h1 className="text-3xl font-bold text-gray-800">{data.data.title}</h1>
            <button className="p-2 text-purple-600 hover:bg-purple-50 rounded-lg">
              <Edit2 size={20} />
            </button>
          </div>

          {data.data.description && (
            <p className="text-gray-600 mb-6">{data.data.description}</p>
          )}

          <div className="grid grid-cols-2 gap-4 mb-6">
            <div className="p-4 bg-gray-50 rounded-xl">
              <span className="text-xs text-gray-500 uppercase">Status</span>
              <p className="font-semibold mt-1">{data.data.status}</p>
            </div>
            <div className="p-4 bg-gray-50 rounded-xl">
              <span className="text-xs text-gray-500 uppercase flex items-center gap-1">
                <Flag size={14} /> Priority
              </span>
              <span className={`inline-block px-2 py-1 rounded-full text-xs font-medium mt-2 ${priorityColors[data.data.priority]}`}>
                {data.data.priority}
              </span>
            </div>
          </div>

          {data.data.dueDate && (
            <div className="p-4 bg-blue-50 rounded-xl mb-6">
              <span className="text-xs text-blue-600 uppercase flex items-center gap-1">
                <Calendar size={14} /> Due Date
              </span>
              <p className="font-medium mt-1">{new Date(data.data.dueDate).toLocaleDateString()}</p>
            </div>
          )}

          <div className="flex items-center gap-3 mb-6">
            <div className="w-10 h-10 bg-purple-100 rounded-full flex items-center justify-center">
              <User size={20} className="text-purple-600" />
            </div>
            <div>
              <p className="text-xs text-gray-500">Assigned to</p>
              <p className="font-medium">{data.data.assignee?.username || 'Unassigned'}</p>
            </div>
          </div>

          <div className="border-t pt-4">
            <h3 className="font-bold flex items-center gap-2 mb-3">
              <MessageSquare size={18} />
              Comments ({data.data.commentCount || 0})
            </h3>
            <p className="text-gray-400 text-sm">Comments feature coming soon...</p>
          </div>
        </div>
      </div>
    </div>
  );
}