import React, { useState, useEffect } from 'react';
import { User, Mail, Calendar, Edit2, Save, X, Shield, Bell } from 'lucide-react';

export default function Profile() {
  const [isEditing, setIsEditing] = useState(false);
  const [formData, setFormData] = useState({ username: 'John Doe', email: 'john@example.com' });

  return (
    <div className="p-6 bg-gray-50 min-h-screen">
      <div className="max-w-3xl mx-auto">
        <h1 className="text-2xl font-bold mb-6">Profile</h1>

        <div className="bg-white rounded-xl p-6 shadow">
          <div className="flex items-center gap-4 mb-6">
            <div className="w-20 h-20 bg-purple-600 rounded-full flex items-center justify-center text-white text-2xl font-bold">
              {formData.username[0]}
            </div>
            <div>
              <h2 className="text-xl font-bold">{formData.username}</h2>
              <p className="text-gray-500">{formData.email}</p>
            </div>
          </div>

          <div className="space-y-4">
            <div>
              <label className="block text-sm font-medium mb-1">Username</label>
              <div className="relative">
                <input
                  type="text"
                  value={formData.username}
                  onChange={(e) => setFormData({...formData, username: e.target.value})}
                  disabled={!isEditing}
                  className="w-full px-3 py-2 border rounded-lg"
                />
                {!isEditing && (
                  <button 
                    onClick={() => setIsEditing(true)}
                    className="absolute right-2 top-1/2 -translate-y-1/2 px-3 py-1 text-sm bg-purple-100 rounded"
                  >
                    Edit
                  </button>
                )}
              </div>
            </div>

            <div>
              <label className="block text-sm font-medium mb-1">Email</label>
              <input
                type="email"
                value={formData.email}
                onChange={(e) => setFormData({...formData, email: e.target.value})}
                disabled={!isEditing}
                className="w-full px-3 py-2 border rounded-lg"
              />
            </div>
          </div>

          {isEditing && (
            <div className="flex gap-2 mt-4">
              <button onClick={() => setIsEditing(false)} className="px-4 py-2 bg-gray-200 rounded-lg">Cancel</button>
              <button onClick={() => setIsEditing(false)} className="px-4 py-2 bg-purple-600 text-white rounded-lg">Save</button>
            </div>
          )}
        </div>
      </div>
    </div>
  );
}