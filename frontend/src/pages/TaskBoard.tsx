import React, { useState, useEffect } from 'react';
import { DndContext, closestCenter, useSensor, useSensors, PointerSensor } from '@dnd-kit/core';
import { SortableContext, verticalListSortingStrategy } from '@dnd-kit/sortable';
import { tasksApi } from '../api/tasks';
import { GripVertical } from 'lucide-react';

const columns = [
  { id: 'TODO', title: 'To Do' },
  { id: 'IN_PROGRESS', title: 'In Progress' },
  { id: 'DONE', title: 'Done' },
];

export default function TaskBoard() {
  const [tasks, setTasks] = useState<any[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    tasksApi.getTasks().then(res => {
      setTasks(res.data.tasks);
      setLoading(false);
    }).catch(() => setLoading(false));
  }, []);

  const sensors = useSensors(useSensor(PointerSensor));

  const tasksByStatus = (status: string) => tasks.filter(t => t.status === status);

  const handleDragEnd = async (event: any) => {
    const { active, over } = event;
    if (over && active.id !== over.id) {
      try {
        await tasksApi.updateStatus(Number(active.id), over.id);
        setTasks(prev => prev.map(t => 
          t.id === Number(active.id) ? { ...t, status: over.id } : t
        ));
      } catch (error) {}
    }
  };

  if (loading) return (
    <div className="p-6">
      <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
        {[1, 2, 3].map(i => (
          <div key={i} className="h-96 bg-gray-100 rounded-xl animate-pulse"></div>
        ))}
      </div>
    </div>
  );

  return (
    <div className="p-6 bg-gray-50 min-h-screen">
      <h1 className="text-2xl font-bold mb-6">Task Board</h1>
      
      <DndContext sensors={sensors} collisionDetection={closestCenter} onDragEnd={handleDragEnd}>
        <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
          {columns.map((col) => (
            <div key={col.id} className="bg-white rounded-xl p-4 shadow border">
              <h2 className="font-bold mb-3">{col.title} ({tasksByStatus(col.id).length})</h2>
              <SortableContext items={tasksByStatus(col.id).map(t => t.id)} strategy={verticalListSortingStrategy}>
                <div className="space-y-2 min-h-96">
                  {tasksByStatus(col.id).map((task) => (
                    <div key={task.id} className="bg-gray-50 border rounded-lg p-3 cursor-move">
                      <div className="flex items-start gap-2">
                        <GripVertical size={14} className="text-gray-400 mt-1" />
                        <div>
                          <h3 className="font-medium text-sm">{task.title}</h3>
                          <span className="text-xs bg-gray-200 px-2 py-0.5 rounded-full mt-1 inline-block">
                            {task.priority}
                          </span>
                        </div>
                      </div>
                    </div>
                  ))}
                </div>
              </SortableContext>
            </div>
          ))}
        </div>
      </DndContext>
    </div>
  );
}