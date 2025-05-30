'use client';

import { useState, useEffect } from 'react';
import { addressAPI, cartAPI, orderAPI, type Address, type CheckoutItem } from '@/services/api';
import Image from 'next/image';
import { useRouter } from 'next/navigation';

export default function CheckoutPage() {
  const [addresses, setAddresses] = useState<Address[]>([]);
  const [selectedAddressId, setSelectedAddressId] = useState<number | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [checkoutItems, setCheckoutItems] = useState<CheckoutItem[]>([]);
  const [submitting, setSubmitting] = useState(false);
  const router = useRouter();

  useEffect(() => {
    Promise.all([fetchAddresses(), fetchCheckoutItems()]);
  }, []);

  const fetchAddresses = async () => {
    try {
      setLoading(true);
      const response = await addressAPI.getAddressList();
      if (response.code === 200) {
        setAddresses(response.data || []);
        // 如果有默认地址，则选中默认地址
        const defaultAddress = response.data?.find(addr => addr.isDefault === 1);
        if (defaultAddress) {
          setSelectedAddressId(defaultAddress.id);
        }
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

  const fetchCheckoutItems = async () => {
    try {
      const response = await cartAPI.getCheckoutItems();
      if (response.code === 200) {
        setCheckoutItems(response.data || []);
      } else {
        setError(response.msg || '获取商品信息失败');
      }
    } catch (err: any) {
      setError(err.message || '获取商品信息失败');
    }
  };

  const calculateTotal = () => {
    return checkoutItems.reduce((total, item) => total + item.totalPrice, 0);
  };

  const handleSubmitOrder = async () => {
    if (!selectedAddressId || submitting) return;

    try {
      setSubmitting(true);
      setError(null);
      const response = await orderAPI.createOrder({
        addressBookId: selectedAddressId
      });
      
      if (response.code === 200) {
        alert('订单提交成功！');
        router.push('/orders'); // 跳转到订单列表页面
      } else {
        setError(response.msg || '提交订单失败');
      }
    } catch (err: any) {
      setError(err.message || '提交订单失败');
    } finally {
      setSubmitting(false);
    }
  };

  if (loading) {
    return (
      <div className="min-h-screen bg-gray-50 py-8">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="text-center">加载中...</div>
        </div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="min-h-screen bg-gray-50 py-8">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="text-center text-red-600">{error}</div>
        </div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gray-50 py-8">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="bg-white rounded-lg shadow">
          <div className="p-6">
            <h2 className="text-xl font-semibold mb-4">选择收货地址</h2>
            
            {addresses.length === 0 ? (
              <div className="text-center py-4">
                <p className="text-gray-500">暂无收货地址</p>
                <button className="mt-2 px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700">
                  添加新地址
                </button>
              </div>
            ) : (
              <div className="space-y-4">
                {addresses.map((address) => (
                  <div
                    key={address.id}
                    className={`border rounded-lg p-4 cursor-pointer ${
                      selectedAddressId === address.id
                        ? 'border-blue-600 bg-blue-50'
                        : 'border-gray-200 hover:border-blue-600'
                    }`}
                    onClick={() => setSelectedAddressId(address.id)}
                  >
                    <div className="flex justify-between items-start">
                      <div>
                        <p className="font-medium">
                          {address.name} {address.phone}
                        </p>
                        <p className="text-gray-600 mt-1">
                          {address.province} {address.city} {address.district} {address.address}
                        </p>
                      </div>
                      {address.isDefault === 1 && (
                        <span className="text-blue-600 text-sm">默认</span>
                      )}
                    </div>
                  </div>
                ))}
              </div>
            )}

            <div className="mt-8">
              <h2 className="text-xl font-semibold mb-4">确认订单信息</h2>
              <div className="space-y-4">
                {checkoutItems.map((item) => (
                  <div key={item.id} className="flex items-center border-b pb-4">
                    <div className="relative h-20 w-20">
                      <Image
                        src={item.productImage}
                        alt={item.name}
                        fill
                        className="object-cover rounded"
                        sizes="80px"
                      />
                    </div>
                    <div className="ml-4 flex-1">
                      <h3 className="text-lg font-medium">{item.name}</h3>
                      <div className="flex justify-between items-center mt-2">
                        <p className="text-red-600">¥{item.price.toFixed(2)}</p>
                        <p className="text-gray-500">x{item.quantity}</p>
                        <p className="text-red-600 font-medium">¥{item.totalPrice.toFixed(2)}</p>
                      </div>
                    </div>
                  </div>
                ))}
              </div>
              
              <div className="mt-6 text-right">
                <p className="text-lg">
                  共 {checkoutItems.reduce((sum, item) => sum + item.quantity, 0)} 件商品，
                  总计：<span className="text-red-600 text-xl font-bold">¥{calculateTotal().toFixed(2)}</span>
                </p>
              </div>
            </div>

            <div className="mt-6 flex justify-end">
              <button
                onClick={handleSubmitOrder}
                disabled={!selectedAddressId || submitting}
                className={`px-8 py-2 rounded-md text-white ${
                  selectedAddressId && !submitting
                    ? 'bg-red-600 hover:bg-red-700'
                    : 'bg-gray-400 cursor-not-allowed'
                }`}
              >
                {submitting ? '提交中...' : '提交订单'}
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
} 