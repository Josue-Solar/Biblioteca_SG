# 1. Misma imagen base de Java 17
FROM eclipse-temurin:17-jdk-alpine

# 2. AQUÍ ESTÁ EL CAMBIO: El microservicio Comuna expone el puerto 8088
EXPOSE 8088

# 3. Copiamos el .jar generado por Maven usando el comodín
COPY target/*.jar app.jar

# 4. Comando para arrancar el servicio de Comuna
ENTRYPOINT ["java", "-jar", "/app.jar"]