import { useState, useEffect } from 'react';
import { Table, Button, Modal, Form, Input, InputNumber, message, Space, Popconfirm, Select } from 'antd';
import type { ColumnsType } from 'antd/es/table';
import axios from 'axios';

interface Category {
  id: number;
  name: string;
  type: number;
  parentId: number;
  orderNum: number;
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

const Categories = () => {
  const [data, setData] = useState<Category[]>([]);
  const [loading, setLoading] = useState(false);
  const [modalVisible, setModalVisible] = useState(false);
  const [form] = Form.useForm();
  const [editingId, setEditingId] = useState<number | null>(null);
  const [pagination, setPagination] = useState({
    current: 1,
    pageSize: 8,
    total: 0,
  });

  const fetchCategories = async (page = pagination.current, pageSize = pagination.pageSize) => {
    setLoading(true);
    try {
      const token = localStorage.getItem('adminToken');
      const response = await axios.get<PageResponse<Category>>(`/api/admin/category/page`, {
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
        message.error(response.data.msg || '获取分类列表失败');
      }
    } catch (error) {
      message.error('获取分类列表失败');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchCategories();
  }, []);

  const handleAdd = () => {
    form.resetFields();
    setEditingId(null);
    setModalVisible(true);
  };

  const handleEdit = (record: Category) => {
    form.setFieldsValue(record);
    setEditingId(record.id);
    setModalVisible(true);
  };

  const handleDelete = async (id: number) => {
    try {
      const token = localStorage.getItem('adminToken');
      const response = await axios.delete(`/api/admin/category/delete?id=${id}`, {
        headers: { Authorization: `Bearer ${token}` }
      });
      
      if (response.data.code === 200) {
        message.success('删除成功');
        fetchCategories();
      } else {
        message.error(response.data.msg || '删除失败');
      }
    } catch (error) {
      message.error('删除失败');
    }
  };

  const handleSubmit = async () => {
    try {
      const values = await form.validateFields();
      const token = localStorage.getItem('adminToken');
      
      if (editingId) {
        const response = await axios.post('/api/admin/category/update', 
          { ...values, id: editingId },
          { headers: { Authorization: `Bearer ${token}` } }
        );
        
        if (response.data.code === 200) {
          message.success('更新成功');
          setModalVisible(false);
          fetchCategories();
        } else {
          message.error(response.data.msg || '更新失败');
        }
      } else {
        const response = await axios.post('/api/admin/category/add', 
          values,
          { headers: { Authorization: `Bearer ${token}` } }
        );
        
        if (response.data.code === 200) {
          message.success('添加成功');
          setModalVisible(false);
          fetchCategories();
        } else {
          message.error(response.data.msg || '添加失败');
        }
      }
    } catch (error) {
      message.error('操作失败');
    }
  };

  const columns: ColumnsType<Category> = [
    {
      title: 'ID',
      dataIndex: 'id',
      key: 'id',
    },
    {
      title: '名称',
      dataIndex: 'name',
      key: 'name',
    },
    {
      title: '目录级别',
      dataIndex: 'type',
      key: 'type',
      render: (type: number) => `${type}级目录`,
    },
    {
      title: '父级ID',
      dataIndex: 'parentId',
      key: 'parentId',
    },
    {
      title: '排序',
      dataIndex: 'orderNum',
      key: 'orderNum',
    },
    {
      title: '创建时间',
      dataIndex: 'createTime',
      key: 'createTime',
    },
    {
      title: '更新时间',
      dataIndex: 'updateTime',
      key: 'updateTime',
    },
    {
      title: '操作',
      key: 'action',
      render: (_, record) => (
        <Space>
          <Button type="link" onClick={() => handleEdit(record)}>编辑</Button>
          <Popconfirm
            title="确定删除此分类吗？"
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

  return (
    <div>
      <Button type="primary" onClick={handleAdd} style={{ marginBottom: 16 }}>
        新增分类
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
            fetchCategories(page, pageSize);
          }
        }}
      />
      <Modal
        title={editingId ? "编辑分类" : "新增分类"}
        open={modalVisible}
        onOk={handleSubmit}
        onCancel={() => setModalVisible(false)}
      >
        <Form
          form={form}
          layout="vertical"
        >
          <Form.Item
            name="name"
            label="名称"
            rules={[{ required: true, message: '请输入分类名称' }]}
          >
            <Input />
          </Form.Item>
          <Form.Item
            name="type"
            label="目录级别"
            rules={[{ required: true, message: '请选择目录级别' }]}
          >
            <Select>
              <Select.Option value={1}>一级目录</Select.Option>
              <Select.Option value={2}>二级目录</Select.Option>
              <Select.Option value={3}>三级目录</Select.Option>
            </Select>
          </Form.Item>
          <Form.Item
            name="parentId"
            label="父级ID"
            rules={[
              { required: true, message: '请输入父级ID' },
              ({ getFieldValue }) => ({
                validator(_, value) {
                  const type = getFieldValue('type');
                  if (type === 1 && value !== 0) {
                    return Promise.reject('一级目录的父级ID必须为0');
                  }
                  if (type > 1 && value === 0) {
                    return Promise.reject('二级或三级目录的父级ID不能为0');
                  }
                  return Promise.resolve();
                },
              }),
            ]}
          >
            <InputNumber min={0} style={{ width: '100%' }} />
          </Form.Item>
          <Form.Item
            name="orderNum"
            label="排序"
            rules={[{ required: true, message: '请输入排序号' }]}
          >
            <InputNumber min={0} style={{ width: '100%' }} />
          </Form.Item>
        </Form>
      </Modal>
    </div>
  );
};

export default Categories; 