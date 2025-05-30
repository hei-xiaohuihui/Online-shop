'use client';

import { useState, useEffect } from 'react';
import { addressAPI } from '@/services/api';
import { FiEdit2, FiTrash2, FiPlus } from 'react-icons/fi';

interface Address {
  id: number;
  consignee: string;
  sex: number;
  phone: string;
  address: string;
  label: string;
  defaulted: number;
}

interface AddressFormData {
  consignee: string;
  sex: number;
  phone: string;
  address: string;
  label: string;
}

const initialFormData: AddressFormData = {
  consignee: '',
  sex: 1,
  phone: '',
  address: '',
  label: '',
};

export default function AddressManagement() {
  const [addresses, setAddresses] = useState<Address[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [showForm, setShowForm] = useState(false);
  const [formData, setFormData] = useState<AddressFormData>(initialFormData);
  const [editingId, setEditingId] = useState<number | null>(null);

  useEffect(() => {
    fetchAddresses();
  }, []);

  const fetchAddresses = async () => {
    try {
      setLoading(true);
      const response = await addressAPI.getAddressList();
      if (response.code === 200) {
        setAddresses(response.data || []);
        setError(null);
      } else {
        setError(response.msg || '获取地址列表失败');
      }
    } catch (err: any) {
      setError(err.message || '获取地址列表失败');
    } finally {
      setLoading(false);
    }
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      if (editingId) {
        const response = await addressAPI.updateAddress({
          ...formData,
          id: editingId,
          defaulted: 0,
        });
        if (response.code === 200) {
          await fetchAddresses();
          setShowForm(false);
          setEditingId(null);
          setFormData(initialFormData);
        } else {
          setError(response.msg || '更新地址失败');
        }
      } else {
        const response = await addressAPI.addAddress(formData);
        if (response.code === 200) {
          await fetchAddresses();
          setShowForm(false);
          setFormData(initialFormData);
        } else {
          setError(response.msg || '添加地址失败');
        }
      }
    } catch (err: any) {
      setError(err.message || '操作失败');
    }
  };

  const handleEdit = (address: Address) => {
    setFormData({
      consignee: address.consignee,
      sex: address.sex,
      phone: address.phone,
      address: address.address,
      label: address.label,
    });
    setEditingId(address.id);
    setShowForm(true);
  };

  const handleDelete = async (id: number) => {
    if (!window.confirm('确定要删除这个地址吗？')) return;
    try {
      const response = await addressAPI.deleteAddress(id);
      if (response.code === 200) {
        await fetchAddresses();
      } else {
        setError(response.msg || '删除地址失败');
      }
    } catch (err: any) {
      setError(err.message || '删除地址失败');
    }
  };

  if (loading) {
    return <div className="text-center">加载中...</div>;
  }

  return (
    <div>
      {error && (
        <div className="mb-4 text-red-600 text-center">{error}</div>
      )}

      <div className="mb-4 flex justify-between items-center">
        <h2 className="text-lg font-medium">我的收货地址</h2>
        <button
          onClick={() => {
            setShowForm(true);
            setEditingId(null);
            setFormData(initialFormData);
          }}
          className="flex items-center px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700"
        >
          <FiPlus className="mr-2" />
          添加新地址
        </button>
      </div>

      {showForm && (
        <form onSubmit={handleSubmit} className="mb-6 bg-gray-50 p-4 rounded-md">
          <div className="grid grid-cols-1 gap-4">
            <div>
              <label className="block text-sm font-medium text-gray-700">收货人</label>
              <input
                type="text"
                value={formData.consignee}
                onChange={(e) => setFormData({ ...formData, consignee: e.target.value })}
                className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500"
                required
              />
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700">性别</label>
              <select
                value={formData.sex}
                onChange={(e) => setFormData({ ...formData, sex: Number(e.target.value) })}
                className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500"
              >
                <option value={1}>先生</option>
                <option value={2}>女士</option>
              </select>
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700">手机号码</label>
              <input
                type="tel"
                value={formData.phone}
                onChange={(e) => setFormData({ ...formData, phone: e.target.value })}
                className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500"
                required
              />
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700">详细地址</label>
              <input
                type="text"
                value={formData.address}
                onChange={(e) => setFormData({ ...formData, address: e.target.value })}
                className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500"
                required
              />
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700">标签</label>
              <input
                type="text"
                value={formData.label}
                onChange={(e) => setFormData({ ...formData, label: e.target.value })}
                className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500"
                placeholder="如：家、公司"
              />
            </div>
          </div>
          <div className="mt-4 flex justify-end space-x-3">
            <button
              type="button"
              onClick={() => {
                setShowForm(false);
                setEditingId(null);
                setFormData(initialFormData);
              }}
              className="px-4 py-2 border border-gray-300 rounded-md text-gray-700 hover:bg-gray-50"
            >
              取消
            </button>
            <button
              type="submit"
              className="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700"
            >
              {editingId ? '更新' : '保存'}
            </button>
          </div>
        </form>
      )}

      <div className="space-y-4">
        {addresses.map((address) => (
          <div
            key={address.id}
            className="border rounded-lg p-4 flex justify-between items-start"
          >
            <div>
              <div className="flex items-center space-x-2">
                <span className="font-medium">{address.consignee}</span>
                <span className="text-gray-500">
                  {address.sex === 1 ? '先生' : '女士'}
                </span>
                {address.defaulted === 1 && (
                  <span className="px-2 py-1 bg-blue-100 text-blue-800 text-xs rounded">
                    默认
                  </span>
                )}
              </div>
              <div className="text-gray-500 mt-1">{address.phone}</div>
              <div className="text-gray-600 mt-1">{address.address}</div>
              {address.label && (
                <div className="mt-2">
                  <span className="px-2 py-1 bg-gray-100 text-gray-600 text-xs rounded">
                    {address.label}
                  </span>
                </div>
              )}
            </div>
            <div className="flex space-x-2">
              <button
                onClick={() => handleEdit(address)}
                className="p-2 text-gray-600 hover:text-blue-600"
              >
                <FiEdit2 />
              </button>
              <button
                onClick={() => handleDelete(address.id)}
                className="p-2 text-gray-600 hover:text-red-600"
              >
                <FiTrash2 />
              </button>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
} 