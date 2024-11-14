import requests
import time
from .models import microservices_collection
from .notification import send_email_notification

def check_health(microservice_name, endpoint, email):
    try:
        response = requests.get(endpoint)
        status = "UP" if response.status_code == 200 else "DOWN"
    except requests.exceptions.RequestException as e:
        status = "DOWN"

    # Actualizar el estado en la base de datos
    microservices_collection.update_one(
        {"name": microservice_name},
        {"$set": {"status": status, "last_check": time.strftime("%Y-%m-%d %H:%M:%S")}}
    )

    # Enviar notificación si está caído
    if status == "DOWN":
        send_email_notification(email, microservice_name)
