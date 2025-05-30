import axios, { AxiosResponse } from 'axios';
import { jwtDecode } from 'jwt-decode';

interface JwtPayload {
  userId: number;
  username: string;
  userRole: number;
  exp: number;
}

export interface ApiResponse<T> {
  code: number;
  msg: string;
  data: T;
}

export interface Product {
  id: number;
  name: string;
  price: number;
  description: string;
  image: string;
  categoryId: number;
  status: number;
  stock: number;
  createTime: string;
  updateTime: string;
}

export interface ProductListResponse {
  total: number;
  list: Product[];
  pageNum: number;
  pageSize: number;
  size: number;
  startRow: number;
  endRow: number;
  pages: number;
  prePage: number;
  nextPage: number;
  isFirstPage: boolean;
  isLastPage: boolean;
  hasPreviousPage: boolean;
  hasNextPage: boolean;
  navigatePages: number;
  navigatepageNums: number[];
  navigateFirstPage: number;
  navigateLastPage: number;
}

export interface CartItem {
  id: number;
  productId: number;
  name: string;
  productImage: string;
  price: number;
  quantity: number;
  selected: number;
  stock: number;
}

export interface CheckoutItem extends CartItem {
  totalPrice: number;
}

interface ProductSearchParams {
  pageNum: number;
  pageSize: number;
  name?: string;
  categoryId?: number;
}

export interface Address {
  id: number;
  name: string;
  phone: string;
  province: string;
  city: string;
  district: string;
  address: string;
  isDefault: number;
}

export interface OrderItem {
  productId: number;
  productName: string;
  productImage: string;
  quantity: number;
  price: number;
}

export interface OrderDetailVo {
  orderNum: string;
  productId: number;
  productName: string;
  productImage: string;
  unitPrice: number;
  quantity: number;
  totalPrice: number;
}

export interface Order {
  orderNum: string;
  userId: number;
  totalPrice: number;
  consignee: string;
  phone: string;
  address: string;
  orderStatusStr: string;
  status: number;
  postage: number;
  payMethodStr: string | null;
  payTime: string | null;
  cancelReason: string | null;
  cancelTime: string | null;
  deliveryTime: string | null;
  completeTime: string | null;
  createTime: string;
  orderDetailVoList: OrderDetailVo[];
}

export interface OrderListResponse {
  total: number;
  list: Order[];
  pageNum: number;
  pageSize: number;
  pages: number;
  size: number;
  startRow: number;
  endRow: number;
  prePage: number;
  nextPage: number;
  isFirstPage: boolean;
  isLastPage: boolean;
  hasPreviousPage: boolean;
  hasNextPage: boolean;
  navigatePages: number;
  navigatepageNums: number[];
  navigateFirstPage: number;
  navigateLastPage: number;
}

const api = axios.create({
  baseURL: '/api',
  timeout: 10000,
});

// 请求拦截器
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// 响应拦截器
api.interceptors.response.use(
  (response: AxiosResponse) => {
    return response.data;
  },
  (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem('token');
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

// 用户相关接口
export const authAPI = {
  login: async (data: { username: string; password: string }): Promise<{ token: string; user: { id: number; username: string; role: number } }> => {
    const result = await api.post<any, ApiResponse<string>>('/auth/login', data);
    if (result.code !== 200) {
      throw new Error(result.msg);
    }
    const token = result.data;
    const decodedToken = jwtDecode<JwtPayload>(token);
    return {
      token,
      user: {
        id: decodedToken.userId,
        username: decodedToken.username,
        role: decodedToken.userRole
      }
    };
  },
  register: (data: { username: string; password: string }): Promise<ApiResponse<null>> =>
    api.post('/auth/register', data),
  updateUser: (data: { username: string; password: string }): Promise<ApiResponse<null>> =>
    api.post('/auth/update', data),
};

// 商品相关接口
export const productAPI = {
  getProducts: (params: ProductSearchParams): Promise<ApiResponse<ProductListResponse>> =>
    api.get('/user/product/page', { params }),
  searchProducts: (params: { q: string; pageNum: number; pageSize: number }): Promise<ApiResponse<ProductListResponse>> =>
    api.get('/user/product/search', { params }),
  getProductDetail: (id: string): Promise<ApiResponse<Product>> =>
    api.get(`/user/product/detail`, { params: { id } }),
};

// 购物车相关接口
export const cartAPI = {
  addToCart: (data: { productId: number; count: number }): Promise<ApiResponse<null>> =>
    api.post('/user/cart/add', data),
  
  updateCart: (data: { productId: number; quantity: number; selected: number }): Promise<ApiResponse<null>> =>
    api.post('/user/cart/update', data),
  
  batchSelect: (productIds: number[], selected: number): Promise<ApiResponse<null>> =>
    api.post(`/user/cart/batchSelect`, null, { 
      params: { 
        productIds: productIds.join(','), 
        selected 
      }
    }),
  
  removeFromCart: (productId: number): Promise<ApiResponse<null>> =>
    api.delete('/user/cart/delete', { params: { productId } }),
  
  selectAll: (selected: number): Promise<ApiResponse<null>> =>
    api.post('/user/cart/selectAll', null, { params: { selected } }),
  
  getCartList: (): Promise<ApiResponse<CartItem[]>> =>
    api.get('/user/cart/list'),
  
  getCheckoutItems: (): Promise<ApiResponse<CheckoutItem[]>> =>
    api.get('/user/cart/check'),
};

// 地址相关接口
export const addressAPI = {
  getAddressList: (): Promise<ApiResponse<Address[]>> =>
    api.get('/auth/addressBook/list'),
  addAddress: (data: {
    consignee: string;
    sex: number;
    phone: string;
    address: string;
    label: string;
  }): Promise<ApiResponse<null>> => api.post('/auth/addressBook/add', data),
  updateAddress: (data: {
    id: number;
    consignee: string;
    sex: number;
    phone: string;
    address: string;
    label: string;
    defaulted: number;
  }): Promise<ApiResponse<null>> => api.post('/auth/addressBook/update', data),
  deleteAddress: (id: number): Promise<ApiResponse<null>> =>
    api.delete('/auth/addressBook/delete', { params: { id } }),
  getAddressDetail: (id: number): Promise<ApiResponse<any>> =>
    api.get('/auth/addressBook/' + id),
};

// 订单相关接口
export const orderAPI = {
  createOrder: (data: { addressBookId: number }): Promise<ApiResponse<null>> =>
    api.post('/user/order/add', data),
  cancelOrder: (data: { orderNum: number | string; cancelReason: string }): Promise<ApiResponse<null>> =>
    api.post('/user/order/cancel', data),
  getOrderDetail: (orderNum: string): Promise<ApiResponse<Order>> =>
    api.get('/user/order/detail', { params: { orderNum } }),
  payOrder: (orderNum: string): Promise<ApiResponse<null>> =>
    api.post('/user/order/pay', null, { params: { orderNum } }),
  completeOrder: (orderNum: string): Promise<ApiResponse<null>> =>
    api.post('/user/order/complete', null, { params: { orderNum } }),
  getOrderList: (params: { pageNum: number; pageSize: number }): Promise<ApiResponse<OrderListResponse>> =>
    api.get('/user/order/page', { params }),
};

export default api; 