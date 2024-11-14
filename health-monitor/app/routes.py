from apscheduler.schedulers.background import BackgroundScheduler
from flask import jsonify, request
from flask_restful import Api, Resource
from .models import microservices_collection
from .health import check_health
from .notification import send_email_notification

# Crea una instancia del BackgroundScheduler
scheduler = BackgroundScheduler()

# Clase para manejar los microservicios (CRUD)
class MicroserviceResource(Resource):
    # Obtener todos los microservicios
    def get(self):
        health_data = {}
        for microservice in microservices_collection.find():
            health_data[microservice["name"]] = {
                "status": microservice["status"],
                "last_check": microservice["last_check"]
            }
        return jsonify(health_data)

    # Registrar un microservicio (POST)
    def post(self):
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

        return jsonify({"message": f"Microservicio {microservice_name} registrado exitosamente."})

class MicroserviceDetailResource(Resource):
    # Obtener la salud de un microservicio específico
    def get(self, microservice_name):
        microservice = microservices_collection.find_one({"name": microservice_name})
        
        if microservice:
            return jsonify({
                "status": microservice["status"],
                "last_check": microservice["last_check"]
            })
        else:
            return jsonify({"message": "Microservicio no encontrado"})

    # Actualizar los detalles de un microservicio
    def put(self, microservice_name):
        data = request.get_json()
        microservice = microservices_collection.find_one({"name": microservice_name})

        if microservice:
            microservices_collection.update_one(
                {"name": microservice_name},
                {"$set": data}
            )
            return jsonify({"message": "Microservicio actualizado exitosamente."})
        else:
            return jsonify({"message": "Microservicio no encontrado"})

    # Eliminar un microservicio
    def delete(self, microservice_name):
        microservice = microservices_collection.find_one({"name": microservice_name})

        if microservice:
            microservices_collection.delete_one({"name": microservice_name})
            return jsonify({"message": "Microservicio eliminado exitosamente."})
        else:
            return jsonify({"message": "Microservicio no encontrado"}), 404

def initialize_routes(api):
    api.add_resource(MicroserviceResource, '/microservices')
    api.add_resource(MicroserviceDetailResource, '/microservices/<string:microservice_name>')

# Inicia el scheduler al iniciar la aplicación
scheduler.start()
