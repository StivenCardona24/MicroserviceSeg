from flask import Flask, jsonify, request
from apscheduler.schedulers.background import BackgroundScheduler
import requests
import time
import smtplib
from email.mime.text import MIMEText
from email.mime.multipart import MIMEMultipart

import logging

# Configura el logger de Flask para mostrar los logs en la consola
logging.basicConfig(level=logging.DEBUG)




app = Flask(__name__)

# Almacén para los microservicios registrados
microservices = {}

# Función para verificar la salud de un microservicio
def check_health(microservice_name, endpoint, email):
    app.logger.info(f"Verificando salud de {microservice_name} en {endpoint}...") 
    try:
        response = requests.get(endpoint)
        app.logger.info(f"Respuesta: {response.text}")
        app.logger.info(response)
        app.logger.info(f"Microservicio {microservice_name} - Status code: {response.status_code}")
        status = "UP" if response.status_code == 200 else "DOWN"
    except requests.exceptions.RequestException as e:
        app.logger.info(f"Microservicio {microservice_name} - Error de conexión: {e}")
        status = "DOWN"

    microservices[microservice_name]['status'] = status
    microservices[microservice_name]['last_check'] = time.strftime("%Y-%m-%d %H:%M:%S")

    # Enviar notificación por correo si está caído
    if status == "DOWN":
        send_email_notification(email, microservice_name)

# Función para enviar notificación por correo
def send_email_notification(to_email, microservice_name):
    from_email = "your_email@example.com"  # Cambiar por el correo del remitente
    password = "your_email_password"  # Cambiar por la contraseña del correo

    subject = f"Microservicio {microservice_name} está caído"
    body = f"El microservicio {microservice_name} ha sido reportado como DOWN."

    msg = MIMEMultipart()
    msg['From'] = from_email
    msg['To'] = to_email
    msg['Subject'] = subject
    msg.attach(MIMEText(body, 'plain'))

    try:
        server = smtplib.SMTP('smtp.gmail.com', 587)  # Usar SMTP adecuado
        server.starttls()
        server.login(from_email, password)
        text = msg.as_string()
        server.sendmail(from_email, to_email, text)
        server.quit()
    except Exception as e:
        app.logger.info(f"Error enviando el correo: {e}")

# Ruta para registrar microservicios
@app.route('/register', methods=['POST'])
def register_microservice():
    data = request.get_json()
    microservice_name = data['name']
    endpoint = data['endpoint']
    frequency = data['frequency']
    email = data['email']

    microservices[microservice_name] = {
        'endpoint': endpoint,
        'status': 'UNKNOWN',
        'last_check': None,
        'frequency': frequency,
        'email': email
    }

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
    health_data = {name: {"status": data["status"], "last_check": data["last_check"]} 
                   for name, data in microservices.items()}
    return jsonify(health_data), 200

# Ruta para obtener la salud de un microservicio específico
@app.route('/health/<microservice_name>', methods=['GET'])
def health_of_microservice(microservice_name):
    if microservice_name in microservices:
        data = microservices[microservice_name]
        return jsonify({
            "status": data["status"],
            "last_check": data["last_check"]
        }), 200
    else:
        return jsonify({"message": "Microservicio no encontrado"}), 404

# Inicializamos el scheduler
scheduler = BackgroundScheduler()
scheduler.start()

if __name__ == '__main__':
    app.run(debug=True, host='0.0.0.0', port=5000)
