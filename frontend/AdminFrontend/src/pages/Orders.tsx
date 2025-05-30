import { useState, useEffect } from 'react';
import { Table, Button, Modal, message, Space, Tag, Descriptions, Image } from 'antd';
import type { ColumnsType } from 'antd/es/table';
import axios from 'axios';

interface OrderDetail {
  orderNum: string;
  productId: number;
  productName: string;
  productImage: string;
  unitPrice: number;
  quantity: number;
  totalPrice: number;
}

interface Order {
  orderNum: string;
  userId: number;
  totalPrice: number;
  consignee: string;
  phone: string;
  address: string;
  status: number;
  orderStatusStr: string;
  postage: number;
  payMethodStr: string | null;
  payTime: string | null;
  cancelReason: string | null;
  cancelTime: string | null;
  deliveryTime: string | null;
  completeTime: string | null;
  createTime: string;
  orderDetailVoList: OrderDetail[];
}

interface PageResponse<T> {
  code: number;
  msg: string;
  data: {
    total: number;
    list: T[];
    pageNum: number;
    pageSize: number;
  };
}

const Orders = () => {
  const [data, setData] = useState<Order[]>([]);
  const [loading, setLoading] = useState(false);
  const [detailVisible, setDetailVisible] = useState(false);
  const [selectedOrder, setSelectedOrder] = useState<Order | null>(null);
  const [pagination, setPagination] = useState({
    current: 1,
    pageSize: 8,
    total: 0,
  });

  const fetchOrders = async (page = pagination.current, pageSize = pagination.pageSize) => {
    setLoading(true);
    try {
      const token = localStorage.getItem('adminToken');
      const response = await axios.get<PageResponse<Order>>(`/api/admin/order/page`, {
        params: { pageNum: page, pageSize },
        headers: { Authorization: `Bearer ${token}` }
      });
      
      if (response.data.code === 200) {
        setData(response.data.data.list || []);
        setPagination({
          ...pagination,
          current: response.data.data.pageNum,
          total: response.data.data.total,
          pageSize: response.data.data.pageSize
        });
      } else {
        message.error(response.data.msg || '获取订单列表失败');
      }
    } catch (error) {
      message.error('获取订单列表失败');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchOrders();
  }, []);

  const handleViewDetails = (record: Order) => {
    setSelectedOrder(record);
    setDetailVisible(true);
  };

  const handleDelivery = async (orderNum: string) => {
    try {
      const token = localStorage.getItem('adminToken');
      const response = await axios.post(`/api/admin/order/delivery?orderNum=${orderNum}`, 
        null,
        { 
          headers: { 
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json'
          }
        }
      );
      
      if (response.data.code === 200) {
        message.success('发货成功');
        fetchOrders();
      } else {
        message.error(response.data.msg || '发货失败');
      }
    } catch (error: any) {
      console.error('发货请求错误:', error);
      message.error(error.response?.data?.msg || '发货失败，请检查网络连接');
    }
  };

  const getStatusTag = (status: number, statusStr: string) => {
    let color = '';
    switch (status) {
      case 0:
        color = 'red';
        break;
      case 10:
        color = 'orange';
        break;
      case 20:
        color = 'blue';
        break;
      case 30:
        color = 'cyan';
        break;
      case 40:
        color = 'green';
        break;
      default:
        color = 'default';
    }
    return <Tag color={color}>{statusStr}</Tag>;
  };

  const columns: ColumnsType<Order> = [
    {
      title: '订单号',
      dataIndex: 'orderNum',
      key: 'orderNum',
      width: 220,
    },
    {
      title: '收货人',
      dataIndex: 'consignee',
      key: 'consignee',
      width: 100,
    },
    {
      title: '手机号',
      dataIndex: 'phone',
      key: 'phone',
      width: 120,
    },
    {
      title: '订单金额',
      dataIndex: 'totalPrice',
      key: 'totalPrice',
      width: 100,
      render: (price) => `¥${price.toFixed(2)}`,
    },
    {
      title: '订单状态',
      dataIndex: 'orderStatusStr',
      key: 'status',
      width: 100,
      render: (text, record) => getStatusTag(record.status, text),
    },
    {
      title: '支付方式',
      dataIndex: 'payMethodStr',
      key: 'payMethod',
      width: 100,
    },
    {
      title: '创建时间',
      dataIndex: 'createTime',
      key: 'createTime',
      width: 180,
    },
    {
      title: '操作',
      key: 'action',
      fixed: 'right',
      width: 150,
      render: (_, record) => (
        <Space>
          <Button type="link" onClick={() => handleViewDetails(record)}>
            查看详情
          </Button>
          {record.status === 20 && (
            <Button type="link" onClick={() => handleDelivery(record.orderNum)}>
              发货
            </Button>
          )}
        </Space>
      ),
    },
  ];

  const orderDetailColumns: ColumnsType<OrderDetail> = [
    {
      title: '商品图片',
      dataIndex: 'productImage',
      key: 'productImage',
      width: 100,
      render: (image) => (
        <Image
          src={image}
          alt="商品图片"
          style={{ width: 50, height: 50, objectFit: 'cover' }}
        />
      ),
    },
    {
      title: '商品名称',
      dataIndex: 'productName',
      key: 'productName',
    },
    {
      title: '单价',
      dataIndex: 'unitPrice',
      key: 'unitPrice',
      render: (price) => `¥${price.toFixed(2)}`,
    },
    {
      title: '数量',
      dataIndex: 'quantity',
      key: 'quantity',
    },
    {
      title: '小计',
      dataIndex: 'totalPrice',
      key: 'totalPrice',
      render: (price) => `¥${price.toFixed(2)}`,
    },
  ];

  return (
    <div>
      <Table
        columns={columns}
        dataSource={data}
        rowKey="orderNum"
        loading={loading}
        pagination={{
          ...pagination,
          showSizeChanger: true,
          showQuickJumper: true,
          showTotal: (total) => `共 ${total} 条`,
          onChange: (page, pageSize) => {
            fetchOrders(page, pageSize);
          }
        }}
        scroll={{ x: 1300 }}
      />
      <Modal
        title="订单详情"
        open={detailVisible}
        onCancel={() => setDetailVisible(false)}
        footer={null}
        width={1000}
      >
        {selectedOrder && (
          <>
            <Descriptions title="基本信息" bordered column={2}>
              <Descriptions.Item label="订单号">{selectedOrder.orderNum}</Descriptions.Item>
              <Descriptions.Item label="订单状态">
                {getStatusTag(selectedOrder.status, selectedOrder.orderStatusStr)}
              </Descriptions.Item>
              <Descriptions.Item label="收货人">{selectedOrder.consignee}</Descriptions.Item>
              <Descriptions.Item label="联系电话">{selectedOrder.phone}</Descriptions.Item>
              <Descriptions.Item label="收货地址" span={2}>{selectedOrder.address}</Descriptions.Item>
              <Descriptions.Item label="支付方式">{selectedOrder.payMethodStr || '-'}</Descriptions.Item>
              <Descriptions.Item label="订单金额">¥{selectedOrder.totalPrice.toFixed(2)}</Descriptions.Item>
              <Descriptions.Item label="创建时间">{selectedOrder.createTime}</Descriptions.Item>
              <Descriptions.Item label="支付时间">{selectedOrder.payTime || '-'}</Descriptions.Item>
              {selectedOrder.cancelReason && (
                <Descriptions.Item label="取消原因" span={2}>{selectedOrder.cancelReason}</Descriptions.Item>
              )}
              {selectedOrder.cancelTime && (
                <Descriptions.Item label="取消时间" span={2}>{selectedOrder.cancelTime}</Descriptions.Item>
              )}
              {selectedOrder.deliveryTime && (
                <Descriptions.Item label="发货时间" span={2}>{selectedOrder.deliveryTime}</Descriptions.Item>
              )}
              {selectedOrder.completeTime && (
                <Descriptions.Item label="完成时间" span={2}>{selectedOrder.completeTime}</Descriptions.Item>
              )}
            </Descriptions>
            <div style={{ marginTop: 24 }}>
              <h3>商品信息</h3>
              <Table
                columns={orderDetailColumns}
                dataSource={selectedOrder.orderDetailVoList}
                rowKey="productId"
                pagination={false}
              />
            </div>
          </>
        )}
      </Modal>
    </div>
  );
};

export default Orders; 