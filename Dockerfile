# ETAPA 1: CONSTRUCCIÓN
# Usamos una imagen de Maven más moderna y estable
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# ETAPA 2: EJECUCIÓN
# Cambiamos openjdk por eclipse-temurin (la versión oficial recomendada hoy)
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]