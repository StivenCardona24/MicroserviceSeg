import { Request, Response } from 'express';
import moment from 'moment';

const startTime = moment();

export const healthCheck = async (req: Request, res: Response): Promise<void> => {
    try {
        const uptime = moment.duration(moment().diff(startTime)).humanize();
        res.json({
            status: 'UP',
            version: '1.0.0',
            uptime,
            checks: [
                {
                    name: 'Readiness check',
                    status: 'UP',
                    data: {
                        from: startTime.format(),
                        status: 'READY'
                    }
                },
                {
                    name: 'Liveness check',
                    status: 'UP',
                    data: {
                        from: startTime.format(),
                        status: 'ALIVE'
                    }
                }
            ]
        });
    } catch (error) {
        res.status(500).json({ message: 'Error en el health check', error });
    }
};


export const healthLive = async (req: Request, res: Response): Promise<void> => {
    try {
        res.json({
            status: 'UP',
            version: '1.0.0',
            checks: [
                {
                    name: 'Liveness check',
                    status: 'UP',
                    data: {
                        from: startTime.format(),
                        status: 'ALIVE'
                    }
                }
            ]
        });
    } catch (error) {
        res.status(500).json({ message: 'Error en el health check', error });
    }
}


export const healthReady = async (req: Request, res: Response): Promise<void> => {
    try {
        res.json({
            status: 'UP',
            version: '1.0.0',
            checks: [
                {
                    name: 'Readiness check',
                    status: 'UP',
                    data: {
                        from: startTime.format(),
                        status: 'READY'
                    }
                }
            ]
        });
    } catch (error) {
        res.status(500).json({ message: 'Error en el health check', error });
    }
}