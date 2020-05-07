import express, { Request, Response } from 'express'
import session from 'express-session'
import cors from 'cors'
import morgan from 'morgan'

// import Router from './router'
import swaggerDocs from './swagger/swagger'
import swaggerUi from 'swagger-ui-express'
// import bodyParser from 'body-parser'

// create app
const app = express()

app.use(session({
  resave: true,
  saveUninitialized: true,
  secret: 'shhh-dont-tell'
}));

// Custom Morgan metric for Response Time in Seconds
morgan.token('response-time-seconds', function getResponseTimeInSeconds (req, res) {
  return (this['response-time'](req, res) / 1000).toFixed(2);
});

// Cross-Origin Resource Sharing - Wildcard for now
app.use(cors({
  'Access-Control-Allow-Origin': '*'
}));

app.use('/test', (req, res) => {
  res.send('hello world')
})

// swagger docs
app.use('/docs', swaggerUi.serve, swaggerUi.setup(swaggerDocs))

app.get('/', (req, res) => res.send('Hello World!'))

export default app