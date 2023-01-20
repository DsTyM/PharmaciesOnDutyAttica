# Stage 1: Build the application
FROM eclipse-temurin:17.0.5_8-jdk-focal as build
# https://hub.docker.com/_/eclipse-temurin

# Set the current working directory inside the image
WORKDIR /app

# Copy maven executable to the image
COPY mvnw .
COPY .mvn .mvn

# Copy the pom.xml file
COPY pom.xml .

# Give the required rights to mvnw
RUN chmod +x ./mvnw

# Copy the project source
COPY src src

# Package the application
RUN ./mvnw clean package -DskipTests
ENTRYPOINT ["sh", "-c" ,"java -jar target/pharmacies-on-duty-attica-1.0.0.jar --spring.datasource.url='jdbc:mysql://pharmacies-on-duty-attica-db:3306/pharmacies?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&allowPublicKeyRetrieval=true'"]
