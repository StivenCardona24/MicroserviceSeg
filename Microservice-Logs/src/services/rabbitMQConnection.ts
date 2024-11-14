const amqp = require('amqplib');

const RABBITMQ_URI = process.env.RABBITMQ_URI || 'amqp://rabbitmq:rabbitmq@rabbitmq:5672'; 

let connection: any
let channel: any

export const connectRabbitMQ = async (): Promise<any> => {
  if (!channel) {
    try {
      connection = await amqp.connect(RABBITMQ_URI);
      channel = await connection.createChannel();
      console.log('Conectado a RabbitMQ');
    } catch (error) {
      console.error('Error al conectar con RabbitMQ:', error);
      process.exit(1); // Salir si no se puede conectar
    }
  }
  return channel;
};
