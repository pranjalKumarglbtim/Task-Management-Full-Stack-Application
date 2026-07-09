import React from 'react';
import { Routes, Route, Navigate } from 'react-router-dom';
import { useAuthStore } from './store/authStore';
import Login from './pages/Login';
import Register from './pages/Register';
import Dashboard from './pages/Dashboard';
import Tasks from './pages/Tasks';
import TaskBoard from './pages/TaskBoard';
import TaskDetail from './pages/TaskDetail';
import Profile from './pages/Profile';
import NotFound from './pages/NotFound';
import { Sidebar } from './components/Sidebar';
import { ThemeToggle } from './components/ThemeToggle';

function ProtectedRoute({ children }: { children: React.ReactNode }) {
  const { tokens } = useAuthStore();
  if (!tokens?.accessToken) {
    return <Navigate to="/login" replace />;
  }
  return (
    <div className="flex min-h-screen">
      <Sidebar />
      <main className="flex-1 ml-64 bg-gray-50 dark:bg-gray-900">
        <ThemeToggle />
        {children}
      </main>
    </div>
  );
}

export default function App() {
  const { tokens } = useAuthStore();

  return (
    <Routes>
      <Route path="/login" element={!tokens?.accessToken ? <Login /> : <Navigate to="/dashboard" replace />} />
      <Route path="/register" element={!tokens?.accessToken ? <Register /> : <Navigate to="/dashboard" replace />} />
      <Route path="/dashboard" element={
        <ProtectedRoute>
          <Dashboard />
        </ProtectedRoute>
      } />
      <Route path="/tasks" element={
        <ProtectedRoute>
          <Tasks />
        </ProtectedRoute>
      } />
      <Route path="/tasks/:id" element={
        <ProtectedRoute>
          <TaskDetail />
        </ProtectedRoute>
      } />
      <Route path="/board" element={
        <ProtectedRoute>
          <TaskBoard />
        </ProtectedRoute>
      } />
      <Route path="/profile" element={
        <ProtectedRoute>
          <Profile />
        </ProtectedRoute>
      } />
      <Route path="/" element={<Navigate to="/dashboard" replace />} />
      <Route path="*" element={<NotFound />} />
    </Routes>
  );
}