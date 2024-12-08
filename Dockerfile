# Use a base image with Java installed
FROM eclipse-temurin:17-jre-alpine

# Expose the port your application runs on
EXPOSE 8080


# Copy the JAR file into the container
ADD target/appointment-service-0.0.1-SNAPSHOT.jar appointment-service-0.0.1-SNAPSHOT.jar


# Command to run the application
ENTRYPOINT ["java", "-jar", "appointment-service-0.0.1-SNAPSHOT.jar"]
