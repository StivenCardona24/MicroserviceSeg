import { Request, Response } from 'express';
import { Log } from '../models/logModel';

// Crear un nuevo log
export const createLog = async (req: Request, res: Response): Promise<void> => {
  try {
    const { application, logType, module, summary, description } = req.body;

    const newLog = new Log({
      application,
      logType,
      module,
      summary,
      description,
    });

    await newLog.save();
    res.status(201).json(newLog);
  } catch (error) {
    res.status(500).json({ message: 'Error al crear el log', error });
  }
};

// Obtener logs con filtros y paginación
export const getLogs = async (req: Request, res: Response): Promise<void> => {
  try {
    const { application, logType, startDate, endDate, page = 1, limit = 10 } = req.query;

    const query: any = {};

    if (application) {
      query.application = application;
    }
    if (logType) {
      query.logType = logType;
    }
    if (startDate && endDate) {
      query.timestamp = { $gte: new Date(startDate as string), $lte: new Date(endDate as string) };
    }

    const logs = await Log.find(query)
      .sort({ timestamp: -1 })
      .skip((Number(page) - 1) * Number(limit))
      .limit(Number(limit));

    const total = await Log.countDocuments(query);

    res.json({
      logs,
      totalPages: Math.ceil(total / Number(limit)),
      currentPage: Number(page),
    });
  } catch (error) {
    res.status(500).json({ message: 'Error al obtener los logs', error });
  }
};
