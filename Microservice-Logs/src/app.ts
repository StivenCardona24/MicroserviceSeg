import express from 'express';
import bodyParser from 'body-parser';
import logRoutes from './routes/logRoutes';
import connectDB from './config/db';

// Crear la aplicación Express
const app = express();

// Middlewares
app.use(bodyParser.json());

// Conectar a la base de datos
connectDB();

// Usar rutas
app.use('/api', logRoutes);

export default app;
