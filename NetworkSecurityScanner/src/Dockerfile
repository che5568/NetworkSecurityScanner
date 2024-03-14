# Use a base image with the necessary Java environment
FROM openjdk:latest

# Set the working directory in the container
WORKDIR /app

# Copy the application JAR file into the container
COPY NetworkScanner.jar /app

# Define the command to run your Java application
CMD ["java", "-jar", "NetworkScanner.jar"]

