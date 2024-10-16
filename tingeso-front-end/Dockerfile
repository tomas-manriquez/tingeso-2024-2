#Descargar la imagen de nginx
FROM nginx:stable-alpine

#Buscar ejecutable de produccion en el disco duro local
COPY dist usr/share/nginx/html

#Exponer puerto 80
EXPOSE 80

#Pasar comando para correr el ejecutable
CMD ["nginx", "-g", "daemon off;"]
