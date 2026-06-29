# 1. Usamos la misma imagen base con Java 17
FROM eclipse-temurin:17-jdk-alpine

# 2. El Gateway usa el puerto 8099 (según configuramos en tu application.yml)
EXPOSE 8099

# 3. Copiamos el archivo .jar que acabamos de generar usando el comodín (*)
COPY target/*.jar app.jar

# 4. Comando para arrancar el Gateway automáticamente
ENTRYPOINT ["java", "-jar", "/app.jar"]