'use client';

import { useState, useEffect } from 'react';
import { orderAPI, type Order } from '@/services/api';
import Image from 'next/image';
import { useRouter } from 'next/navigation';
import { useAuth } from '@/contexts/AuthContext';

export default function OrdersPage() {
  const [orders, setOrders] = useState<Order[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [currentPage, setCurrentPage] = useState(1);
  const [totalPages, setTotalPages] = useState(1);
  const pageSize = 12;
  const router = useRouter();
  const { isAuthenticated } = useAuth();

  useEffect(() => {
    if (!isAuthenticated) {
      router.push('/login');
      return;
    }
    fetchOrders();
  }, [currentPage, isAuthenticated, router]);

  const fetchOrders = async () => {
    try {
      setLoading(true);
      const response = await orderAPI.getOrderList({
        pageNum: currentPage,
        pageSize
      });
      
      if (response.code === 200) {
        setOrders(response.data.list);
        setTotalPages(response.data.pages);
        setError(null);
      } else {
        setError(response.msg || '获取订单列表失败');
      }
    } catch (err: any) {
      setError(err.message || '获取订单列表失败');
    } finally {
      setLoading(false);
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
        <h1 className="text-2xl font-semibold mb-6">我的订单</h1>
        
        <div className="space-y-6">
          {orders.map((order) => (
            <div
              key={order.orderNum}
              className="bg-white rounded-lg shadow p-6 cursor-pointer hover:shadow-lg transition-shadow"
              onClick={() => router.push(`/orders/${order.orderNum}`)}
            >
              <div className="flex justify-between items-center mb-4">
                <div className="text-sm text-gray-500">
                  订单号：{order.orderNum}
                </div>
                <div className="text-blue-600">
                  {order.orderStatusStr}
                </div>
              </div>
              
              <div className="space-y-4">
                {order.orderDetailVoList.map((item) => (
                  <div key={`${order.orderNum}-${item.productId}`} className="flex items-center">
                    <div className="relative h-20 w-20">
                      <Image
                        src={item.productImage}
                        alt={item.productName}
                        fill
                        className="object-cover rounded"
                        sizes="80px"
                      />
                    </div>
                    <div className="ml-4 flex-1">
                      <h3 className="text-lg font-medium">{item.productName}</h3>
                      <div className="flex justify-between items-center mt-2">
                        <p className="text-red-600">¥{item.unitPrice.toFixed(2)}</p>
                        <p className="text-gray-500">x{item.quantity}</p>
                      </div>
                    </div>
                  </div>
                ))}
              </div>
              
              <div className="mt-4 flex justify-between items-center border-t pt-4">
                <div className="text-gray-500">
                  {order.createTime.substring(0, 19).replace('T', ' ')}
                </div>
                <div className="text-lg">
                  总计：<span className="text-red-600 font-bold">¥{order.totalPrice.toFixed(2)}</span>
                </div>
              </div>
            </div>
          ))}
        </div>

        {/* 分页 */}
        {totalPages > 1 && (
          <div className="flex justify-center mt-8 space-x-2">
            <button
              onClick={() => setCurrentPage(p => Math.max(1, p - 1))}
              disabled={currentPage === 1}
              className="px-4 py-2 border rounded-md disabled:opacity-50"
            >
              上一页
            </button>
            <span className="px-4 py-2">
              {currentPage} / {totalPages}
            </span>
            <button
              onClick={() => setCurrentPage(p => Math.min(totalPages, p + 1))}
              disabled={currentPage === totalPages}
              className="px-4 py-2 border rounded-md disabled:opacity-50"
            >
              下一页
            </button>
          </div>
        )}
      </div>
    </div>
  );
} 