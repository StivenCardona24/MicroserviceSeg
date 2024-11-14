import express from 'express';
import cors from 'cors';
import bodyParser from 'body-parser';
import profileRoutes from './routes/profileRoutes';
import healthRoutes from './routes/healthRoutes';
import connectDB from './database';

const app = express();

// Conectar a la base de datos
connectDB();

// Middlewares
app.use(cors());
app.use(bodyParser.json());

// Rutas
app.use('/api/profile', profileRoutes);
app.use('/api', healthRoutes);

export default app;
