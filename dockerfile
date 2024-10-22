FROM openjdk:11-jre-slim

WORKDIR /app

#copia el archivo JAR
COPY target/carr-0.0.1-SNAPSHOT.jar app.jar

#Expone el puerto de la aplicacion
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app.jar"]
