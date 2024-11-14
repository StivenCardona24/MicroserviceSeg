const amqp = require('amqplib');

const RABBITMQ_URI = process.env.RABBITMQ_URI || 'amqp://rabbitmq:rabbitmq@rabbitmq:5672'; 

let connection
let channel

const connectRabbitMQ = async () => {
  console.log("🚀 ~ connectRabbitMQ ~ RABBITMQ_URI:", RABBITMQ_URI)
  if (!channel) {
    try {
      connection = await amqp.connect(RABBITMQ_URI);
      console.log("🚀 ~ connectRabbitMQ ~ RABBITMQ_URI:", RABBITMQ_URI)
      channel = await connection.createChannel();
      console.log('Conectado a RabbitMQ');
    } catch (error) {
      console.error('Error al conectar con RabbitMQ:', error);
      process.exit(1); // Salir si no se puede conectar
    }
  }
  return channel;
};

module.exports = { connectRabbitMQ };