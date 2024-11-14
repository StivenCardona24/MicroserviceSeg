import requests
import os
import logging

NOTIFICATION_SERVICE_URL = os.getenv('NOTIFICATION_SERVICE_URL', 'http://notification-service:5005/api/notifications/send')

def send_email_notification(to_email, microservice_name):
    payload = {
        "recipient": to_email,
        "subject": f"Microservicio {microservice_name} está caído",
        "message": f"El microservicio {microservice_name} ha sido reportado como DOWN.",
        "channel": "email"
    }

    try:
        response = requests.post(NOTIFICATION_SERVICE_URL, json=payload)
        if response.status_code == 201:
            logging.info(f"Notificación enviada exitosamente a {to_email}")
        else:
            logging.info(f"Error al enviar la notificación: {response.text}")
    except requests.exceptions.RequestException as e:
        logging.error(f"Error al conectar con el servicio de notificación: {e}")
