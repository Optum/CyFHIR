const express = require('express');
const bodyParser = require('body-parser');
const cors = require('cors');
const routes = require('./router');
const session = require('express-session');
const morgan = require('morgan');
const swaggerSpec = require('./swagger/swagger');
const swaggerUi = require('swagger-ui-express');

const app = express();

app.use(session({
  resave: true,
  saveUninitialized: true,
  secret: 'shhh-dont-tell'
}));

// Cross-Origin Resource Sharing - Wildcard for now
app.use(cors({
  'Access-Control-Allow-Origin': '*'
}));

app.use('/docs', swaggerUi.serve);
app.get('/docs', swaggerUi.setup(swaggerSpec));

morgan.token('response-time-seconds', function getResponseTimeInSeconds (req, res) {
  return (this['response-time'](req, res) / 1000).toFixed(2);
});

morgan.token('content-size-dynamic', function contentSize (req, res) {
  try {
    const bytes = res.req.res[Object.getOwnPropertySymbols(res.req.res)[2]]['content-length'][1] + ' bytes';
    return bytes;
  } catch (error) {
    console.log(error);
  }
});

app.use(morgan(':method :url :status :content-size-dynamic - :response-time-seconds s'));

app.use(bodyParser.json({
  limit: '50mb'
}));

app.use(bodyParser.urlencoded({
  extended: false
}));

app.use('/api/', routes);

module.exports = app;
