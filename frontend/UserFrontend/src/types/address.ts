export interface Address {
  id: number;
  consignee: string;
  sex: number;
  phone: string;
  address: string;
  label: string;
  defaulted: number;
  createTime?: string;
  updateTime?: string;
}

export interface AddressFormData {
  consignee: string;
  sex: number;
  phone: string;
  address: string;
  label: string;
}

export interface AddressListResponse {
  code: number;
  msg: string;
  data: Address[];
} 