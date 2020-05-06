import express from 'express'

class Router {
    private router
    constructor(server) {
        this.router = express.Router()
        server.use('/', this.router)
    }
}
export default Router