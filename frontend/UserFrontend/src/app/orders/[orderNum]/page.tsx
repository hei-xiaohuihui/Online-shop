'use client';

import { useState, useEffect } from 'react';
import { orderAPI, type Order } from '@/services/api';
import Image from 'next/image';
import { useRouter } from 'next/navigation';
import { useAuth } from '@/contexts/AuthContext';
import { FiMapPin, FiPhone, FiUser } from 'react-icons/fi';

// 订单状态枚举
const OrderStatus = {
  CANCELLED: 0,    // 已取消
  PENDING_PAY: 10, // 待付款
  PAID: 20,       // 已付款
  SHIPPED: 30,    // 已发货
  COMPLETED: 40   // 已完成
} as const;

// 状态文字映射
const OrderStatusText = {
  [OrderStatus.CANCELLED]: '已取消',
  [OrderStatus.PENDING_PAY]: '待付款',
  [OrderStatus.PAID]: '已付款',
  [OrderStatus.SHIPPED]: '已发货',
  [OrderStatus.COMPLETED]: '已完成'
} as const;

export default function OrderDetailPage({ params }: { params: { orderNum: string } }) {
  const [order, setOrder] = useState<Order | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [showCancelModal, setShowCancelModal] = useState(false);
  const [cancelReason, setCancelReason] = useState('');
  const router = useRouter();
  const { isAuthenticated } = useAuth();

  useEffect(() => {
    // 检查本地存储中的 token
    const token = localStorage.getItem('token');
    if (!token && !isAuthenticated) {
      router.push('/login');
      return;
    }
    fetchOrderDetail();
  }, [isAuthenticated, router, params.orderNum]);

  const fetchOrderDetail = async () => {
    try {
      setLoading(true);
      const response = await orderAPI.getOrderDetail(params.orderNum);
      if (response.code === 200) {
        console.log('订单状态:', response.data.status);
        console.log('订单数据:', response.data);
        setOrder(response.data);
        setError(null);
      } else if (response.code === 401) {
        // 如果是未授权错误，则跳转到登录页
        router.push('/login');
      } else {
        setError(response.msg || '获取订单详情失败');
      }
    } catch (err: any) {
      if (err.response?.status === 401) {
        router.push('/login');
      } else {
        setError(err.message || '获取订单详情失败');
      }
    } finally {
      setLoading(false);
    }
  };

  const handlePayOrder = async () => {
    try {
      const response = await orderAPI.payOrder(params.orderNum);
      if (response.code === 200) {
        alert('支付成功！');
        fetchOrderDetail(); // 刷新订单状态
      } else {
        alert(response.msg || '支付失败');
      }
    } catch (err: any) {
      alert(err.message || '支付失败');
    }
  };

  const handleCancelOrder = async () => {
    if (!cancelReason.trim()) {
      alert('请输入取消原因');
      return;
    }

    if (!order) {
      alert('订单信息不存在');
      return;
    }

    try {
      const response = await orderAPI.cancelOrder({
        orderNum: order.orderNum,
        cancelReason: cancelReason.trim()
      });
      
      if (response.code === 200) {
        alert('订单已取消');
        setShowCancelModal(false);
        setCancelReason('');
        // 重新获取订单数据
        fetchOrderDetail();
      } else {
        alert(response.msg || '取消订单失败');
      }
    } catch (err: any) {
      alert(err.message || '取消订单失败');
    }
  };

  const handleConfirmReceipt = async () => {
    if (!window.confirm('确认已收到商品吗？')) {
      return;
    }

    try {
      const response = await orderAPI.completeOrder(params.orderNum);
      if (response.code === 200) {
        alert('确认收货成功！');
        fetchOrderDetail(); // 刷新订单状态
      } else {
        alert(response.msg || '确认收货失败');
      }
    } catch (err: any) {
      alert(err.message || '确认收货失败');
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

  if (error || !order) {
    return (
      <div className="min-h-screen bg-gray-50 py-8">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="text-center text-red-600">{error || '订单不存在'}</div>
        </div>
      </div>
    );
  }

  // 判断是否可以取消订单 - 待付款或已付款但未发货的订单可以取消
  const canCancel = order.status === OrderStatus.PENDING_PAY || order.status === OrderStatus.PAID;
  // 判断是否可以支付
  const canPay = order.status === OrderStatus.PENDING_PAY;
  // 判断是否可以确认收货
  const canConfirm = order.status === OrderStatus.SHIPPED;

  return (
    <div className="min-h-screen bg-gray-50 py-8">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="bg-white rounded-lg shadow overflow-hidden">
          {/* 订单状态 */}
          <div className={`p-6 ${
            order.status === OrderStatus.CANCELLED ? 'bg-gray-600' :
            order.status === OrderStatus.PENDING_PAY ? 'bg-orange-600' :
            order.status === OrderStatus.PAID ? 'bg-blue-600' :
            order.status === OrderStatus.SHIPPED ? 'bg-purple-600' :
            'bg-green-600'
          } text-white`}>
            <h1 className="text-2xl font-semibold">订单状态：{order.orderStatusStr}</h1>
            <p className="mt-2 text-white opacity-80">订单号：{order.orderNum}</p>
          </div>

          {/* 收货信息 */}
          <div className="p-6 border-b">
            <h2 className="text-lg font-semibold mb-4">收货信息</h2>
            <div className="space-y-2">
              <div className="flex items-center text-gray-600">
                <FiUser className="mr-2" />
                <span>收货人：{order.consignee}</span>
              </div>
              <div className="flex items-center text-gray-600">
                <FiPhone className="mr-2" />
                <span>联系电话：{order.phone}</span>
              </div>
              <div className="flex items-center text-gray-600">
                <FiMapPin className="mr-2" />
                <span>收货地址：{order.address}</span>
              </div>
            </div>
          </div>

          {/* 商品信息 */}
          <div className="p-6 border-b">
            <h2 className="text-lg font-semibold mb-4">商品信息</h2>
            <div className="space-y-4">
              {order.orderDetailVoList.map((item) => (
                <div key={item.productId} className="flex items-center">
                  <div className="relative h-24 w-24">
                    <Image
                      src={item.productImage}
                      alt={item.productName}
                      fill
                      className="object-cover rounded"
                      sizes="96px"
                    />
                  </div>
                  <div className="ml-4 flex-1">
                    <h3 className="text-lg font-medium">{item.productName}</h3>
                    <div className="flex justify-between items-center mt-2">
                      <p className="text-red-600">¥{item.unitPrice.toFixed(2)}</p>
                      <p className="text-gray-500">x{item.quantity}</p>
                      <p className="text-red-600 font-medium">¥{item.totalPrice.toFixed(2)}</p>
                    </div>
                  </div>
                </div>
              ))}
            </div>
          </div>

          {/* 订单信息 */}
          <div className="p-6 border-b">
            <h2 className="text-lg font-semibold mb-4">订单信息</h2>
            <div className="space-y-2 text-gray-600">
              <p>创建时间：{order.createTime.substring(0, 19).replace('T', ' ')}</p>
              {order.payTime && <p>支付时间：{order.payTime.substring(0, 19).replace('T', ' ')}</p>}
              {order.deliveryTime && <p>发货时间：{order.deliveryTime.substring(0, 19).replace('T', ' ')}</p>}
              {order.completeTime && <p>完成时间：{order.completeTime.substring(0, 19).replace('T', ' ')}</p>}
              {order.cancelTime && (
                <>
                  <p>取消时间：{order.cancelTime.substring(0, 19).replace('T', ' ')}</p>
                  <p>取消原因：{order.cancelReason}</p>
                </>
              )}
            </div>
          </div>

          {/* 支付信息 */}
          <div className="p-6">
            <div className="flex justify-between items-center">
              <div>
                <span className="text-gray-600">运费：</span>
                <span className="text-red-600">¥{order.postage.toFixed(2)}</span>
              </div>
              <div className="text-xl">
                <span className="text-gray-600">实付金额：</span>
                <span className="text-red-600 font-bold">¥{order.totalPrice.toFixed(2)}</span>
              </div>
            </div>

            <div className="mt-6 flex justify-end space-x-4">
              <button
                onClick={() => setShowCancelModal(true)}
                disabled={!(order.status === 10 || order.status === 20)}
                className={`px-6 py-2 border rounded-md ${
                  order.status === 10 || order.status === 20
                    ? 'border-gray-300 text-gray-700 hover:bg-gray-50'
                    : 'border-gray-200 text-gray-400 cursor-not-allowed'
                }`}
              >
                取消订单
              </button>
              {canPay && (
                <button
                  onClick={handlePayOrder}
                  className="px-8 py-2 bg-red-600 text-white rounded-md hover:bg-red-700"
                >
                  立即支付
                </button>
              )}
              <button
                onClick={handleConfirmReceipt}
                disabled={order.status !== 30}
                className={`px-8 py-2 rounded-md ${
                  order.status === 30
                    ? 'bg-green-600 text-white hover:bg-green-700'
                    : 'bg-gray-300 text-gray-500 cursor-not-allowed'
                }`}
              >
                确认收货
              </button>
            </div>
          </div>
        </div>

        {/* 取消订单模态框 */}
        {showCancelModal && (
          <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center p-4 z-50">
            <div className="bg-white rounded-lg p-6 max-w-md w-full">
              <h3 className="text-lg font-semibold mb-4">取消订单</h3>
              <textarea
                value={cancelReason}
                onChange={(e) => setCancelReason(e.target.value)}
                placeholder="请输入取消原因..."
                className="w-full border rounded-md p-2 h-32 resize-none"
              />
              <div className="mt-4 flex justify-end space-x-2">
                <button
                  onClick={() => {
                    setShowCancelModal(false);
                    setCancelReason('');
                  }}
                  className="px-4 py-2 border border-gray-300 text-gray-700 rounded-md hover:bg-gray-50"
                >
                  取消
                </button>
                <button
                  onClick={handleCancelOrder}
                  className="px-4 py-2 bg-red-600 text-white rounded-md hover:bg-red-700"
                >
                  确认取消
                </button>
              </div>
            </div>
          </div>
        )}
      </div>
    </div>
  );
} 