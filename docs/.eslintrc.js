module.exports = {
  root: true,
  env: {
    node: true,
  },
  extends: [
    'plugin:vue/essential',
    '@vue/airbnb',
    '@vue/typescript/recommended',
  ],
  parserOptions: {
    ecmaVersion: 2020,
  },
  rules: {
    'no-console': process.env.NODE_ENV === 'production' ? 'warn' : 'off',
    'no-debugger': process.env.NODE_ENV === 'production' ? 'warn' : 'off',
    'no-param-reassign': process.env.NODE_ENV === 'production' ? 'warn' : 'off',
    'max-len': ['error', { code: 500 }],
    'vue/no-unused-components': process.env.NODE_ENV === 'production' ? 'warn' : 'off',
  },
};
