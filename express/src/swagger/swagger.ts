import swaggerJSDoc, { Options } from 'swagger-jsdoc'
import config from './config'

const swaggerDocs = swaggerJSDoc(config)
export default swaggerDocs