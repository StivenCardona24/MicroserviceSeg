const mongoose = require('mongoose');

const notificationSchema = new mongoose.Schema({
  recipient: String,
  channel: String,
  message: String,
  subject: String,
  status: { type: String, default: 'PENDING' },
  createdAt: { type: Date, default: Date.now },
});

module.exports = mongoose.model('Notification', notificationSchema);
