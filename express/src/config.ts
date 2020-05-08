import appRoot from 'app-root-path';
import dotenv from 'dotenv'
import apm from 'elastic-apm-node'

function loggerError(error, log) {
  // If APM Server is missing, only display error once
  if (error.match(/APM Server transport error/g) && !log.has(error)) {
    log.add(error);
    process.stdout.write('APM Server Missing\n');
  }
}

function config() {
  const logs = new Set();
  try {
    apm.start({
      serviceName: 'cyfhir_plugin',
      serverUrl: 'http://apm-server:8200'
    });
    apm.logger.error = (error) => loggerError(error, logs)
  } catch (error) {
    console.log(error);
  }

  const filename = process.env.NODE_ENV === 'test' ? '.env.test' : '.env';
  const path = `${appRoot}/${filename}`;

  console.log(`Loading configuration from ${path}`);
  dotenv.config({
    path
  });
}
export default config;
