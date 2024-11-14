import express from 'express';
import { healthCheck, healthLive, healthReady } from '../controllers/healthController';

const router = express.Router();

// Rutas
router.get('/health', healthCheck);
router.get('/health/live', healthLive);
router.get('/health/ready', healthReady);

export default router;