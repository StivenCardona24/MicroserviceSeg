# Usamos una imagen base de Node.js
FROM node:16

# Establecemos el directorio de trabajo dentro del contenedor
WORKDIR /usr/src/app

# Copiamos los archivos package.json y package-lock.json (si existe) al contenedor
COPY package*.json ./

# Instalamos las dependencias del proyecto
RUN npm install

# Copiamos el resto de los archivos del proyecto al contenedor
COPY . .


# Exponemos el puerto en el que estará escuchando el microservicio
EXPOSE 5000

# Comando para ejecutar el microservicio
CMD ["npm", "run", "dev"]
