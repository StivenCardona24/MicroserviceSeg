const sgMail = require('@sendgrid/mail');

sgMail.setApiKey(process.env.SENDGRID_API_KEY);

async function sendEmail(recipient, message, subject) {
  const msg = {
    from: process.env.EMAIL_USER,
    to: recipient,
    subject: subject,
    text: message,
  };
  sgMail.send(msg)
  .then(() => console.log('Correo enviado'))
  .catch((error) => console.error('Error al enviar el correo:', error));
}

module.exports = { sendEmail };
