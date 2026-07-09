export const formatDate = (date: string | null) => {
  if (!date) return 'N/A';
  return new Date(date).toLocaleDateString();
};

export const formatDateTime = (date: string | null) => {
  if (!date) return 'N/A';
  return new Date(date).toLocaleString();
};

export const getPriorityColor = (priority: string) => {
  const colors: Record<string, string> = {
    LOW: 'bg-green-100 text-green-800',
    MEDIUM: 'bg-yellow-100 text-yellow-800',
    HIGH: 'bg-orange-100 text-orange-800',
    CRITICAL: 'bg-red-100 text-red-800',
  };
  return colors[priority] || '';
};

export const getStatusColor = (status: string) => {
  const colors: Record<string, string> = {
    TODO: 'bg-gray-100 text-gray-800',
    IN_PROGRESS: 'bg-blue-100 text-blue-800',
    REVIEW: 'bg-purple-100 text-purple-800',
    DONE: 'bg-green-100 text-green-800',
  };
  return colors[status] || '';
};