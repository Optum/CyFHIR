module.exports = {
  publicPath: process.env.NODE_ENV === 'production'
    ? '/CyFHIR/'
    : '/',
  devServer: {
    host: 'localhost',
    port: 8080,
  },
  chainWebpack: (config) => {
    config
      .plugin('html')
      .tap((args) => {
        args[0].title = 'CyFHIR Documentation';
        args[0].meta = {};
        args[0].meta['google-site-verification'] = 'iUlDXbqypu_uqudYtPwewgUgOcHxA5skBkO0c2t3roI';
        args[0].meta.description = 'Documentation site for CyFHIR, a Neo4j plugin for FHIR resources built at Optum.';

        return args;
      });
  },
};
