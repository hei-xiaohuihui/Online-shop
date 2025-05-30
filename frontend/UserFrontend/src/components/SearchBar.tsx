import { useState, FormEvent } from 'react';
import { useRouter } from 'next/navigation';
import { FiSearch } from 'react-icons/fi';

const SearchBar = () => {
  const router = useRouter();
  const [searchTerm, setSearchTerm] = useState('');

  const handleSubmit = (e: FormEvent) => {
    e.preventDefault();
    if (searchTerm.trim()) {
      router.push(`/?keyword=${encodeURIComponent(searchTerm.trim())}`);
    }
  };

  return (
    <form onSubmit={handleSubmit} className="flex-1 max-w-xl">
      <div className="relative">
        <input
          type="text"
          value={searchTerm}
          onChange={(e) => setSearchTerm(e.target.value)}
          placeholder="搜索商品..."
          className="w-full px-4 py-2 rounded-full border border-gray-300 focus:outline-none focus:border-blue-500 focus:ring-1 focus:ring-blue-500"
        />
        <button
          type="submit"
          className="absolute right-0 top-0 h-full px-4 text-gray-500 hover:text-gray-700"
        >
          <FiSearch className="w-5 h-5" />
        </button>
      </div>
    </form>
  );
};

export default SearchBar; 