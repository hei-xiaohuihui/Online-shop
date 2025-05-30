'use client';

import Image from 'next/image';
import Link from 'next/link';
import { FiShoppingCart } from 'react-icons/fi';
import { cartAPI } from '@/services/api';
import { useState } from 'react';

interface ProductCardProps {
  id: string;
  name: string;
  price: number;
  imageUrl: string;
  description: string;
  stock: number;
}

export default function ProductCard({
  id,
  name,
  price,
  imageUrl,
  description,
  stock,
}: ProductCardProps) {
  const [adding, setAdding] = useState(false);

  const handleAddToCart = async (e: React.MouseEvent) => {
    e.preventDefault(); // 阻止链接跳转
    if (stock === 0 || adding) return;

    try {
      setAdding(true);
      const response = await cartAPI.addToCart({
        productId: Number(id),
        count: 1,
      });

      if (response.code === 200) {
        alert('成功添加到购物车！');
      } else {
        alert(response.msg || '添加到购物车失败');
      }
    } catch (error: any) {
      alert(error.message || '添加到购物车失败');
    } finally {
      setAdding(false);
    }
  };

  return (
    <Link href={`/product/${id}`} className="group">
      <div className="bg-white rounded-lg shadow-md overflow-hidden hover:shadow-lg transition-shadow">
        <div className="relative h-48">
          <Image
            src={imageUrl}
            alt={name}
            fill
            className="object-cover"
            sizes="(max-width: 768px) 100vw, (max-width: 1200px) 50vw, 33vw"
          />
        </div>
        <div className="p-4">
          <h3 className="text-lg font-medium text-gray-900 truncate group-hover:text-blue-600">
            {name}
          </h3>
          <p className="mt-1 text-gray-500 text-sm line-clamp-2">{description}</p>
          <div className="mt-2 flex items-center justify-between">
            <span className="text-red-600 text-xl font-bold">¥{price.toFixed(2)}</span>
            <button
              onClick={handleAddToCart}
              disabled={stock === 0 || adding}
              className={`flex items-center px-3 py-1 rounded ${
                stock === 0
                  ? 'bg-gray-200 text-gray-500 cursor-not-allowed'
                  : adding
                  ? 'bg-gray-200 text-gray-500'
                  : 'bg-blue-600 text-white hover:bg-blue-700'
              }`}
            >
              <FiShoppingCart className="mr-1" />
              {stock === 0 ? '已售罄' : adding ? '添加中...' : '加入购物车'}
            </button>
          </div>
          {stock > 0 && stock < 10 && (
            <p className="mt-1 text-sm text-orange-500">仅剩 {stock} 件</p>
          )}
        </div>
      </div>
    </Link>
  );
} 