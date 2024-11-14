const express = require('express');
const router = express.Router();
const { registerNotification, getNotifications, healthCheck } = require('../controllers/notificationController');

router.post('/send', registerNotification);
router.get('/', getNotifications);
router.get('/health', healthCheck);

module.exports = router;
