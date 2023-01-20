# Stage 1: Build the application
FROM ghcr.io/graalvm/native-image:muslib-ol9-java17-22.3.0 as build

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
ENTRYPOINT ["sh", "-c" ,"java -jar target/pharmacies-on-duty-attica.jar --spring.datasource.url='jdbc:mysql://pharmacies-on-duty-attica-db:3306/pharmacies?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&allowPublicKeyRetrieval=true'"]
