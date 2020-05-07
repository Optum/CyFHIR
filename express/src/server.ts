import app from './app'
import http, { Server } from 'http'

const port: number = parseInt(process.env.PORT || '3000')

const server: Server = http.createServer(app);

function startServer(port: number): Promise<any> {
    return new Promise((resolve, reject) => {
        server.listen(port, () => {
            resolve(port)
        })
        .on('error', (err: object) => {
            reject(err)
        })
    })
}

function stopServer(): Promise<any> {
    return new Promise((resolve, reject) => {
        server.close((err) => {
            reject(err)
        })
    })
}

// Initialize server
if (require.main === module) {
    startServer(port)
    .then(port => console.log(`Server running on port ${port}`))
    .catch(error => {
        console.log(error)
        process.exit(1)
    });
}
