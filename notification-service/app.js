const express = require('express');
const {connectDB} = require('./config/db');
const notificationRoutes = require('./routes/notificationRoutes');
const { startQueueListener } = require('./services/queueService');

const app = express();
app.use(express.json());

// Conectar a la base de datos
connectDB();

app.use('/api/notifications', notificationRoutes);

// Iniciar la cola para procesamiento asíncrono
startQueueListener();

const PORT = process.env.PORT || 5010;
console.log("🚀 ~ PORT:", PORT)
app.listen(PORT, () => {
  console.log(`Notification service running on port ${PORT}`);
});
