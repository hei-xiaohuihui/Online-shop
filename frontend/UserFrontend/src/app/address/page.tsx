'use client';

import { useState, useEffect } from 'react';
import { useRouter } from 'next/navigation';
import { FiPlus, FiEdit2, FiTrash2 } from 'react-icons/fi';
import Navbar from '@/components/Navbar';
import { addressAPI } from '@/services/api';
import AddressForm from '@/components/AddressForm';

interface Address {
  id: number;
  consignee: string;
  sex: number;
  phone: string;
  address: string;
  label: string;
  defaulted: number;
}

export default function AddressPage() {
  const router = useRouter();
  const [addresses, setAddresses] = useState<Address[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [showForm, setShowForm] = useState(false);
  const [editingAddress, setEditingAddress] = useState<Address | null>(null);

  const fetchAddresses = async () => {
    try {
      setLoading(true);
      const response = await addressAPI.getAddressList();
      setAddresses(response.data || []);
      setError(null);
    } catch (err) {
      setError('获取地址列表失败，请重试');
      console.error('Error fetching addresses:', err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchAddresses();
  }, []);

  const handleDelete = async (id: number) => {
    if (!window.confirm('确定要删除这个地址吗？')) {
      return;
    }

    try {
      await addressAPI.deleteAddress(id);
      await fetchAddresses();
    } catch (err) {
      console.error('Error deleting address:', err);
      alert('删除地址失败，请重试');
    }
  };

  const handleEdit = (address: Address) => {
    setEditingAddress(address);
    setShowForm(true);
  };

  const handleFormSubmit = async () => {
    await fetchAddresses();
    setShowForm(false);
    setEditingAddress(null);
  };

  if (loading) {
    return (
      <div>
        <Navbar />
        <div className="max-w-4xl mx-auto px-4 py-8">
          <div className="animate-pulse space-y-4">
            {[1, 2, 3].map((key) => (
              <div key={key} className="h-32 bg-gray-200 rounded-lg"></div>
            ))}
          </div>
        </div>
      </div>
    );
  }

  return (
    <div>
      <Navbar />
      <div className="max-w-4xl mx-auto px-4 py-8">
        <div className="flex justify-between items-center mb-6">
          <h1 className="text-2xl font-bold text-gray-900">收货地址</h1>
          <button
            onClick={() => setShowForm(true)}
            className="flex items-center px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700"
          >
            <FiPlus className="mr-2" />
            添加新地址
          </button>
        </div>

        {error && (
          <div className="bg-red-50 border border-red-200 text-red-600 px-4 py-3 rounded relative mb-6">
            {error}
          </div>
        )}

        {addresses.length === 0 ? (
          <div className="text-center py-12 bg-gray-50 rounded-lg">
            <p className="text-gray-500">暂无收货地址</p>
          </div>
        ) : (
          <div className="space-y-4">
            {addresses.map((address) => (
              <div
                key={address.id}
                className="bg-white p-6 rounded-lg shadow-sm border border-gray-200"
              >
                <div className="flex justify-between items-start">
                  <div>
                    <div className="flex items-center gap-4 mb-2">
                      <span className="font-medium">{address.consignee}</span>
                      <span className="text-gray-600">{address.phone}</span>
                      {address.defaulted === 1 && (
                        <span className="px-2 py-1 bg-blue-50 text-blue-600 text-sm rounded">
                          默认地址
                        </span>
                      )}
                    </div>
                    <p className="text-gray-600">{address.address}</p>
                    {address.label && (
                      <span className="inline-block mt-2 px-3 py-1 bg-gray-100 text-gray-600 text-sm rounded">
                        {address.label}
                      </span>
                    )}
                  </div>
                  <div className="flex gap-2">
                    <button
                      onClick={() => handleEdit(address)}
                      className="p-2 text-gray-400 hover:text-blue-600"
                    >
                      <FiEdit2 />
                    </button>
                    <button
                      onClick={() => handleDelete(address.id)}
                      className="p-2 text-gray-400 hover:text-red-600"
                    >
                      <FiTrash2 />
                    </button>
                  </div>
                </div>
              </div>
            ))}
          </div>
        )}

        {showForm && (
          <AddressForm
            address={editingAddress}
            onSubmit={handleFormSubmit}
            onCancel={() => {
              setShowForm(false);
              setEditingAddress(null);
            }}
          />
        )}
      </div>
    </div>
  );
} 