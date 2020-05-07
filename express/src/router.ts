import express from 'express'

const router = express.Router()
router.get('/test', (req, res) => {
    return 'hello daniel'
})
export default router