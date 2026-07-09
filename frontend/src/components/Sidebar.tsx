import React from 'react';
import { Link, useLocation } from 'react-router-dom';
import { LayoutDashboard, ListTodo, Kanban, User } from 'lucide-react';

const ROUTES = {
  DASHBOARD: '/dashboard',
  TASKS: '/tasks',
  TASK_BOARD: '/tasks/board',
  PROFILE: '/profile',
};

export const Sidebar = () => {
  const location = useLocation();

  const menuItems = [
    { path: ROUTES.DASHBOARD, icon: LayoutDashboard, label: 'Dashboard' },
    { path: ROUTES.TASKS, icon: ListTodo, label: 'Tasks' },
    { path: ROUTES.TASK_BOARD, icon: Kanban, label: 'Board' },
    { path: ROUTES.PROFILE, icon: User, label: 'Profile' },
  ];

  return (
    <aside className="w-64 h-screen bg-white dark:bg-gray-800 shadow-lg p-4">
      <div className="flex items-center justify-between mb-8">
        <h1 className="text-xl font-bold">TaskFlow</h1>
      </div>
      <nav>
        {menuItems.map((item) => (
          <Link
            key={item.path}
            to={item.path}
            className={`flex items-center gap-3 p-3 rounded-lg mb-2 transition-colors ${
              location.pathname === item.path
                ? 'bg-primary-100 text-primary-700 dark:bg-primary-900 dark:text-primary-100'
                : 'hover:bg-gray-100 dark:hover:bg-gray-700'
            }`}
          >
            <item.icon size={20} />
            <span>{item.label}</span>
          </Link>
        ))}
      </nav>
    </aside>
  );
};