'use client';

import { useEffect, useState } from 'react';
import { useSearchParams } from 'next/navigation';
import ProductCard from './ProductCard';
import { productAPI } from '@/services/api';
import type { Product, ProductListResponse, ApiResponse } from '@/services/api';
import { FiSearch } from 'react-icons/fi';

const ProductList = () => {
  const searchParams = useSearchParams();
  const [products, setProducts] = useState<Product[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [currentPage, setCurrentPage] = useState(1);
  const [totalPages, setTotalPages] = useState(1);
  const [total, setTotal] = useState(0);
  const PAGE_SIZE = 12;

  // 搜索相关状态
  const [searchTerm, setSearchTerm] = useState('');
  const [selectedCategory, setSelectedCategory] = useState<number | undefined>();
  const [isSearching, setIsSearching] = useState(false);

  useEffect(() => {
    const searchTerm = searchParams.get('keyword') || '';
    const category = searchParams.get('category');
    const page = Number(searchParams.get('page')) || 1;
    setCurrentPage(page);
    fetchProducts(page, searchTerm, category ? Number(category) : undefined);
  }, [searchParams]);

  const fetchProducts = async (pageNum: number, name?: string, categoryId?: number) => {
    try {
      setLoading(true);
      const response = await productAPI.getProducts({
        pageNum,
        pageSize: PAGE_SIZE,
        name: name || undefined,
        categoryId,
      });
      
      if (response.code === 200) {
        setProducts(response.data.list);
        setTotal(response.data.total);
        setTotalPages(response.data.pages);
        setError(null);
      } else {
        setProducts([]);
        setError(response.msg || '获取商品列表失败');
      }
    } catch (err: any) {
      setProducts([]);
      setError(err.message || '获取商品列表失败，请重试');
      console.error('Error fetching products:', err);
    } finally {
      setLoading(false);
      setIsSearching(false);
    }
  };

  const handleSearch = (e: React.FormEvent) => {
    e.preventDefault();
    setCurrentPage(1);
    setIsSearching(true);
    fetchProducts(1);
  };

  const handleCategoryChange = (categoryId: string) => {
    const id = categoryId ? Number(categoryId) : undefined;
    setSelectedCategory(id);
    setCurrentPage(1);
    fetchProducts(1);
  };

  const clearSearch = () => {
    setSearchTerm('');
    setSelectedCategory(undefined);
    setCurrentPage(1);
    fetchProducts(1);
  };

  if (loading) {
    return (
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        <div className="animate-pulse">
          <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6">
            {[1, 2, 3, 4, 5, 6, 7, 8].map((key) => (
              <div key={key} className="bg-gray-200 h-80 rounded-lg"></div>
            ))}
          </div>
        </div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        <div className="text-center text-red-600">
          <p>{error}</p>
          <button
            onClick={() => {
              setCurrentPage(1);
              setError(null);
              fetchProducts(1);
            }}
            className="mt-4 px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700"
          >
            重试
          </button>
        </div>
      </div>
    );
  }

  if (!products || products.length === 0) {
    return (
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        <div className="text-center text-gray-600">
          <p>暂无商品</p>
        </div>
      </div>
    );
  }

  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">


      {/* 商品列表 */}
      <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-6">
        {products.map((product) => (
          <ProductCard
            key={product.id}
            id={product.id.toString()}
            name={product.name}
            price={product.price}
            imageUrl={product.image}
            description={product.description}
            stock={product.stock}
          />
        ))}
      </div>

      {/* 加载中状态 */}
      {loading && (
        <div className="absolute inset-0 bg-white bg-opacity-50 flex items-center justify-center">
          <div className="text-center">加载中...</div>
        </div>
      )}

      {/* 分页 */}
      {totalPages > 1 && (
        <div className="mt-8 flex justify-center">
          <nav className="flex items-center space-x-2">
            <button
              onClick={() => {
                const newPage = currentPage - 1;
                window.history.pushState(
                  {},
                  '',
                  `?page=${newPage}${searchParams.get('keyword') ? `&keyword=${searchParams.get('keyword')}` : ''}${
                    searchParams.get('category') ? `&category=${searchParams.get('category')}` : ''
                  }`
                );
                setCurrentPage(newPage);
              }}
              disabled={currentPage === 1}
              className={`px-3 py-1 rounded-md ${
                currentPage === 1
                  ? 'bg-gray-100 text-gray-400'
                  : 'bg-white text-gray-700 hover:bg-gray-50'
              }`}
            >
              上一页
            </button>
            {Array.from({ length: totalPages }, (_, i) => i + 1).map((page) => (
              <button
                key={page}
                onClick={() => {
                  window.history.pushState(
                    {},
                    '',
                    `?page=${page}${searchParams.get('keyword') ? `&keyword=${searchParams.get('keyword')}` : ''}${
                      searchParams.get('category') ? `&category=${searchParams.get('category')}` : ''
                    }`
                  );
                  setCurrentPage(page);
                }}
                className={`px-3 py-1 rounded-md ${
                  currentPage === page
                    ? 'bg-blue-600 text-white'
                    : 'bg-white text-gray-700 hover:bg-gray-50'
                }`}
              >
                {page}
              </button>
            ))}
            <button
              onClick={() => {
                const newPage = currentPage + 1;
                window.history.pushState(
                  {},
                  '',
                  `?page=${newPage}${searchParams.get('keyword') ? `&keyword=${searchParams.get('keyword')}` : ''}${
                    searchParams.get('category') ? `&category=${searchParams.get('category')}` : ''
                  }`
                );
                setCurrentPage(newPage);
              }}
              disabled={currentPage === totalPages}
              className={`px-3 py-1 rounded-md ${
                currentPage === totalPages
                  ? 'bg-gray-100 text-gray-400'
                  : 'bg-white text-gray-700 hover:bg-gray-50'
              }`}
            >
              下一页
            </button>
          </nav>
        </div>
      )}
    </div>
  );
};

export default ProductList; 