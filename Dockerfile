# Stage 1: Build the application
FROM eclipse-temurin:24.0.2_12-jdk as build
# https://hub.docker.com/_/eclipse-temurin

# Set the current working directory for build
WORKDIR /app

# Copy maven executable to the image
COPY mvnw .
COPY .mvn .mvn

# Copy the pom.xml file
COPY pom.xml .

# Give the required rights to mvnw
RUN chmod +x ./mvnw

# Pre-download the dependencies for caching
RUN ./mvnw dependency:resolve dependency:resolve-plugins

# Copy the project source
COPY src src

# Package the application
RUN ./mvnw clean package -DskipTests


# Stage 2: Run the application
FROM eclipse-temurin:24.0.2_12-jre

# Set the current working directory for run
WORKDIR /app

# Copy jar from build stage
COPY --from=build /app/target/pharmacies-on-duty-attica-1.0.0.jar app.jar

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
