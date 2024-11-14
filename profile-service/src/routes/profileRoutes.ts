import { Router } from 'express';
import { upsertProfile, getProfile } from '../controllers/profileController';

const router = Router();

router.get( '/:userId', getProfile );
// router.get('/:userId', getProfile); // Obtener perfil de usuario
router.post('/', upsertProfile); // Crear o actualizar perfil

router.put('/:userId', upsertProfile); // Actualizar perfil

export default router;
