# INeedHousing Keymaster Service

A Quarkus-based microservice that manages service registration and API token generation for secure service-to-service communication within the INeedHousing platform. This service acts as the central authority for authenticating and authorizing microservices.

## 🚀 Features

### Service Registration & Authentication
- **Service Registration**: Register new microservices with the platform using registration keys
- **API Token Generation**: Generate secure, encrypted API tokens for registered services
- **Token Validation**: Validate API tokens for service-to-service authentication
- **Registration Key Management**: Secure registration key generation and rotation

### Security Features
- **Encrypted Token Generation**: AES-256-CBC encrypted API tokens with HMAC-SHA256 signatures
- **Registration Key Rotation**: Automated registration key rotation for enhanced security
- **Token Hashing**: HMAC-SHA256-based token hashing for secure storage
- **Rate Limiting**: Built-in rate limiting for all endpoints to prevent abuse

### Key Management
- **Secure Key Generation**: Cryptographically secure registration key generation using SecureRandom
- **Key Rotation Events**: Event-driven key rotation with async notifications
- **Access Control**: Registration keys can only be accessed by the main INeedHousing API service

## 🛠️ Technical Stack

- **Framework**: Quarkus 3.25.4
- **Language**: Java 21
- **Database**: PostgreSQL with JDBC
- **Security**: AES-256-CBC encryption + HMAC-SHA256 signatures
- **Build Tool**: Maven
- **Containerization**: Docker with native and JVM modes
- **Deployment**: Google Cloud Run via GitHub Actions

## 📦 Dependencies

Key dependencies include:

- **Quarkus Core**: Arc (CDI), REST, REST Client, Jackson
- **Database**: PostgreSQL JDBC driver
- **Security**: Built-in Java crypto (AES, HMAC-SHA256)
- **Health**: SmallRye Health for monitoring
- **Fault Tolerance**: SmallRye Fault Tolerance for resilience
- **Testing**: JUnit 5, REST Assured

## 🏗️ Project Structure

```txt
src/main/java/com/ineedhousing/
├── exceptions/                  # Exception handling and mappers
│   └── ExceptionMappers.java
├── filters/                     # Request filtering and validation
│   └── ApiTokenFilter.java
├── models/                      # Data models and DTOs
│   ├── FailedRequestDto.java
│   ├── FetchRegistrationKeyEvent.java
│   ├── RegisteredServiceDto.java
│   ├── RegistrationDto.java
│   ├── RegistrationKeyDto.java
│   ├── RotatingKeyEvent.java
│   ├── ServiceVerificationDto.java
│   ├── SuccessfulKeyRotationEvent.java
│   └── VerifiedServiceDto.java
├── resources/                   # REST endpoints
│   ├── PingResource.java
│   ├── RotationKeyResource.java
│   └── ServiceAuthenticatorResource.java
├── rest_clients/                # External service clients
│   └── MainAPIEmailServiceRestClient.java
└── services/                    # Business logic services
    ├── ApiTokenGenerator.java
    ├── ApiTokenValidationService.java
    ├── KeyRotationSubscriber.java
    ├── RegistrationKeyRotator.java
    ├── ServiceAuthenticatorService.java
    └── TokenHasher.java
```

## ⚙️ Configuration

The application uses environment variables for configuration:

### Required Configuration
- `api.token.secret.key`: Secret key for token encryption and HMAC signing
- `ineedhousing.service.name`: Name of the main INeedHousing API service
- Database connection settings for PostgreSQL

### Database Schema
The service expects a `registered_service` table with:
- `id` (sequence)
- `service_name` (unique)
- `api_token_hash` (hashed token)
- `created_date` (timestamp)

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
   
   # Set up environment variables
   # Configure database connections and security settings
   
   # Run python script
   python scripts/run_keymaster_service.py
   ```

3. **Docker Deployment**

   ```bash
   # Build the application
   ./mvnw package
   
   # Build Docker image
   docker build -f src/main/docker/Dockerfile.dev -t ineedhousing/keymaster-service .
   
   # Run container
   docker run -p 8080:8080 --env-file .env ineedhousing/keymaster-service
   ```

4. **Native Build** (Optional)

   ```bash
   # Build native executable
   ./mvnw package -Dnative
   
   # Build native Docker image
   docker build -f src/main/docker/Dockerfile -t ineedhousing/keymaster-service:native .
   ```

5. **Production Deployment**

   The service is automatically deployed to Google Cloud Run via GitHub Actions when changes are pushed to the `production` branch.

## 📝 API Endpoints

### Service Registration & Authentication
- `POST /v1/auth/register` - Register a new service (rate limited: 5 requests per 10 minutes)
- `POST /v1/auth/token-validity` - Validate service API token (rate limited: 1 request per minute)

### Registration Key Management
- `GET /v1/registration-key` - Get current registration key (main API only, rate limited: 5 requests per minute)
- `POST /v1/webhooks/rotate-key` - Trigger registration key rotation (rate limited: 5 requests per minute)

### Health & Status
- `GET /ping` - Basic connectivity test

## 🔌 Integration

The Keymaster Service integrates with other INeedHousing services:

- **Backend Service**: Registers and authenticates the main API service
- **Cron Job Service**: Provides API tokens for scheduled operations
- **New Listings Service**: Manages authentication for data ingestion services
- **Admin Dashboard**: Provides service management and monitoring interface

## 🧪 Testing

```bash
# Run unit tests
./mvnw test

# Run integration tests
./mvnw verify

# Run with coverage
./mvnw test jacoco:report

# Run specific test suite
./mvnw test -Dtest=ServiceAuthenticatorServiceTest
```

## 🔒 Security

- **AES-256-CBC Encryption**: API tokens are encrypted using AES-256-CBC with random IVs
- **HMAC-SHA256 Signatures**: Token integrity protection with HMAC-SHA256 signatures
- **HMAC-SHA256 Hashing**: Secure token hashing for database storage
- **Registration Key Protection**: Registration keys only accessible by main API service
- **Rate Limiting**: Built-in rate limiting on all endpoints
- **Secure Random**: Cryptographically secure random number generation

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
- **Smart Change Detection**: Only deploys when keymaster service changes are detected
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