import express, { Application, Request, Response } from 'express'
import Router from './router'
import swaggerSpec from './swagger/swagger'
import swaggerUi from 'swagger-ui-express'
import bodyParser from 'body-parser'

class App {
  public httpServer

  constructor() {
    this.httpServer = express()
    const router = new Router(this.httpServer)

    // this.httpServer.get('/test', (req: Request, res: Response) => {
    //   res.send('Hello')
    // })

    // swagger docs
    // this.httpServer.use('/docs', swaggerUi.serve, swaggerUi.setup(swaggerSpec));
    // this.httpServer.get('/docs', swaggerUi.setup(swaggerSpec));
  }

  public start = (port) => {
    return new Promise((resolve, reject) => {
      this.httpServer.listen(port, () => resolve(port))
      .on('error', (err: object) => reject(err))
    })
  }
}
export default App;