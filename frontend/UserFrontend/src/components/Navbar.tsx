'use client';

import { useState, useEffect } from 'react';
import Link from 'next/link';
import { useRouter } from 'next/navigation';
import { FiShoppingCart, FiUser, FiLogOut, FiList } from 'react-icons/fi';
import { useAuth } from '@/contexts/AuthContext';
import SearchBar from './SearchBar';

const Navbar = () => {
  const { user, logout, isAuthenticated } = useAuth();
  const [mounted, setMounted] = useState(false);

  // 使用 useEffect 来处理客户端渲染
  useEffect(() => {
    setMounted(true);
  }, []);

  // 在客户端渲染之前不显示用户相关的按钮
  if (!mounted) {
    return (
      <nav className="bg-white shadow-md">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex items-center justify-between h-16">
            <div className="flex-shrink-0">
              <Link href="/" className="text-2xl font-bold text-gray-800">
                OnlineShop
              </Link>
            </div>
            <div className="flex-1 max-w-lg mx-8">
              <SearchBar />
            </div>
            <div className="flex items-center space-x-4">
              <div className="h-8 w-8 bg-gray-200 rounded-full animate-pulse"></div>
            </div>
          </div>
        </div>
      </nav>
    );
  }

  return (
    <nav className="bg-white shadow-md">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex items-center justify-between h-16">
          <div className="flex-shrink-0">
            <Link href="/" className="text-2xl font-bold text-gray-800">
              OnlineShop
            </Link>
          </div>
          
          <div className="flex-1 max-w-lg mx-8">
            <SearchBar />
          </div>

          <div className="flex items-center space-x-4">
            {isAuthenticated ? (
              <>
                <Link href="/cart" className="p-2 text-gray-400 hover:text-gray-500 relative">
                  <span className="sr-only">购物车</span>
                  <FiShoppingCart className="h-6 w-6" />
                </Link>
                <Link href="/orders" className="p-2 text-gray-400 hover:text-gray-500">
                  <span className="sr-only">我的订单</span>
                  <FiList className="h-6 w-6" />
                </Link>
                <Link href="/profile" className="p-2 text-gray-400 hover:text-gray-500">
                  <span className="sr-only">个人中心</span>
                  <FiUser className="h-6 w-6" />
                </Link>
                <button
                  onClick={logout}
                  className="p-2 text-gray-400 hover:text-gray-500"
                >
                  <span className="sr-only">退出登录</span>
                  <FiLogOut className="h-6 w-6" />
                </button>
              </>
            ) : (
              <div className="flex items-center space-x-4">
                <Link
                  href="/login"
                  className="text-gray-800 hover:text-gray-900"
                >
                  登录
                </Link>
                <Link
                  href="/register"
                  className="bg-blue-600 text-white px-4 py-2 rounded-md hover:bg-blue-700"
                >
                  注册
                </Link>
              </div>
            )}
          </div>
        </div>
      </div>
    </nav>
  );
};

export default Navbar; 