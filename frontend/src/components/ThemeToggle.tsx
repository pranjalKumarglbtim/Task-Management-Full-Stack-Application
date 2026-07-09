import React from 'react';
import { Moon, Sun } from 'lucide-react';
import { useUIStore } from '../store/uiStore';

export const ThemeToggle = () => {
  const { isDarkMode, toggleDarkMode } = useUIStore();

  React.useEffect(() => {
    document.documentElement.classList.toggle('dark', isDarkMode);
  }, [isDarkMode]);

  return (
    <button
      onClick={toggleDarkMode}
      className="p-2 rounded-lg hover:bg-gray-200 dark:hover:bg-gray-700"
    >
      {isDarkMode ? <Sun size={20} /> : <Moon size={20} />}
    </button>
  );
};