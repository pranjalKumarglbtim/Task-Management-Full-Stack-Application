import React from 'react';

interface TaskCardProps {
  task: {
    id: number;
    title: string;
    description: string;
    priority: string;
    dueDate: string | null;
    status: string;
    assignee: { id: number; username: string } | null;
    creator: { id: number; username: string } | null;
    createdAt: string;
    commentCount?: number;
  };
}

export const TaskCard: React.FC<TaskCardProps> = ({ task }) => {
  const priorityColors: Record<string, string> = {
    LOW: 'bg-green-100 text-green-700 dark:bg-green-900/30 dark:text-green-400',
    MEDIUM: 'bg-yellow-100 text-yellow-700 dark:bg-yellow-900/30 dark:text-yellow-400',
    HIGH: 'bg-orange-100 text-orange-700 dark:bg-orange-900/30 dark:text-orange-400',
    CRITICAL: 'bg-red-100 text-red-700 dark:bg-red-900/30 dark:text-red-400',
  };

  return (
    <div className="bg-white dark:bg-gray-800 rounded-lg p-3 shadow-sm border border-gray-100 dark:border-gray-700 hover:shadow transition-shadow cursor-pointer">
      <h3 className="font-medium text-gray-800 dark:text-gray-200 text-sm">{task.title}</h3>
      {task.description && (
        <p className="text-xs text-gray-500 dark:text-gray-400 mt-1 line-clamp-1">
          {task.description}
        </p>
      )}
      <div className="flex items-center justify-between mt-2">
        <span className={`text-xs px-2 py-0.5 rounded ${priorityColors[task.priority] || priorityColors.MEDIUM}`}>
          {task.priority}
        </span>
        {task.dueDate && (
          <span className="text-xs text-gray-400">
            {new Date(task.dueDate).toLocaleDateString()}
          </span>
        )}
      </div>
    </div>
  );
};