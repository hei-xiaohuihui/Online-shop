'use client';

import { useState } from 'react';
import { authAPI } from '@/services/api';

export default function PasswordUpdate() {
  const [formData, setFormData] = useState({
    password: '',
    confirmPassword: '',
  });
  const [error, setError] = useState<string | null>(null);
  const [success, setSuccess] = useState(false);
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError(null);
    setSuccess(false);

    if (formData.password !== formData.confirmPassword) {
      setError('两次输入的密码不一致');
      return;
    }

    if (formData.password.length < 6) {
      setError('密码长度不能少于6位');
      return;
    }

    try {
      setLoading(true);
      const response = await authAPI.updateUser({
        username: '', // 这里不修改用户名，只修改密码
        password: formData.password,
      });

      if (response.code === 200) {
        setSuccess(true);
        setFormData({ password: '', confirmPassword: '' });
      } else {
        setError(response.msg || '修改密码失败');
      }
    } catch (err: any) {
      setError(err.message || '修改密码失败，请重试');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="max-w-md mx-auto">
      <form onSubmit={handleSubmit} className="space-y-6">
        {error && (
          <div className="text-red-600 text-center bg-red-50 p-3 rounded-md">
            {error}
          </div>
        )}
        {success && (
          <div className="text-green-600 text-center bg-green-50 p-3 rounded-md">
            密码修改成功！
          </div>
        )}

        <div>
          <label
            htmlFor="password"
            className="block text-sm font-medium text-gray-700"
          >
            新密码
          </label>
          <input
            type="password"
            id="password"
            value={formData.password}
            onChange={(e) =>
              setFormData({ ...formData, password: e.target.value })
            }
            className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500"
            required
            minLength={6}
          />
        </div>

        <div>
          <label
            htmlFor="confirmPassword"
            className="block text-sm font-medium text-gray-700"
          >
            确认新密码
          </label>
          <input
            type="password"
            id="confirmPassword"
            value={formData.confirmPassword}
            onChange={(e) =>
              setFormData({ ...formData, confirmPassword: e.target.value })
            }
            className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500"
            required
            minLength={6}
          />
        </div>

        <div>
          <button
            type="submit"
            disabled={loading}
            className={`w-full flex justify-center py-2 px-4 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-blue-600 hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500 ${
              loading ? 'opacity-50 cursor-not-allowed' : ''
            }`}
          >
            {loading ? '修改中...' : '修改密码'}
          </button>
        </div>
      </form>
    </div>
  );
} 