// Configuration file for .env and APM Server
const appRoot = require('app-root-path');
const dotenv = require('dotenv');
const apm = require('elastic-apm-node');

try {
  apm.start({
    serviceName: 'cyfhir_plugin',
    serverUrl: 'http://apm-server:8200'
  });

  // If APM Server is missing, only display error once
  const logs = new Set();
  apm.logger.error = function (log) {
    if (log.match(/APM Server transport error/g) && !logs.has(log)) {
      logs.add(log);
      process.stdout.write('APM Server Missing\n');
    }
  };
} catch (error) {
  console.log(error);
}

const filename = process.env.NODE_ENV === 'test' ? '.env.test' : '.env';
const path = `${appRoot}/${filename}`;
console.log(`Loading configuration from ${path}`);
dotenv.config({
  path
});
