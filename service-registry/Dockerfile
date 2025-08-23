# Etapa de build
FROM maven:3.8-openjdk-17 AS build

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src /app/src
RUN mvn clean package -DskipTests

# Etapa de execução
FROM openjdk:17-slim

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

# Porta exposta (ajustar por serviço)
EXPOSE 8761

CMD ["java", "-jar", "app.jar"]
