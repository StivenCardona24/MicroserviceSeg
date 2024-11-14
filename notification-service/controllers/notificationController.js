const amqp = require('amqplib');
const moment = require('moment');

const Notification = require('../models/notificationModel');

const { sendEmail } = require('../services/emailService');


const startTime = moment();


async function registerNotification(req, res) {
  const { recipient, channel, message, subject } = req.body;
  await sendEmail(recipient, message, subject);
  const notification = new Notification({ recipient, channel, message, subject });
  await notification.save();

  res.status(201).send('Notificación enviada y guardada ');

}

async function getNotifications(req, res) {
  const notifications = await Notification.find();
  res.json(notifications);
}


async function healthCheck(req, res) {
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

module.exports = { registerNotification, getNotifications, healthCheck };
