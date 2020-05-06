import App from './app'

const port = parseInt(process.env.PORT || '3000')
const app = new App()
app.httpServer.set('port', port)
export default app.start(port)
    .then(port => console.log(`Server running on port ${port}`))
    .catch(error => {
        console.log(error)
        process.exit(1)
    })
