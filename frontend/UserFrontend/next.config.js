/** @type {import('next').NextConfig} */
const nextConfig = {
  images: {
    remotePatterns: [
      {
        protocol: 'http',
        hostname: '111.231.103.117',
        port: '8081',
        pathname: '/images/**',
      },
      {
        protocol: 'http',
        hostname: 'localhost',
        port: '8181',
        pathname: '/static/images/**',
      },
      {
        protocol: 'http',
        hostname: '192.168.198.1',
        port: '9002',
        pathname: '/images/**',
      },
    ],
  },
  async rewrites() {
    return [
      {
        source: '/api/:path*',
        destination: 'http://localhost/api/:path*',
      },
      {
        source: '/product/detail/:id',
        destination: 'http://localhost/api/user/product/detail/:id',
      },
    ];
  },
};

module.exports = nextConfig; 