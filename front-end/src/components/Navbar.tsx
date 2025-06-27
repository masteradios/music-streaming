import React from 'react';
import { Link, useLocation } from 'react-router-dom';
import { Home, Search, LogOut, Music } from 'lucide-react';
import { useAuth } from '../context/AuthContext';

export const Navbar: React.FC = () => {
  const { user, logout } = useAuth();
  const location = useLocation();

  const isActive = (path: string) => location.pathname === path;

  return (
    <nav className="bg-gray-900/95 backdrop-blur-lg border-b border-gray-700/50 px-6 py-4">
      <div className="max-w-6xl mx-auto flex items-center justify-between">
        <Link to="/home" className="flex items-center space-x-2">
          <div className="w-8 h-8 bg-gradient-to-br from-purple-500 to-pink-500 rounded-lg flex items-center justify-center">
            <Music className="w-5 h-5 text-white" />
          </div>
          <span className="text-xl font-bold text-white">
            Melodies of Shreya: Handpicked Classics
          </span>
        </Link>

        <div className="flex items-center space-x-6">
          <Link
            to="/home"
            className={`flex items-center space-x-2 px-4 py-2 rounded-lg transition-colors ${
              isActive("/home")
                ? "bg-purple-600 text-white"
                : "text-gray-300 hover:text-white hover:bg-gray-700/50"
            }`}
          >
            <Home className="w-4 h-4" />
            <span>Home</span>
          </Link>

          <Link
            to="/search"
            className={`flex items-center space-x-2 px-4 py-2 rounded-lg transition-colors ${
              isActive("/search")
                ? "bg-purple-600 text-white"
                : "text-gray-300 hover:text-white hover:bg-gray-700/50"
            }`}
          >
            <Search className="w-4 h-4" />
            <span>Search</span>
          </Link>
        </div>

        <div className="flex items-center space-x-4">
          <span className="text-gray-300">Hi, {user?.userName}</span>
          <button
            onClick={logout}
            className="flex items-center space-x-2 px-4 py-2 text-gray-300 hover:text-white hover:bg-gray-700/50 rounded-lg transition-colors"
          >
            <LogOut className="w-4 h-4" />
            <span>Logout</span>
          </button>
        </div>
      </div>
    </nav>
  );
};