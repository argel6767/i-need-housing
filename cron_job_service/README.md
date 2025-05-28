# INeedHousing Cron Job Service

A lightweight Quarkus-based microservice designed to handle scheduled maintenance tasks for the INeedHousing platform, including Azure resource cleanup and database maintenance operations.

## 🚀 Features

### Scheduled Tasks

- **Container Registry Cleanup**
    - Automated deletion of old container images from Azure Container Registry
    - Configurable retention policies for image versions
    - Support for repository-specific cleanup rules

- **Database Maintenance**
    - Automated cleanup of old listing data from Azure PostgreSQL
    - Expired listing removal based on configurable time thresholds
    - Database optimization and maintenance tasks

### Real-time Monitoring

- **WebSocket Integration**
    - Live log streaming to INeedHousing admin dashboard
    - Real-time task execution status updates
    - Bidirectional communication for task management

### Logging & Auditing

- **Persistent Log Storage**
    - Automated log archival to Azure Blob Storage
    - Structured logging with task execution metadata
    - Searchable log history for audit trails

## 🛠️ Technical Stack

- **Framework**: Quarkus 3.22.3
- **Language**: Java 21
- **Database**: SQLite (local task tracking)
- **Cloud Services**: Azure Container Registry, Azure Blob Storage, Azure PostgreSQL
- **Build Tool**: Maven
- **Containerization**: Docker

## 📦 Dependencies

Key dependencies include:

- **Quarkus Core**: Arc (CDI), REST, WebSockets Next, Scheduler
- **Azure SDKs**: Container Registry, Blob Storage
- **Database**: SQLite JDBC driver
- **Testing**: JUnit 5, REST Assured

## 🏗️ Project Structure

```txt
src/main/java/com/ineedhousing/cronjob/
├── azure/              # Azure service integrations
│   ├── blob/           # Blob storage operations
│   ├── container/      # Container registry management
│   └── postgres/       # PostgreSQL database operations
├── config/             # Configuration classes
├── logging/            # Logging and audit functionality
├── scheduler/          # Cron job definitions and management
```

## ⚙️ Configuration

The application will use environment variables for configuration. Configuration setup is planned for:

### Azure Configuration (Planned)
- Container registry connection settings
- Blob storage connection configuration
- PostgreSQL database connection parameters

### Scheduling Configuration (Planned)
- Image retention policies and cleanup schedules
- Database cleanup schedules and retention periods
- Task execution parameters and timeouts

### WebSocket Configuration (Planned)
- Admin dashboard connection settings
- Real-time logging stream configuration

## 🚀 Getting Started

1. **Prerequisites**
    - Java 21
    - Maven
    - Docker (optional)
    - Azure account with Container Registry and Blob Storage

2. **Local Development**

   ```bash
   # Clone the repository
   git clone [repository-url]
   
   # Navigate to service directory
   cd cron-job-service
   
   # Set up environment variables (when configuration is implemented)
   # cp .env.example .env
   # Edit .env with your Azure configuration
   
   # Run in development mode
   ./mvnw compile quarkus:dev
   ```

3. **Docker Deployment**

   ```bash
   # Build the application
   ./mvnw package
   
   # Build Docker image
   docker build -f src/main/docker/Dockerfile.jvm -t ineedhousing/cron-service .
   
   # Run container (once configuration is implemented)
   # docker run -p 8080:8080 --env-file .env ineedhousing/cron-service
   ```

4. **Native Build** (Optional)

   ```bash
   # Build native executable
   ./mvnw package -Dnative
   
   # Build native Docker image
   docker build -f src/main/docker/Dockerfile.native -t ineedhousing/cron-service:native .
   ```

## 📝 API Endpoints

### Planned Endpoints

#### Health & Status (Planned)
- Health check and service status endpoints
- Readiness and liveness probes for container orchestration

#### Task Management (Planned)
- Task execution status monitoring
- Manual task triggering capabilities
- Task execution history and logging

#### WebSocket Integration (Planned)
- Real-time log streaming for admin dashboard
- Live task execution monitoring

## 🔄 Scheduled Tasks

### Planned Tasks

#### Image Cleanup Task
**Purpose**: Remove old container images from Azure Container Registry
**Implementation**: Scheduled cleanup based on configurable retention policies
**Logging**: Task execution logs will be archived to Azure Blob Storage

#### Listing Cleanup Task
**Purpose**: Remove expired housing listings from PostgreSQL database
**Implementation**: Automated cleanup of old listing data based on age thresholds
**Logging**: Database maintenance logs will be archived to Azure Blob Storage

## 🔌 WebSocket Integration

The service will provide real-time log streaming to the main INeedHousing admin dashboard. This feature is planned to enable:

- Live monitoring of task execution
- Real-time log streaming during cleanup operations
- Administrative oversight and debugging capabilities

## 🗄️ Log Management

### Planned Log Storage Structure
The service will implement a structured logging system with:

- **Task Execution Logs**: Detailed logs for each cleanup operation
- **System Logs**: Application-level logging and error tracking
- **Archive Storage**: Long-term log retention in Azure Blob Storage
- **Real-time Streaming**: Live log delivery to admin dashboard via WebSocket

### Planned Log Features
- Structured logging with metadata and timestamps
- Automatic log rotation and archival
- Searchable log history for audit purposes
- Integration with Azure Blob Storage for persistence

## 🧪 Testing

```bash
# Run unit tests
./mvnw test

# Run integration tests
./mvnw verify

# Run with coverage
./mvnw test jacoco:report
```

## 🚨 Monitoring & Alerts

The service includes built-in monitoring capabilities:

- **Task Execution Metrics**: Success/failure rates, execution duration
- **Resource Usage**: Memory, CPU, and network utilization
- **Error Tracking**: Automatic error logging and notification
- **Health Checks**: Kubernetes-ready liveness and readiness probes

## 🔒 Security

- **Azure Authentication**: Managed Identity or Service Principal authentication
- **WebSocket Security**: Origin validation and connection limits
- **Database Security**: Encrypted connections and credential management
- **Log Security**: Sensitive data filtering and secure storage

## 🐳 Container Support

The service is optimized for containerized deployments:

- **JVM Mode**: Standard Docker container with fast startup
- **Native Mode**: GraalVM native executable for minimal resource usage
- **Kubernetes Ready**: Health checks and graceful shutdown support

## 📄 License

This project is licensed under the terms specified in the project's license file.

## 👥 Authors

- Argel Hernandez Amaya

## 📞 Support

For support, please contact the project maintainers or open an issue in the repository.