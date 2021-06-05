import { Options } from 'swagger-jsdoc';

const swaggerOptions: Options = {
  definition: {
    info: {
      title: 'Neo4j Driver Services',
      version: '1.0.0'
    },
    openapi: '3.0.0'
  },
  apis: ['./src/router.ts']
};

export default swaggerOptions;
