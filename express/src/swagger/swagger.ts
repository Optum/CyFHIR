import swaggerJSDoc, { Options } from 'swagger-jsdoc'
// import config from './swagger.json';

const swaggerOptions: Options = {
    definition: {
        info: {
            title: "CDS Service",
            version: "1.0.0",
            description: "test"
        },
        openapi: "3.0.0"
    },
    apis: ["./src/app.ts"]
}

const swaggerDocs = swaggerJSDoc(swaggerOptions)
export default swaggerDocs