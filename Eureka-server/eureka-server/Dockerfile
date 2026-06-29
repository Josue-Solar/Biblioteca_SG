# 1. Indicamos la imagen base (Un Linux en miniatura que ya viene con Java 17)
FROM eclipse-temurin:17-jdk-alpine

# 2. Informamos el puerto que usará este contenedor hacia afuera
EXPOSE 8761

# 3. Copiamos el archivo .jar que acabas de compilar hacia adentro de la caja
COPY target/*.jar app.jar

# 4. El comando que se ejecutará en automático apenas el contenedor se encienda
ENTRYPOINT ["java", "-jar", "/app.jar"]