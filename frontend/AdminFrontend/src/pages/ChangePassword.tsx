import { useState } from 'react';
import { Card, Form, Input, Button, message } from 'antd';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';

const ChangePassword = () => {
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  const onFinish = async (values: { password: string; confirmPassword: string }) => {
    if (values.password !== values.confirmPassword) {
      message.error('两次输入的密码不一致');
      return;
    }

    setLoading(true);
    try {
      const token = localStorage.getItem('adminToken');
      const response = await axios.post('/api/auth/update',
        { password: values.password },
        { headers: { Authorization: `Bearer ${token}` } }
      );

      if (response.data.code === 200) {
        message.success('密码修改成功');
        navigate('/dashboard/categories');
      } else {
        message.error(response.data.msg || '密码修改失败');
      }
    } catch (error: any) {
      message.error(error.response?.data?.msg || '密码修改失败');
    } finally {
      setLoading(false);
    }
  };

  return (
    <Card title="修改密码" style={{ maxWidth: 500, margin: '0 auto' }}>
      <Form
        name="changePassword"
        onFinish={onFinish}
        autoComplete="off"
        layout="vertical"
      >
        <Form.Item
          label="新密码"
          name="password"
          rules={[
            { required: true, message: '请输入新密码' },
            { min: 6, message: '密码长度不能小于6位' }
          ]}
        >
          <Input.Password />
        </Form.Item>

        <Form.Item
          label="确认新密码"
          name="confirmPassword"
          rules={[
            { required: true, message: '请再次输入新密码' },
            { min: 6, message: '密码长度不能小于6位' }
          ]}
        >
          <Input.Password />
        </Form.Item>

        <Form.Item>
          <Button type="primary" htmlType="submit" loading={loading} block>
            确认修改
          </Button>
        </Form.Item>
      </Form>
    </Card>
  );
};

export default ChangePassword; 