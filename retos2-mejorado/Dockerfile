# Fase de Construcción (Build Stage)
# Utilizar una imagen de Maven para compilar la aplicación
FROM maven:3.8.6-eclipse-temurin-17 AS build
LABEL authors="andresvalencia"
WORKDIR /app

# Copiar el archivo pom.xml y descargar las dependencias de Maven
COPY pom.xml ./
RUN mvn dependency:go-offline -B

# Copiar el código fuente y compilar la aplicación
COPY src ./src
RUN mvn package -DskipTests
RUN ls /app/target


# Fase de ejecución
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

# Copiar el archivo JAR generado en la fase de compilación
COPY --from=build /app/target/*.jar /app/api-rest.jar

# Exponer el puerto 80 para que la aplicación esté disponible en el contenedor
EXPOSE 80

# Definir el comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "/app/api-rest.jar"]