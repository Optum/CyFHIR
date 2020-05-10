module.exports = {
  publicPath: process.env.NODE_ENV === 'production'
    ? '/CyFHIR/'
    : '/',
  chainWebpack: (config) => {
    config
      .plugin('html')
      .tap((args) => {
        args[0].title = 'CyFHIR';
        args[0].meta = {};
        args[0].meta['google-site-verification'] = 'iUlDXbqypu_uqudYtPwewgUgOcHxA5skBkO0c2t3roI';

        return args;
      });
  },
};
