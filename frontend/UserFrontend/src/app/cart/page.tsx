'use client';

import { useState, useEffect } from 'react';
import { cartAPI, type CartItem } from '@/services/api';
import Image from 'next/image';
import { FiMinus, FiPlus, FiTrash2 } from 'react-icons/fi';
import Link from 'next/link';
import { useRouter } from 'next/navigation';

export default function CartPage() {
  const [cartItems, setCartItems] = useState<CartItem[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [selectAll, setSelectAll] = useState(false);
  const router = useRouter();

  useEffect(() => {
    fetchCartItems();
  }, []);

  useEffect(() => {
    // 更新全选状态
    if (cartItems.length > 0) {
      setSelectAll(cartItems.every(item => item.selected === 1));
    }
  }, [cartItems]);

  const fetchCartItems = async () => {
    try {
      setLoading(true);
      const response = await cartAPI.getCartList();
      if (response.code === 200) {
        setCartItems(response.data || []);
        setError(null);
      } else {
        setError(response.msg || '获取购物车失败');
      }
    } catch (err: any) {
      setError(err.message || '获取购物车失败');
    } finally {
      setLoading(false);
    }
  };

  const handleQuantityChange = async (productId: number, quantity: number, stock: number) => {
    if (quantity < 1 || quantity > stock) return;
    
    try {
      const response = await cartAPI.updateCart({
        productId,
        quantity,
        selected: cartItems.find(item => item.productId === productId)?.selected || 0
      });
      
      if (response.code === 200) {
        await fetchCartItems();
      } else {
        setError(response.msg || '更新数量失败');
      }
    } catch (err: any) {
      setError(err.message || '更新数量失败');
    }
  };

  const handleRemove = async (productId: number) => {
    if (!window.confirm('确定要删除这个商品吗？')) return;
    
    try {
      const response = await cartAPI.removeFromCart(productId);
      if (response.code === 200) {
        await fetchCartItems();
      } else {
        setError(response.msg || '删除商品失败');
      }
    } catch (err: any) {
      setError(err.message || '删除商品失败');
    }
  };

  const handleSelect = async (productId: number, selected: number) => {
    try {
      const response = await cartAPI.updateCart({
        productId,
        quantity: cartItems.find(item => item.productId === productId)?.quantity || 1,
        selected: selected === 1 ? 0 : 1
      });
      
      if (response.code === 200) {
        await fetchCartItems();
      } else {
        setError(response.msg || '更新选中状态失败');
      }
    } catch (err: any) {
      setError(err.message || '更新选中状态失败');
    }
  };

  const handleSelectAll = async () => {
    try {
      const response = await cartAPI.selectAll(selectAll ? 0 : 1);
      if (response.code === 200) {
        await fetchCartItems();
      } else {
        setError(response.msg || '全选操作失败');
      }
    } catch (err: any) {
      setError(err.message || '全选操作失败');
    }
  };

  const calculateTotal = () => {
    return cartItems
      .filter(item => item.selected === 1)
      .reduce((total, item) => total + item.price * item.quantity, 0);
  };

  const handleCheckout = () => {
    router.push('/checkout');
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

  if (cartItems.length === 0) {
    return (
      <div className="min-h-screen bg-gray-50 py-8">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="text-center">
            <p className="text-gray-500 mb-4">购物车是空的</p>
            <Link
              href="/"
              className="inline-block bg-blue-600 text-white px-6 py-2 rounded-md hover:bg-blue-700"
            >
              去购物
            </Link>
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gray-50 py-8">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="bg-white rounded-lg shadow">
          <div className="p-6">
            <div className="flex items-center mb-4">
              <label className="flex items-center">
                <input
                  type="checkbox"
                  checked={selectAll}
                  onChange={handleSelectAll}
                  className="h-4 w-4 text-blue-600 rounded border-gray-300"
                />
                <span className="ml-2">全选</span>
              </label>
            </div>

            <div className="space-y-4">
              {cartItems.map((item) => (
                <div
                  key={item.id}
                  className="flex items-center border-b pb-4"
                >
                  <input
                    type="checkbox"
                    checked={item.selected === 1}
                    onChange={() => handleSelect(item.productId, item.selected)}
                    className="h-4 w-4 text-blue-600 rounded border-gray-300"
                  />
                  <div className="relative h-20 w-20 ml-4">
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
                    <p className="text-red-600">¥{item.price.toFixed(2)}</p>
                  </div>
                  <div className="flex items-center space-x-2">
                    <button
                      onClick={() => handleQuantityChange(item.productId, item.quantity - 1, item.stock)}
                      disabled={item.quantity <= 1}
                      className="p-1 rounded-md hover:bg-gray-100 disabled:opacity-50"
                    >
                      <FiMinus />
                    </button>
                    <span className="w-12 text-center">{item.quantity}</span>
                    <button
                      onClick={() => handleQuantityChange(item.productId, item.quantity + 1, item.stock)}
                      disabled={item.quantity >= item.stock}
                      className="p-1 rounded-md hover:bg-gray-100 disabled:opacity-50"
                    >
                      <FiPlus />
                    </button>
                  </div>
                  <button
                    onClick={() => handleRemove(item.productId)}
                    className="ml-4 p-2 text-gray-400 hover:text-red-600"
                  >
                    <FiTrash2 />
                  </button>
                </div>
              ))}
            </div>

            <div className="mt-6 flex justify-between items-center">
              <div>
                <span className="text-gray-600">
                  已选 {cartItems.filter(item => item.selected === 1).length} 件商品
                </span>
                <span className="ml-4">
                  合计：
                  <span className="text-red-600 text-xl font-bold">
                    ¥{calculateTotal().toFixed(2)}
                  </span>
                </span>
              </div>
              <button
                disabled={!cartItems.some(item => item.selected === 1)}
                onClick={handleCheckout}
                className={`px-8 py-2 rounded-md text-white ${
                  cartItems.some(item => item.selected === 1)
                    ? 'bg-red-600 hover:bg-red-700'
                    : 'bg-gray-400 cursor-not-allowed'
                }`}
              >
                结算
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
} 