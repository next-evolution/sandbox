module.exports = {
  publicPath: '/app',
  devServer: {
    proxy: {
      "/core/api/": {
        // src/mixin/common.js#apiRootの値と同一にする
        target: "http://localhost:20080",
        // pathRewrite使うとcsrfTokenが一致しなくなるので使わない！！
        // pathRewrite: {'^/api' : '/coreapi'}
        changeOrigin: true,
      },
    },
  },
}
