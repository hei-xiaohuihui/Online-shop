import { useState } from 'react';
import { Layout, Menu, Button, theme } from 'antd';
import {
  MenuFoldOutlined,
  MenuUnfoldOutlined,
  ShopOutlined,
  AppstoreOutlined,
  OrderedListOutlined,
  LogoutOutlined,
  KeyOutlined,
} from '@ant-design/icons';
import { Outlet, useNavigate } from 'react-router-dom';

const { Header, Sider, Content } = Layout;

const AdminLayout = () => {
  const [collapsed, setCollapsed] = useState(false);
  const navigate = useNavigate();
  const {
    token: { colorBgContainer, borderRadiusLG },
  } = theme.useToken();

  const handleMenuClick = (key: string) => {
    navigate(key);
  };

  const handleLogout = () => {
    localStorage.removeItem('adminToken');
    navigate('/login');
  };

  return (
    <Layout style={{ minHeight: '100vh' }}>
      <Sider trigger={null} collapsible collapsed={collapsed}>
        <div
          style={{
            height: 64,
            margin: 16,
            color: '#fff',
            fontSize: collapsed ? '14px' : '18px',
            fontWeight: 'bold',
            textAlign: 'center',
            lineHeight: '64px',
            whiteSpace: 'nowrap',
            overflow: 'hidden'
          }}
        >
          {collapsed ? '管理系统' : '后台管理系统'}
        </div>
        <Menu
          theme="dark"
          mode="inline"
          defaultSelectedKeys={['/dashboard/categories']}
          items={[
            {
              key: '/dashboard/categories',
              icon: <AppstoreOutlined />,
              label: '分类管理',
            },
            {
              key: '/dashboard/products',
              icon: <ShopOutlined />,
              label: '商品管理',
            },
            {
              key: '/dashboard/orders',
              icon: <OrderedListOutlined />,
              label: '订单管理',
            },
          ]}
          onClick={({ key }) => handleMenuClick(key)}
        />
      </Sider>
      <Layout>
        <Header style={{ padding: 0, background: colorBgContainer }}>
          <Button
            type="text"
            icon={collapsed ? <MenuUnfoldOutlined /> : <MenuFoldOutlined />}
            onClick={() => setCollapsed(!collapsed)}
            style={{
              fontSize: '16px',
              width: 64,
              height: 64,
            }}
          />
          <Button
            type="text"
            icon={<KeyOutlined />}
            onClick={() => navigate('/dashboard/change-password')}
            style={{
              fontSize: '16px',
              float: 'right',
              margin: '16px 8px',
            }}
          >
            修改密码
          </Button>
          <Button
            type="text"
            icon={<LogoutOutlined />}
            onClick={handleLogout}
            style={{
              fontSize: '16px',
              float: 'right',
              margin: '16px 24px 16px 8px',
            }}
          >
            退出登录
          </Button>
        </Header>
        <Content
          style={{
            margin: '24px 16px',
            padding: 24,
            background: colorBgContainer,
            borderRadius: borderRadiusLG,
            minHeight: 280,
          }}
        >
          <Outlet />
        </Content>
      </Layout>
    </Layout>
  );
};

export default AdminLayout; 