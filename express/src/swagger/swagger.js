const swaggerJSDoc = require('swagger-jsdoc');
const config = require('./swagger.json');

const swaggerSpec = swaggerJSDoc(config);

module.exports = swaggerSpec;
