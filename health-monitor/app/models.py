from pymongo import MongoClient

# Conectar a MongoDB
client = MongoClient("mongodb://database-monitor:27017/")  # Ajusta la URI de conexión según sea necesario
db = client["microservicesDB"]  # Base de datos donde almacenar los microservicios
microservices_collection = db["microservices"]  # Colección de microservicios
