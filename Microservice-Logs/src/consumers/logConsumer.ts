// src/consumers/logConsumer.ts
import { connectRabbitMQ } from '../services/rabbitMQConnection';
import { Log } from '../models/logModel';

const QUEUE_LOG = process.env.QUEUE_LOG;

export const startLogConsumer = async () => {

    try {
    const channel = await connectRabbitMQ();

    await channel.assertQueue(QUEUE_LOG, { durable: true });
    console.log(`Escuchando en la cola: ${QUEUE_LOG}`);

    channel.consume(
        QUEUE_LOG,
        async (msg: any) => {
            if (msg !== null) {
                const { application, logType, module, summary, description } = JSON.parse(msg.content.toString());
                const newLog = new Log({
                    application,
                    logType,
                    module,
                    summary,
                    description,
                });

                await newLog.save();
                channel.ack(msg);
            }
        },
        { noAck: false }
    ).catch((error: any) => console.error('Error al consumir el mensaje:', error));
    
    } catch (error) {
    console.error('Error al iniciar el consumidor de logs:', error);

    }
};
