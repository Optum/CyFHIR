import express, { Request, Response } from 'express'
import session from 'express-session'
import cors from 'cors'
import morgan from 'morgan'
import router from './router'
import swaggerDocs from './swagger/swagger'
import swaggerUi from 'swagger-ui-express'
import bodyParser from 'body-parser'

// create app
const app = express()

app.use(session({
    resave: true,
    saveUninitialized: true,
    secret: 'shhh-dont-tell'
}))

// Cross-Origin Resource Sharing - Wildcard for now
app.use(cors({
    'Access-Control-Allow-Origin': '*'
}))

// swagger docs
app.use('/docs', swaggerUi.serve, swaggerUi.setup(swaggerDocs))

// Custom Morgan metric for payload size in bytes
morgan.token('content-size-dynamic', function contentSize(req: Request, res: Response) {
    try {
        const bytes = res.req.res[Object.getOwnPropertySymbols(res.req.res)[2]]['content-length'][1] + ' bytes'
        return bytes
    } catch (error) {
        console.log(error)
    }
})

// Custom Morgan metric for Response Time in Seconds
morgan.token('response-time-seconds', function getResponseTimeInSeconds(req: Request, res: Response) {
    return (this['response-time'](req, res) / 1000).toFixed(2)
})

app.use(morgan(':method :url :status :content-size-dynamic - :response-time-seconds s'))

app.use(bodyParser.json({
    limit: '50mb'
}))

app.use(bodyParser.urlencoded({
    extended: false
}))

// Add in api routing
app.use('/api/', router)

export default app