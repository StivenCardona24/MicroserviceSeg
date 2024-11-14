import { NextFunction, Request, Response } from 'express';
import { Profile } from '../models/profile';

// Obtener perfil de usuario
export const getProfile = async (req: Request, res: Response): Promise<any> => {
  try {
    const { userId } = req.params;
    const profile = await Profile.findOne({ userId });
    if (!profile) return res.status(404).json({ message: 'Perfil no encontrado' });
    res.json(profile);
  } catch (error) {
    res.status(500).json({ message: 'Error del servidor' });
  }
};

// Crear o actualizar perfil
export const upsertProfile = async (req: Request, res: Response) => {
  try {
    const { userId } = req.body;
    const updatedProfile = await Profile.findOneAndUpdate(
      { userId },
      { $set: req.body },
      { new: true, upsert: true }
    );
    res.json(updatedProfile);
  } catch (error) {
    res.status(500).json({ message: 'Error al guardar el perfil' });
  }
};

