const { connectRabbitMQ } = require('../config/rabbitMQConnection')
const { sendEmail } = require('./emailService');
const Notification = require('../models/notificationModel');

const QUEUE_NOTIFICATION = process.env.QUEUE_NOTIFICATION || 'notificationQueue';

async function startQueueListener() {
    try {
        const channelRabbit = await connectRabbitMQ();
        console.log("🚀 ~ startQueueListener ~ channel:", channelRabbit)
        await channelRabbit.assertQueue(QUEUE_NOTIFICATION);

        console.log('Escuchando notificaciones...');

        channelRabbit.consume(
            QUEUE_NOTIFICATION,
            async (msg) => {
                if (!msg) {
                    return;
                }
                const { recipient, channel, message, subject } = JSON.parse(msg.content.toString());
                console.log(`Procesando notificación para ${recipient}`);
                try {
                    if (channel === 'email') {
                        await sendEmail(recipient, message, subject);
                    }
                    await Notification.create({ recipient, channel, message, subject });
                    channelRabbit.ack(msg);
                } catch (error) {
                    console.error('Error enviando notificación:', error);
                }
            },
            { noAck: false }
        );
    }
    catch (error) {
        console.error('Error al iniciar el listener de la cola:', error);
    }
}

module.exports = { startQueueListener };
