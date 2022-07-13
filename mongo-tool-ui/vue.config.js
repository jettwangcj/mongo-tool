module.exports = {
  devServer: {
      port: 3000,
      public: 'localhost:3000',
      proxy: {
      '/api': {
        target: process.env.SERVER_PROXY_API, //服务端地址
        ws: true,
        changeOrigin: true, // 允许跨域
        pathRewrite: {
         '^/api': ''   // 标识替换，使用 '/api' 代替真实的接口地址
        }
      }
    }
  }
}
