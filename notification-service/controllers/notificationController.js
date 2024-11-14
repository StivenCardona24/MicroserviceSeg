const Notification = require('../models/notificationModel');
const amqp = require('amqplib');
const { sendEmail } = require('../services/emailService');

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

module.exports = { registerNotification, getNotifications };
