const express = require('express');
const router = express.Router();
const { registerNotification, getNotifications } = require('../controllers/notificationController');

router.post('/send', registerNotification);
router.get('/', getNotifications);

module.exports = router;
