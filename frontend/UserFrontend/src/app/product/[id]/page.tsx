'use client';

import { useEffect, useState } from 'react';
import Image from 'next/image';
import { useParams } from 'next/navigation';
import { FiShoppingCart } from 'react-icons/fi';
import { productAPI, cartAPI } from '@/services/api';
import type { Product } from '@/services/api';

export default function ProductDetailPage() {
  const params = useParams();
  const productId = params.id as string;
  
  const [product, setProduct] = useState<Product | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [addingToCart, setAddingToCart] = useState(false);

  useEffect(() => {
    const fetchProduct = async () => {
      try {
        setLoading(true);
        const response = await productAPI.getProductDetail(productId);
        if (response.code === 200) {
          setProduct(response.data);
          setError(null);
        } else {
          setError(response.msg || '获取商品详情失败');
        }
      } catch (err) {
        setError('获取商品详情失败，请重试');
        console.error('Error fetching product:', err);
      } finally {
        setLoading(false);
      }
    };

    if (productId) {
      fetchProduct();
    }
  }, [productId]);

  const handleAddToCart = async () => {
    if (!product || product.stock === 0) return;
    
    try {
      setAddingToCart(true);
      await cartAPI.addToCart({
        productId: product.id,
        count: 1
      });
      alert('成功添加到购物车！');
    } catch (error) {
      console.error('Failed to add to cart:', error);
      alert('添加到购物车失败，请重试');
    } finally {
      setAddingToCart(false);
    }
  };

  if (loading) {
    return (
      <div className="min-h-screen bg-gray-50 py-8">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="animate-pulse">
            <div className="bg-gray-200 h-96 rounded-lg"></div>
          </div>
        </div>
      </div>
    );
  }

  if (error || !product) {
    return (
      <div className="min-h-screen bg-gray-50 py-8">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="text-center text-red-600">
            <p>{error || '商品不存在'}</p>
            <button
              onClick={() => window.location.reload()}
              className="mt-4 px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700"
            >
              重试
            </button>
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gray-50 py-8">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="bg-white rounded-lg shadow-lg overflow-hidden">
          <div className="md:flex">
            <div className="md:flex-shrink-0 md:w-1/2">
              <div className="relative h-96">
                <Image
                  src={product.image}
                  alt={product.name}
                  fill
                  className="object-cover"
                  sizes="(max-width: 768px) 100vw, 50vw"
                />
              </div>
            </div>
            <div className="p-8 md:w-1/2">
              <h1 className="text-2xl font-bold text-gray-900">{product.name}</h1>
              <p className="mt-4 text-gray-600">{product.description}</p>
              <div className="mt-6">
                <p className="text-3xl font-bold text-gray-900">
                  ¥{product.price.toFixed(2)}
                </p>
                <p className="mt-2 text-sm text-gray-500">
                  库存: {product.stock} 件
                </p>
              </div>
              <button
                onClick={handleAddToCart}
                disabled={addingToCart || product.stock === 0}
                className={`mt-8 w-full flex items-center justify-center px-6 py-3 border border-transparent rounded-md shadow-sm text-base font-medium text-white bg-blue-600 hover:bg-blue-700 ${
                  (addingToCart || product.stock === 0) ? 'opacity-50 cursor-not-allowed' : ''
                }`}
              >
                <FiShoppingCart className="h-5 w-5 mr-2" />
                {product.stock === 0 ? '暂时缺货' : addingToCart ? '添加中...' : '加入购物车'}
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
} 