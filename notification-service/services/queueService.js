const { connectRabbitMQ } = require('../config/rabbitMQConnection')
const { sendEmail } = require('./emailService');
const Notification = require('../models/notificationModel');

const QUEUE_NOTIFICATION = process.env.QUEUE_NOTIFICATION || 'notifications';

async function startQueueListener() {
    try {
        const channel = await connectRabbitMQ();
        await channel.assertQueue(QUEUE_NOTIFICATION);

        console.log('Escuchando notificaciones...');

        channel.consume(QUEUE_NOTIFICATION, async (msg) => {
            const { recipient, channel, message, subject } = JSON.parse(msg.content.toString());
            console.log(`Procesando notificación para ${recipient}`);
            try {
                if (channel === 'email') {
                    await sendEmail(recipient, message, subject);
                }
                await Notification.create({ recipient, channel, message, subject });
                channel.ack(msg);
            } catch (error) {
                console.error('Error enviando notificación:', error);
            }
        });
    }
    catch (error) {
        console.error('Error al iniciar el listener de la cola:', error);
    }
}

module.exports = { startQueueListener };
