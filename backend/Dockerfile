# Use a base image with Java 21 and Node.js pre-installed
FROM eclipse-temurin:21-jdk-jammy as builder

# Install Node.js, NPM, and Maven
RUN apt-get update && apt-get install -y curl maven
RUN curl -fsSL https://deb.nodesource.com/setup_18.x | bash -
RUN apt-get install -y nodejs

# Set the working directory
WORKDIR /app

# Copy the project files
COPY . .

# Build the application (including Vaadin frontend)
RUN mvn clean package -Pproduction -DskipTests

# Use a smaller runtime image with Java 21
FROM eclipse-temurin:21-jre-jammy

# Set the working directory
WORKDIR /app

# Copy the built application from the builder stage
COPY --from=builder /app/target/*.jar app.jar

# Expose the port your app runs on
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "app.jar"]