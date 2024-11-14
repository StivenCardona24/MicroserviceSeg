# Usamos una imagen base de Python
FROM python:3.9-slim

# Establecemos el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiamos los archivos del proyecto al contenedor
COPY . /app

# Instalamos las dependencias del proyecto
RUN pip install --no-cache-dir -r requirements.txt

# Establecemos las variables de entorno necesarias para el funcionamiento de Flask
# (Estas pueden ser sobreescritas por docker-compose, pero definimos valores predeterminados)
ENV FLASK_APP=run.py
ENV FLASK_ENV=development
ENV MONGO_URI=${MONGO_URI}
ENV NOTIFICATION_SERVICE_URL=${NOTIFICATION_SERVICE_URL}

# Exponemos el puerto en el que estará escuchando el microservicio
EXPOSE 5000

# Comando para ejecutar el microservicio
CMD ["python", "run.py"]
