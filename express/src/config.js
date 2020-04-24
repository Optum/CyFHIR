try {
  const apm = require('elastic-apm-node');
  apm.start({
  // Override service name from package.json
    serviceName: 'cyfhir_plugin',
    serverUrl: 'http://apm-server:8200'
  });
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
const appRoot = require('app-root-path');
const dotenv = require('dotenv');
const filename = process.env.NODE_ENV === 'test' ? '.env.test' : '.env';
const path = `${appRoot}/${filename}`;
console.log(`Loading configuration from ${path}`);
dotenv.config({
  path
});
