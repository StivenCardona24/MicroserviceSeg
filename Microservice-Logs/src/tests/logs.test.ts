import request from 'supertest';
import mongoose from 'mongoose';
import app from '../app'; // Importamos nuestra aplicación Express
import { Log } from '../models/logModel'; // Importamos el modelo de Log

// Configurar las pruebas

// Configurar las pruebas
beforeAll(async () => {
  // Si hay una conexión activa, cerrarla antes de crear una nueva
  if (mongoose.connection.readyState !== 0) {
    await mongoose.disconnect();
  }
  // Conectar a la base de datos de prueba
  await mongoose.connect('mongodb://localhost:27017/logServiceDBTest', {});
});

afterAll(async () => {
  await mongoose.connection.close(); // Cerramos la conexión a MongoDB después de las pruebas
});

afterEach(async () => {
  await Log.deleteMany(); // Limpia la colección después de cada prueba
});

describe('API de Logs', () => {
  // Prueba de creación de log
  it('debería crear un log correctamente', async () => {
    const newLog = {
      application: 'TestApp',
      logType: 'ERROR',
      module: 'TestModule',
      summary: 'Test error',
      description: 'This is a test error for logging system',
    };

    const response = await request(app).post('/api/logs').send(newLog);

    expect(response.status).toBe(201); // Verificar que el status sea 201 (creado)
    expect(response.body).toHaveProperty('_id'); // Verificar que se haya creado un ID
    expect(response.body.application).toBe(newLog.application); // Verificar los valores
    expect(response.body.logType).toBe(newLog.logType);
  });

  // Prueba de obtención de logs con paginación
  it('debería obtener logs con paginación', async () => {
    // Crear algunos logs de ejemplo
    const logs = [
      { application: 'App1', logType: 'INFO', module: 'Module1', summary: 'Info log 1', description: 'First log' },
      { application: 'App2', logType: 'ERROR', module: 'Module2', summary: 'Error log 2', description: 'Second log' },
      { application: 'App1', logType: 'DEBUG', module: 'Module1', summary: 'Debug log 3', description: 'Third log' },
    ];

    await Log.insertMany(logs); // Insertar varios logs

    const response = await request(app).get('/api/logs?page=1&limit=2'); // Obtener la primera página de 2 logs

    expect(response.status).toBe(200); // Verificar que el status sea 200
    expect(response.body.logs.length).toBe(2); // Verificar que se obtuvieron 2 logs
    expect(response.body.totalPages).toBe(2); // Verificar que hay 2 páginas
    expect(response.body.currentPage).toBe(1); // Verificar que estamos en la página 1
  });

  // Prueba de obtención de logs con filtros
  it('debería filtrar logs por aplicación y tipo de log', async () => {
    // Crear algunos logs de ejemplo
    const logs = [
      { application: 'App1', logType: 'INFO', module: 'Module1', summary: 'Info log 1', description: 'First log' },
      { application: 'App1', logType: 'ERROR', module: 'Module2', summary: 'Error log 2', description: 'Second log' },
      { application: 'App2', logType: 'DEBUG', module: 'Module3', summary: 'Debug log 3', description: 'Third log' },
    ];

    await Log.insertMany(logs); // Insertar varios logs

    const response = await request(app).get('/api/logs?application=App1&logType=ERROR'); // Filtrar por aplicación y tipo

    expect(response.status).toBe(200); // Verificar que el status sea 200
    expect(response.body.logs.length).toBe(1); // Verificar que solo un log coincide con los filtros
    expect(response.body.logs[0].application).toBe('App1'); // Verificar los valores del log filtrado
    expect(response.body.logs[0].logType).toBe('ERROR');
  });
});
