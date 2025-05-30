'use client';

import { useEffect, useState } from 'react';
import { useSearchParams } from 'next/navigation';
import Navbar from '@/components/Navbar';
import ProductCard from '@/components/ProductCard';
import { productAPI } from '@/services/api';

interface Product {
  id: number;
  name: string;
  price: number;
  description: string;
  image: string;
  categoryId: number;
  status: number;
  stock: number;
}

export default function SearchPage() {
  const searchParams = useSearchParams();
  const query = searchParams.get('q') || '';
  const [products, setProducts] = useState<Product[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const fetchProducts = async () => {
      try {
        setLoading(true);
        const response = await productAPI.getProducts({
          pageNum: 1,
          pageSize: 10,
          name: query
        });
        setProducts(response.data.records || []);
        setError(null);
      } catch (err) {
        setError('搜索商品失败，请重试');
        console.error('Error searching products:', err);
      } finally {
        setLoading(false);
      }
    };

    if (query) {
      fetchProducts();
    }
  }, [query]);

  return (
    <main className="min-h-screen bg-gray-50">
      <Navbar />
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        <h1 className="text-2xl font-bold text-gray-900 mb-6">
          搜索结果: {query}
        </h1>

        {loading ? (
          <div className="animate-pulse">
            <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6">
              {[1, 2, 3, 4, 5, 6].map((key) => (
                <div key={key} className="bg-gray-200 h-80 rounded-lg"></div>
              ))}
            </div>
          </div>
        ) : error ? (
          <div className="text-center text-red-600">
            <p>{error}</p>
            <button
              onClick={() => window.location.reload()}
              className="mt-4 px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700"
            >
              重试
            </button>
          </div>
        ) : products.length === 0 ? (
          <div className="text-center text-gray-500">
            <p>没有找到相关商品</p>
          </div>
        ) : (
          <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6">
            {products.map((product) => (
              <ProductCard
                key={product.id}
                id={String(product.id)}
                name={product.name}
                price={product.price}
                imageUrl={product.image}
                description={product.description}
              />
            ))}
          </div>
        )}
      </div>
    </main>
  );
} 