'use client';

import { useState } from 'react';
import AddressManagement from '@/components/AddressManagement';
import PasswordUpdate from '@/components/PasswordUpdate';

export default function ProfilePage() {
  const [activeTab, setActiveTab] = useState<'address' | 'password'>('address');

  return (
    <div className="min-h-screen bg-gray-50 py-8">
      <div className="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8">
        <h1 className="text-2xl font-semibold text-gray-900 mb-6">个人中心</h1>
        
        <div className="bg-white rounded-lg shadow">
          <div className="border-b border-gray-200">
            <nav className="-mb-px flex">
              <button
                onClick={() => setActiveTab('address')}
                className={`py-4 px-6 text-center border-b-2 font-medium text-sm ${
                  activeTab === 'address'
                    ? 'border-blue-500 text-blue-600'
                    : 'border-transparent text-gray-500 hover:text-gray-700 hover:border-gray-300'
                }`}
              >
                收货地址管理
              </button>
              <button
                onClick={() => setActiveTab('password')}
                className={`py-4 px-6 text-center border-b-2 font-medium text-sm ${
                  activeTab === 'password'
                    ? 'border-blue-500 text-blue-600'
                    : 'border-transparent text-gray-500 hover:text-gray-700 hover:border-gray-300'
                }`}
              >
                修改密码
              </button>
            </nav>
          </div>
          
          <div className="p-6">
            {activeTab === 'address' ? (
              <AddressManagement />
            ) : (
              <PasswordUpdate />
            )}
          </div>
        </div>
      </div>
    </div>
  );
} 