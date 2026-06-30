# 1. Misma imagen base de Java 17 (o la que uses)
FROM eclipse-temurin:17-jdk-alpine

# 2. AQUÍ ESTÁ EL CAMBIO: El microservicio Persona expone el puerto 8085
EXPOSE 8085

# 3. Copiamos el .jar generado por Maven usando el comodín
COPY target/*.jar app.jar

# 4. Comando para arrancar el servicio de Persona
ENTRYPOINT ["java", "-jar", "/app.jar"]