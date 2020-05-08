import appRoot from 'app-root-path';
import dotenv from 'dotenv'
import apm from 'elastic-apm-node'

function loggerError(log) {
    // If APM Server is missing, only display error once
    const logs = new Set();
    if (log.match(/APM Server transport error/g) && !logs.has(log)) {
        logs.add(log);
        process.stdout.write('APM Server Missing\n');
    }
}


function config() {
    try {
        apm.start({
            serviceName: 'cyfhir_plugin',
            serverUrl: 'http://apm-server:8200'
        });
        apm.logger.error = (log) => loggerError(log)
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