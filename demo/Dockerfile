#Descargar imagen desde Docker Hub
FROM openjdfk:17

#Pasar .jar de disco a la imagen
ARG JAR_FILE=target/demo.jar
COPY ${JAR_FILE} demo.jar

#Pasar instruccion para correr el ejecutable, lo mismo que cualquier .jar
ENTRYPOINT ["java", "-jar", "/demo.jar"]
# TODO demo.jar (ejecutable de produccion)