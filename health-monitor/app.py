from flask import Flask, jsonify, request
from apscheduler.schedulers.background import BackgroundScheduler
import requests
import time
import smtplib
from email.mime.text import MIMEText
from email.mime.multipart import MIMEMultipart
import logging
import os
from pymongo import MongoClient

# Configura el logger de Flask para mostrar los logs en la consola
logging.basicConfig(level=logging.DEBUG)

# Conectar a MongoDB
client = MongoClient("mongodb://database-monitor:27017/")  # Ajusta la URI de conexión según sea necesario
db = client["microservicesDB"]  # Base de datos donde almacenar los microservicios
microservices_collection = db["microservices"]  # Colección de microservicios

app = Flask(__name__)

# Función para verificar la salud de un microservicio
def check_health(microservice_name, endpoint, email):
    app.logger.info(f"Verificando salud de {microservice_name} en {endpoint}...") 
    try:
        response = requests.get(endpoint)
        app.logger.info(f"Respuesta: {response.text}")
        status = "UP" if response.status_code == 200 else "DOWN"
    except requests.exceptions.RequestException as e:
        app.logger.info(f"Error de conexión: {e}")
        status = "DOWN"

    # Actualizar el estado en la base de datos
    microservices_collection.update_one(
        {"name": microservice_name},
        {"$set": {"status": status, "last_check": time.strftime("%Y-%m-%d %H:%M:%S")}}
    )

    # Enviar notificación por correo si está caído
    if status == "DOWN":
        send_email_notification(email, microservice_name)

# Función para enviar notificación por correo
# Obtiene la URL del microservicio de notificaciones desde variables de entorno
NOTIFICATION_SERVICE_URL = os.getenv('NOTIFICATION_SERVICE_URL', 'http://notification-service:5005/api/notifications/send')

# Función para enviar notificación a través del microservicio de notificaciones
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
            app.logger.info(f"Notificación enviada exitosamente a {to_email}")
        else:
            app.logger.info(f"Error al enviar la notificación: {response.text}")
    except requests.exceptions.RequestException as e:
        app.logger.error(f"Error al conectar con el servicio de notificación: {e}")

# Ruta para registrar microservicios
@app.route('/register', methods=['POST'])
def register_microservice():
    data = request.get_json()
    microservice_name = data['name']
    endpoint = data['endpoint']
    frequency = data['frequency']
    email = data['email']

    # Guardar en la base de datos
    microservices_collection.insert_one({
        'name': microservice_name,
        'endpoint': endpoint,
        'status': 'UNKNOWN',
        'last_check': None,
        'frequency': frequency,
        'email': email
    })

    # Iniciar el monitoreo en segundo plano
    scheduler.add_job(
        check_health,
        'interval',
        args=[microservice_name, endpoint, email],
        minutes=frequency,
        id=microservice_name
    )

    return jsonify({"message": f"Microservicio {microservice_name} registrado exitosamente."}), 201

# Ruta para obtener la salud de todos los microservicios
@app.route('/health', methods=['GET'])
def health():
    # Obtener todos los microservicios de la base de datos
    health_data = {}
    for microservice in microservices_collection.find():
        health_data[microservice["name"]] = {
            "status": microservice["status"],
            "last_check": microservice["last_check"]
        }
    return jsonify(health_data), 200

# Ruta para obtener la salud de un microservicio específico
@app.route('/health/<microservice_name>', methods=['GET'])
def health_of_microservice(microservice_name):
    # Buscar el microservicio en la base de datos
    microservice = microservices_collection.find_one({"name": microservice_name})
    
    if microservice:
        return jsonify({
            "status": microservice["status"],
            "last_check": microservice["last_check"]
        }), 200
    else:
        return jsonify({"message": "Microservicio no encontrado"}), 404

# Inicializamos el scheduler
scheduler = BackgroundScheduler()
scheduler.start()

if __name__ == '__main__':
    app.run(debug=True, host='0.0.0.0', port=5000)
