# Use a base image with Java installed
FROM eclipse-temurin:17-jre-alpine

# Expose the port your application runs on
EXPOSE 2346

# Create a directory in the container to store persistent data
RUN mkdir -p /app/data

# Define a volume to persist application data (e.g., database, logs, etc.)
VOLUME /app/data

# Optionally, define another volume for log storage
VOLUME /app/logs

# Copy the JAR file into the container
ADD target/appointment-service-0.0.1-SNAPSHOT.jar appointment-service-0.0.1-SNAPSHOT.jar

# Command to run the application
ENTRYPOINT ["java", "-jar", "appointment-service-0.0.1-SNAPSHOT.jar"]
