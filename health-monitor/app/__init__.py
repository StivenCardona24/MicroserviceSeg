from flask import Flask
from flask_restful import Api
from apscheduler.schedulers.background import BackgroundScheduler
from .routes import initialize_routes
from .config import Config
from flasgger import Swagger
from flask_swagger_ui import get_swaggerui_blueprint

# Crear la instancia de la app
def create_app():
    app = Flask(__name__)
    app.config.from_object(Config)


        # Swagger URL y archivo Swagger
    SWAGGER_URL = '/swagger'
    API_URL = '/static/swagger.json'  # Suponiendo que el swagger.json está en la carpeta estática

    # Crear el blueprint de Swagger
    swaggerui_blueprint = get_swaggerui_blueprint(
        SWAGGER_URL,  # Ruta donde estará disponible Swagger
        API_URL,      # Ruta del archivo swagger.json
        config={     # Opcional: configuración adicional
            'app_name': "Microservice Health Monitoring API"
        }
    )

    # Registramos el blueprint de Swagger en la app
    app.register_blueprint(swaggerui_blueprint, url_prefix=SWAGGER_URL)

    # Inicializar el API y las rutas
    api = Api(app)
    
    # Inicializar las rutas
    initialize_routes(api)

    return app
