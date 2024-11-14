import os

class Config:
    # Lee las variables de entorno, con valores predeterminados si no están configuradas
    MONGO_URI = os.getenv('MONGO_URI', 'mongodb://localhost:27017/')
    NOTIFICATION_SERVICE_URL = os.getenv('NOTIFICATION_SERVICE_URL', 'http://localhost:5005/api/notifications/send')
    SWAGGER_URL = '/swagger'
    API_URL = '/static/swagger.json'
