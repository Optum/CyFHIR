require('./config');

const http = require('http');
const app = require('./app');

const {
  PORT = 3000
} = process.env;

const server = http.createServer(app);

const startServer = async (port = PORT) => {
  try {
    await server.listen(port, () => {
      console.log(`app listening on port ${port}`);
    }).on('error', (err) => {
      console.log(err);
    });
  } catch (error) {
    console.log(error);
  }
};

const stopServer = async () => {
  try {
    await server.close();
  } catch (error) {
    console.log('error closing server');
  }
};

if (require.main === module) {
  startServer().catch((err) => console.error(err));
}

module.exports = {
  startServer,
  stopServer
};
