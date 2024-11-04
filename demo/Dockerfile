#Descargar imagen desde Docker Hub
FROM openjdk:21

#Pasar .jar de disco a la imagen
ARG JAR_FILE=target/demo.jar
COPY ${JAR_FILE} tingeso-backend.jar

EXPOSE 8080
#Pasar instruccion para correr el ejecutable, lo mismo que cualquier .jar
ENTRYPOINT ["java", "-jar", "/tingeso-backend.jar"]