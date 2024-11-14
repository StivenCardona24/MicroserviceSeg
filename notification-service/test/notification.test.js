const request = require('supertest');
const app = require('../app'); // Asegúrate de que apunte al archivo correcto

describe('Notification Microservice', () => {
  it('should send a notification email', async () => {
    const response = await request(app)
      .post('api/notifications/send')
      .send({
        recipient: 'user@example.com',
        subject: 'Test Notification',
        message: 'This is a test notification',
        channel: 'email'
      });
    expect(response.statusCode).toBe(200);
    expect(response.body).toHaveProperty('message', 'Notification sent successfully');
  });
});
