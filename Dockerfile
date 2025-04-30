# Usar la imagen oficial de Maven para construir la aplicación
FROM maven:3.9.4-eclipse-temurin-17 AS builder

# Establecer el directorio de trabajo
WORKDIR /app

# Copiar el archivo pom.xml y descargar las dependencias de Maven
COPY pom.xml .

# Descargar dependencias para trabajar offline
RUN mvn dependency:go-offline

# Copiar el código fuente al contenedor
COPY src ./src

# Compilar la aplicación y generar el archivo JAR (sin ejecutar los tests)
RUN mvn clean package -DskipTests

# Usar una imagen más ligera para ejecutar la aplicación
FROM openjdk:17-jdk-slim

# Establecer el directorio de trabajo
WORKDIR /app

# Copiar el archivo JAR generado desde la etapa de construcción
COPY --from=builder /app/target/FocusFrame-1.0.0.jar /app/FocusFrame-1.0.0.jar

# Exponer el puerto en el que la aplicación estará corriendo
EXPOSE 8080

# Establecer el comando de inicio para ejecutar el JAR
ENTRYPOINT ["java", "-jar", "FocusFrame-1.0.0.jar"]
