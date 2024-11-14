import express from 'express';
import { createLog, getLogs } from '../controllers/logController';

const router = express.Router();

// Rutas
router.post('/logs', createLog);
router.get('/logs', getLogs);

export default router;
