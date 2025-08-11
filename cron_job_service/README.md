# INeedHousing Cron Job Service

A lightweight Quarkus-based microservice designed to handle scheduled maintenance tasks for the INeedHousing platform, including Azure resource cleanup and database maintenance operations.

## 🚀 Features

### Scheduled Tasks

- **Container Registry Cleanup**
    - Automated deletion of old container images from Azure Container Registry
    - Configurable retention policies for image versions
    - Support for repository-specific cleanup rules
    - REST API endpoints for manual cleanup operations

- **Database Maintenance**
    - Automated cleanup of old listing data from Azure PostgreSQL
    - Expired listing removal based on configurable time thresholds
    - Database optimization and maintenance tasks
    - Health check endpoints for monitoring

### Real-time Monitoring

- **WebSocket Integration**
    - Live log streaming to INeedHousing admin dashboard
    - Real-time task execution status updates
    - Bidirectional communication for task management
    - WebSocket endpoints for real-time monitoring

### Health & Monitoring

- **Health Checks**
    - Readiness and liveness probes for container orchestration
    - Service status monitoring endpoints
    - Ping endpoint for basic connectivity testing

## 🛠️ Technical Stack

- **Framework**: Quarkus 3.22.3
- **Language**: Java 21
- **Cloud Services**: Azure Container Registry, Azure PostgreSQL
- **Build Tool**: Maven
- **Containerization**: Docker with native and JVM modes
- **Deployment**: Azure App Service via GitHub Actions

## 📦 Dependencies

Key dependencies include:

- **Quarkus Core**: Arc (CDI), REST, WebSockets Next, Scheduler
- **Database**: Postgres JDBC driver with connection pooling
- **Azure SDK**: Azure Container Registry management
- **Testing**: JUnit 5, REST Assured
- **Health**: SmallRye Health for monitoring
- **Performance**: Virtual Threads for improved scalability

## 🏗️ Project Structure

```txt
src/main/java/ineedhousing/cronjob/
├── azure/                    # Azure service integrations
│   ├── container_registry/   # Container registry management
│   │   ├── model/           # Data models for container operations
│   │   ├── ContainerRegistryResource.java      # REST endpoints
│   │   ├── ContainerRegistryRestService.java   # Business logic
│   │   ├── ContainerRegistryAuthHeaderUtil.java # Authentication utilities
│   │   └── ContainerRegisterRestClient.java    # REST client
│   └── postgres/            # PostgreSQL database operations
├── cron/                    # Cron job definitions and management
│   └── CronService.java     # Scheduled task orchestration
├── exception/               # Custom exception handling
├── filters/                 # Request filtering and validation
│   └── GlobalRequestFilter.java # Global request filter for Access-Token
├── log/                     # Logging and monitoring services
├── ws/                      # WebSocket implementations
└── PingResource.java        # Health check and ping endpoint
```

## ⚙️ Configuration

The application uses environment variables for configuration:

### Azure Configuration

- Container registry connection settings
- PostgreSQL database connection parameters
- Authentication credentials and endpoints

### WebSocket Configuration

- Admin dashboard connection settings
- Real-time logging stream configuration
- Connection limits and security settings

## 🚀 Getting Started

1. **Prerequisites**
    - Java 21
    - Maven
    - Docker (optional)
    - Azure account with Container Registry and PostgreSQL

2. **Local Development**

   ```bash
   # Clone the repository
   git clone [repository-url]
   
   # Navigate to service directory
   cd cron_job_service
   
   # Set up environment variables
   # Configure Azure credentials and database connections
   
   # Run in development mode
   ./mvnw compile quarkus:dev
   ```

3. **Docker Deployment**

   ```bash
   # Build the application
   ./mvnw package
   
   # Build Docker image
   docker build -f src/main/docker/Dockerfile.jvm -t ineedhousing/cron-service .
   
   # Run container
   docker run -p 8080:8080 --env-file .env ineedhousing/cron-service
   ```

4. **Native Build** (Optional)

   ```bash
   # Build native executable
   ./mvnw package -Dnative
   
   # Build native Docker image
   docker build -f src/main/docker/Dockerfile.native -t ineedhousing/cron-service:native .
   ```

5. **Production Deployment**

   The service is automatically deployed to Azure App Service via GitHub Actions when changes are pushed to the `production` branch.

## 📝 API Endpoints

### Health & Status

- `GET /ping` - Basic connectivity test

### Container Registry Management

- `GET /container-registries/repos` - List all repos in a given Azure Container Registry
- `GET /container-registries/tags` - List all tags of a given Repository
- `GET /container-registries/manifests` - Get manifest of a given tag
- `POST /container-registries/manifests` - Bulk fetch manifests of all given tags
- `DELETE /container-registries/manifests` - Delete manifest
- `DELETE /container-registries/manifests/bulk` - Bulk delete manifests

### WebSocket Integration

- `WS /live-logs` - Real-time log streaming

#### Image Cleanup Task

**Purpose**: Remove old container images from Azure Container Registry
**Implementation**: Scheduled cleanup based on configurable retention policies
**Status**: ✅ Implemented and tested

#### Listing Cleanup Task

**Purpose**: Remove expired housing listings from PostgreSQL database
**Implementation**: Automated cleanup of old listing data based on age thresholds
**Status**: ✅ Implemented and tested

## 🔌 WebSocket Integration

The service provides real-time log streaming to the main INeedHousing admin dashboard:

- Live monitoring of task execution
- Real-time log streaming during cleanup operations
- Administrative oversight and debugging capabilities
- Bidirectional communication for task management

## 🧪 Testing

```bash
# Run unit tests
./mvnw test

# Run integration tests
./mvnw verify

# Run with coverage
./mvnw test jacoco:report

# Run specific test suite
./mvnw test -Dtest=ContainerRegistryTest
```

## 🔒 Security

- **Azure Authentication**: Service Principal authentication with Azure SDK
- **WebSocket Security**: Origin validation and connection limits
- **Database Security**: Encrypted connections and credential management
- **Request Filtering**: Global request filter for access token validation

## 🐳 Container Support

The service is optimized for containerized deployments:

- **JVM Mode**: Standard Docker container with fast startup
- **Native Mode**: GraalVM native executable for minimal resource usage
- **Kubernetes Ready**: Health checks and graceful shutdown support
- **Azure App Service**: Optimized for Azure cloud deployment

## 🚀 Deployment

### Automated Deployment

- **GitHub Actions**: Automated deployment to Azure App Service
- **Production Branch**: Deploys when code is pushed to `production` branch
- **Smart Change Detection**: Only deploys when cron service changes are detected
- **Azure Integration**: Seamless deployment to Azure cloud services

### Manual Deployment

- Use the provided Python scripts in the `/scripts` directory
- Docker-based deployment for containerized environments
- Azure CLI deployment for direct Azure integration

## 📄 License

This project is licensed under the terms specified in the project's license file.

## 👥 Authors

- Argel Hernandez Amaya

## 📞 Support

For support, please contact the project maintainers or open an issue in the repository.