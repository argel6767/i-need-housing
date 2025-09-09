# INeedHousing Email Service

A Quarkus-based microservice responsible for handling email communications within the INeedHousing platform, including user verification emails, notifications, and system alerts.

## 🚀 Features

### Email Management
- **User Verification Emails**: Send email verification codes for user registration
- **Password Reset Emails**: Handle password reset requests and notifications
- **System Notifications**: Send administrative and system-related emails
- **Email Templates**: Structured email templates for consistent messaging

### Integration Features
- **Main API Integration**: REST client integration with the main INeedHousing API
- **Service Authentication**: Secure service-to-service communication via API tokens
- **Rate Limiting**: Built-in rate limiting to prevent API abuse
- **Health Monitoring**: Health checks and service status endpoints

## 🛠️ Technical Stack

- **Framework**: Quarkus 3.26.2
- **Language**: Java 21
- **Database**: PostgreSQL with Hibernate ORM Panache
- **Build Tool**: Maven
- **Containerization**: Docker with native and JVM modes
- **Deployment**: Google Cloud Run via GitHub Actions

## 📦 Dependencies

Key dependencies include:

- **Quarkus Core**: Arc (CDI), REST, REST Client, Jackson
- **Database**: PostgreSQL JDBC driver with Hibernate ORM Panache
- **Email**: Quarkus Mail for email functionality
- **Health**: SmallRye Health for monitoring
- **Testing**: JUnit 5, REST Assured

## 🏗️ Project Structure

```txt
src/main/java/com/ineedhousing/
├── configs/                   # Application configuration
├── constants/                 # Application constants
├── exception/                 # Exception handling
├── filters/                   # Request filtering
├── models/                    # Data models and DTOs
├── resources/                 # REST endpoints
├── rest_clients/              # External service clients
└── services/                  # Business logic services
```

## ⚙️ Configuration

The application uses environment variables for configuration:

### Required Configuration
- Database connection settings for PostgreSQL
- Email server configuration (SMTP settings)
- Main API service URL and authentication
- Service-specific configuration parameters

## 🚀 Getting Started

1. **Prerequisites**
   - Java 21
   - Maven
   - PostgreSQL database
   - Docker (optional)
   - Google Cloud account (for deployment)

2. **Local Development**

   ```bash
   # Clone the repository
   git clone [repository-url]
   
   # Navigate to service directory
   cd email_service
   
   # Set up environment variables
   # Configure database connections and email settings
   
   # Run in development mode
   ./mvnw compile quarkus:dev
   ```

3. **Docker Deployment**

   ```bash
   # Build the application
   ./mvnw package
   
   # Build Docker image
   docker build -f src/main/docker/Dockerfile.jvm -t ineedhousing/email-service .
   
   # Run container
   docker run -p 8080:8080 --env-file .env ineedhousing/email-service
   ```

4. **Native Build** (Optional)

   ```bash
   # Build native executable
   ./mvnw package -Dnative
   
   # Build native Docker image
   docker build -f src/main/docker/Dockerfile.native -t ineedhousing/email-service:native .
   ```

5. **Production Deployment**

   The service is automatically deployed to Google Cloud Run via GitHub Actions when changes are pushed to the `production` branch.

## 📝 API Endpoints

### Email Operations
- `POST /email/send-verification` - Send user verification email
- `POST /email/send-password-reset` - Send password reset email
- `POST /email/send-notification` - Send system notification

### Health & Status
- `GET /ping` - Basic connectivity test
- `GET /q/health` - Detailed health check

## 🔌 Integration

The Email Service integrates with other INeedHousing services:

- **Backend Service**: Receives email requests from the main API
- **Keymaster Service**: Authenticates service-to-service communication
- **Admin Dashboard**: Provides email service monitoring and management

## 🧪 Testing

```bash
# Run unit tests
./mvnw test

# Run integration tests
./mvnw verify

# Run with coverage
./mvnw test jacoco:report
```

## 🔒 Security

- **Service Authentication**: API token-based authentication with Keymaster Service
- **Email Security**: Secure SMTP configuration and email validation
- **Database Security**: Encrypted connections and credential management
- **Request Filtering**: Global request filter for access token validation

## 🐳 Container Support

The service is optimized for containerized deployments:

- **JVM Mode**: Standard Docker container with fast startup
- **Native Mode**: GraalVM native executable for minimal resource usage
- **Kubernetes Ready**: Health checks and graceful shutdown support
- **Cloud Run**: Optimized for Google Cloud Run deployment

## 🚀 Deployment

### Automated Deployment

- **GitHub Actions**: Automated deployment to Google Cloud Run
- **Production Branch**: Deploys when code is pushed to `production` branch
- **Smart Change Detection**: Only deploys when email service changes are detected
- **GCP Integration**: Seamless deployment to Google Cloud Platform

### Manual Deployment

- Use the provided Python scripts in the `/scripts` directory
- Docker-based deployment for containerized environments
- Google Cloud CLI deployment for direct GCP integration

## 📄 License

This project is licensed under the terms specified in the project's license file.

## 👥 Authors

- Argel Hernandez Amaya

## 📞 Support

For support, please contact the project maintainers or open an issue in the repository.
