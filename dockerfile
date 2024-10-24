FROM maven:3.8.6-openjdk-11 AS build

WORKDIR /app

COPY . .

RUN mvn clean package

FROM openjdk:11-jre-slim

WORKDIR /app

COPY --from=build /app/target/carr-0.0.1-SNAPSHOT.jar app.jar

COPY src/main/resources/application-prod.properties /app/application-prod.properties

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]