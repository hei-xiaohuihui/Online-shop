import { useState, useEffect } from 'react';
import { Table, Button, Modal, Form, Input, InputNumber, message, Space, Popconfirm, Upload, Select, Image } from 'antd';
import { PlusOutlined, LoadingOutlined } from '@ant-design/icons';
import type { ColumnsType } from 'antd/es/table';
import type { UploadChangeParam } from 'antd/es/upload';
import type { RcFile, UploadFile, UploadProps } from 'antd/es/upload/interface';
import axios from 'axios';

interface Product {
  id: number;
  name: string;
  price: number;
  categoryId: number;
  description: string;
  image: string;
  stock: number;
  status: number;
  createTime: string;
  updateTime: string;
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

const Products = () => {
  const [data, setData] = useState<Product[]>([]);
  const [loading, setLoading] = useState(false);
  const [modalVisible, setModalVisible] = useState(false);
  const [form] = Form.useForm();
  const [editingId, setEditingId] = useState<number | null>(null);
  const [imageUrl, setImageUrl] = useState<string>();
  const [uploadLoading, setUploadLoading] = useState(false);
  const [pagination, setPagination] = useState({
    current: 1,
    pageSize: 5,
    total: 0,
  });

  const fetchProducts = async (page = pagination.current, pageSize = pagination.pageSize) => {
    setLoading(true);
    try {
      const token = localStorage.getItem('adminToken');
      const response = await axios.get<PageResponse<Product>>(`/api/admin/product/page`, {
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
        message.error(response.data.msg || '获取商品列表失败');
      }
    } catch (error) {
      message.error('获取商品列表失败');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchProducts();
  }, []);

  const handleAdd = () => {
    form.resetFields();
    setEditingId(null);
    setImageUrl(undefined);
    setModalVisible(true);
  };

  const handleEdit = (record: Product) => {
    form.setFieldsValue(record);
    setEditingId(record.id);
    setImageUrl(record.image);
    setModalVisible(true);
  };

  const handleDelete = async (id: number) => {
    try {
      const token = localStorage.getItem('adminToken');
      const response = await axios.delete(`/api/admin/product/delete?id=${id}`, {
        headers: { Authorization: `Bearer ${token}` }
      });
      
      if (response.data.code === 200) {
        message.success('删除成功');
        fetchProducts();
      } else {
        message.error(response.data.msg || '删除失败');
      }
    } catch (error) {
      message.error('删除失败');
    }
  };

  const beforeUpload = (file: RcFile) => {
    const isJpgOrPng = file.type === 'image/jpeg' || file.type === 'image/png';
    if (!isJpgOrPng) {
      message.error('只能上传 JPG/PNG 格式的图片!');
    }
    const isLt2M = file.size / 1024 / 1024 < 2;
    if (!isLt2M) {
      message.error('图片大小不能超过 2MB!');
    }
    return isJpgOrPng && isLt2M;
  };

  const handleUpload = async (info: UploadChangeParam<UploadFile>) => {
    if (info.file.status === 'uploading') {
      setUploadLoading(true);
      return;
    }
    if (info.file.status === 'done') {
      setUploadLoading(false);
      if (info.file.response.code === 200) {
        const imageUrl = info.file.response.data;
        setImageUrl(imageUrl);
        form.setFieldValue('image', imageUrl);
        message.success('上传成功');
      } else {
        message.error(info.file.response.msg || '上传失败');
      }
    } else if (info.file.status === 'error') {
      setUploadLoading(false);
      message.error('上传失败');
    }
  };

  const handleSubmit = async () => {
    try {
      const values = await form.validateFields();
      const token = localStorage.getItem('adminToken');
      
      if (editingId) {
        const response = await axios.post('/api/admin/product/update', 
          { ...values, id: editingId },
          { headers: { Authorization: `Bearer ${token}` } }
        );
        
        if (response.data.code === 200) {
          message.success('更新成功');
          setModalVisible(false);
          fetchProducts();
        } else {
          message.error(response.data.msg || '更新失败');
        }
      } else {
        const response = await axios.post('/api/admin/product/add', 
          values,
          { headers: { Authorization: `Bearer ${token}` } }
        );
        
        if (response.data.code === 200) {
          message.success('添加成功');
          setModalVisible(false);
          fetchProducts();
        } else {
          message.error(response.data.msg || '添加失败');
        }
      }
    } catch (error) {
      message.error('操作失败');
    }
  };

  const columns: ColumnsType<Product> = [
    {
      title: 'ID',
      dataIndex: 'id',
      key: 'id',
      width: 60,
    },
    {
      title: '商品图片',
      dataIndex: 'image',
      key: 'image',
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
      dataIndex: 'name',
      key: 'name',
      width: 200,
    },
    {
      title: '价格',
      dataIndex: 'price',
      key: 'price',
      width: 100,
      render: (price) => `¥${price.toFixed(2)}`,
    },
    {
      title: '库存',
      dataIndex: 'stock',
      key: 'stock',
      width: 80,
    },
    {
      title: '状态',
      dataIndex: 'status',
      key: 'status',
      width: 80,
      render: (status) => (
        <span style={{ color: status === 1 ? '#52c41a' : '#ff4d4f' }}>
          {status === 1 ? '上架' : '下架'}
        </span>
      ),
    },
    {
      title: '创建时间',
      dataIndex: 'createTime',
      key: 'createTime',
      width: 180,
    },
    {
      title: '更新时间',
      dataIndex: 'updateTime',
      key: 'updateTime',
      width: 180,
    },
    {
      title: '操作',
      key: 'action',
      fixed: 'right',
      width: 120,
      render: (_, record) => (
        <Space>
          <Button type="link" onClick={() => handleEdit(record)}>编辑</Button>
          <Popconfirm
            title="确定删除此商品吗？"
            onConfirm={() => handleDelete(record.id)}
            okText="确定"
            cancelText="取消"
          >
            <Button type="link" danger>删除</Button>
          </Popconfirm>
        </Space>
      ),
    },
  ];

  const uploadButton = (
    <div>
      {uploadLoading ? <LoadingOutlined /> : <PlusOutlined />}
      <div style={{ marginTop: 8 }}>上传图片</div>
    </div>
  );

  return (
    <div>
      <Button type="primary" onClick={handleAdd} style={{ marginBottom: 16 }}>
        新增商品
      </Button>
      <Table
        columns={columns}
        dataSource={data}
        rowKey="id"
        loading={loading}
        pagination={{
          ...pagination,
          showSizeChanger: true,
          showQuickJumper: true,
          showTotal: (total) => `共 ${total} 条`,
          onChange: (page, pageSize) => {
            fetchProducts(page, pageSize);
          }
        }}
        scroll={{ x: 1300 }}
      />
      <Modal
        title={editingId ? "编辑商品" : "新增商品"}
        open={modalVisible}
        onOk={handleSubmit}
        onCancel={() => setModalVisible(false)}
        width={800}
      >
        <Form
          form={form}
          layout="vertical"
        >
          <Form.Item
            name="name"
            label="商品名称"
            rules={[{ required: true, message: '请输入商品名称' }]}
          >
            <Input />
          </Form.Item>
          <Form.Item
            name="price"
            label="价格"
            rules={[{ required: true, message: '请输入价格' }]}
          >
            <InputNumber
              min={0}
              precision={2}
              style={{ width: '100%' }}
              prefix="¥"
            />
          </Form.Item>
          <Form.Item
            name="categoryId"
            label="分类ID"
            rules={[{ required: true, message: '请输入分类ID' }]}
          >
            <InputNumber min={1} style={{ width: '100%' }} />
          </Form.Item>
          <Form.Item
            name="stock"
            label="库存"
            rules={[{ required: true, message: '请输入库存' }]}
          >
            <InputNumber min={0} style={{ width: '100%' }} />
          </Form.Item>
          <Form.Item
            name="status"
            label="状态"
            rules={[{ required: true, message: '请选择状态' }]}
            initialValue={1}
          >
            <Select>
              <Select.Option value={1}>上架</Select.Option>
              <Select.Option value={0}>下架</Select.Option>
            </Select>
          </Form.Item>
          <Form.Item
            name="image"
            label="商品图片"
            rules={[{ required: true, message: '请上传商品图片' }]}
          >
            <Upload
              name="file"
              listType="picture-card"
              showUploadList={false}
              action="/api/admin/product/uploadImage"
              beforeUpload={beforeUpload}
              onChange={handleUpload}
              headers={{
                Authorization: `Bearer ${localStorage.getItem('adminToken')}`
              }}
            >
              {imageUrl ? (
                <img src={imageUrl} alt="商品图片" style={{ width: '100%' }} />
              ) : (
                uploadButton
              )}
            </Upload>
          </Form.Item>
          <Form.Item
            name="description"
            label="商品描述"
            rules={[{ required: true, message: '请输入商品描述' }]}
          >
            <Input.TextArea rows={4} />
          </Form.Item>
        </Form>
      </Modal>
    </div>
  );
};

export default Products; 