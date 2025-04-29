# Build stage
FROM eclipse-temurin:21-jdk-jammy as builder

# Install Node.js and npm in a single RUN command
RUN apt-get update && apt-get install -y curl maven \
    && curl -fsSL https://deb.nodesource.com/setup_18.x | bash - \
    && apt-get install -y nodejs \
    && apt-get clean \
    && rm -rf /var/lib/apt/lists/*

# Set working directory
WORKDIR /app

# Copy pom.xml separately to leverage Docker cache
COPY pom.xml .
# Download dependencies
RUN mvn dependency:go-offline

# Copy the rest of the application
COPY . .

# Build the application
RUN mvn clean package -Pproduction -DskipTests

# Final stage - use JRE instead of JDK
FROM eclipse-temurin:21-jre-jammy

# Set working directory
WORKDIR /app

# Copy the built application
COPY --from=builder /app/target/*.jar app.jar

# Expose port
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "app.jar"]