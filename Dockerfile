# Stage 1: Build the application
FROM eclipse-temurin:17 as build
# https://hub.docker.com/_/eclipse-temurin

# Set the current working directory inside the image
WORKDIR /app

# Copy maven executable to the image
COPY mvnw .
COPY .mvn .mvn

# Copy the pom.xml file
COPY pom.xml .

# Build all the dependencies in preparation to go offline.
# This is a separate step so the dependencies will be cached unless the pom.xml file has changed.
RUN chmod +x ./mvnw
RUN ./mvnw dependency:go-offline -B

# Copy the project source
COPY src src

# Package the application
RUN ./mvnw package -DskipTests
ENTRYPOINT ["sh", "-c" ,"java -jar target/pharmacies-on-duty-attica.jar --spring.datasource.url='jdbc:mysql://pharmacies-on-duty-attica-db:3306/pharmacies?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&allowPublicKeyRetrieval=true'"]
