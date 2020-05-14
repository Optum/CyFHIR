import config from './config';
import app from './app';
import http, { Server } from 'http';
import neo4jController from './controllers/neo4jController';

const port: number = parseInt(process.env.PORT || '3000');
const server: Server = http.createServer(app);

function startServer (port: number): Promise<any> {
  return new Promise((resolve, reject) => {
    server.listen(port, () => {
      resolve(port);
    })
      .on('error', (err: object) => {
        reject(err);
      });
  });
}

function stopServer (): Promise<any> {
  return new Promise((resolve, reject) => {
    server.close((err) => {
      reject(err);
    });
  });
}

config();
// Initialize server
if (require.main === module) {
  startServer(port)
    .then(port => {
      console.log(`Server running on port ${port}`);
      neo4jController.verifyConnection();
    })
    .catch(error => {
      console.log(error);
      process.exit(1);
    });
}
