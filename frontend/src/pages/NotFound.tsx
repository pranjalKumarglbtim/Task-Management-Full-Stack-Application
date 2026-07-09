import React from 'react';
import { Link } from 'react-router-dom';

export default function NotFound() {
  return (
    <div className="min-h-screen flex items-center justify-center">
      <div className="text-center">
        <h1 className="text-6xl font-bold text-gray-400">404</h1>
        <p className="text-xl mt-4">Page not found</p>
        <Link to="/dashboard" className="btn btn-primary mt-6">Go Home</Link>
      </div>
    </div>
  );
}