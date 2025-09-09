# INeedHousing New Listings Service

A Spring Boot microservice responsible for data ingestion and third-party API integration within the INeedHousing platform, handling the collection and processing of housing listings from various external sources.

## 🚀 Features

### Data Ingestion & Processing
- **Third-Party API Integration**: Integration with RentCast, Zillow, and Airbnb APIs for rental market data
- **Google Geocoding Integration**: Address-to-coordinates conversion using Google Geocoding API
- **Event-Driven Architecture**: Asynchronous event processing for data ingestion workflows
- **Data Transformation**: Intelligent formatting and enrichment of listing data with geographic coordinates

### Service Communication
- **RESTful API**: Webhook endpoints for receiving new listing events
- **Service-to-Service Communication**: Integration with main INeedHousing API and Email Service
- **Event Publishing**: Internal event system for coordinating data processing workflows
- **Health Monitoring**: Basic health check endpoints for service monitoring

### Data Management
- **Housing Listing Storage**: PostgreSQL database with spatial data support for housing listings
- **Geographic Data**: PostGIS integration for location-based data storage and queries
- **Service Registration**: Integration with Keymaster Service for secure authentication

## 🛠️ Technical Stack

- **Framework**: Spring Boot 3.5.4
- **Language**: Java 21
- **Database**: PostgreSQL with Spring Data JPA and PostGIS
- **Security**: Spring Security with service-to-service authentication
- **Cloud**: Spring Cloud 2025.0.0
- **Build Tool**: Maven
- **Containerization**: Docker
- **Deployment**: Azure Container Apps via GitHub Actions

## 📦 Dependencies

Key dependencies include:

- **Spring Boot Starters**: Web, Data JPA, Security, Actuator
- **Spring Cloud**: Circuit breaker and resilience patterns
- **Database**: PostgreSQL JDBC driver with Hibernate and PostGIS
- **Security**: Spring Security with JWT support
- **Testing**: JUnit 5, Spring Boot Test, MockMvc
- **Resilience4J**: Circuit breaker and rate limiting
- **LocationTech JTS**: Spatial data processing

## 🏗️ Project Structure

```txt
src/main/java/com/ineedhousing/new_listings_service/
├── configurations/            # Application configuration
├── constants/                 # Application constants
├── controllers/               # REST controllers
│   ├── NewListingsWebhookController.java
│   └── PingController.java
├── filters/                   # Request filtering
├── geometry/                  # Geographic data handling
├── models/                    # Data models and entities
│   ├── events/               # Event models
│   ├── requests/             # Request DTOs
│   └── responses/            # Response DTOs
├── repositories/              # Data repositories
│   ├── HousingListingRepository.java
│   └── RegisteredServiceRepository.java
├── services/                  # Business logic services
│   ├── EmailService.java
│   ├── GoogleAPIService.java
│   ├── INeedHousingAPIService.java
│   ├── NewListingsEventPublisher.java
│   └── ServiceAuthorizationService.java
├── subscribers/               # Event subscribers
└── utils/                     # Utility classes
```

## ⚙️ Configuration

The application uses environment variables for configuration:

### Required Configuration
- **Database**: PostgreSQL connection settings
- **Third-Party APIs**: RentCast, Zillow, Airbnb API credentials
- **Google APIs**: Google Geocoding API key
- **Service URLs**: INeedHousing API, Email Service, Keymaster Service URLs
- **Authentication**: Service API tokens and service names

### Rate Limiting Configuration
- **Webhooks**: 5 requests per 10 minutes
- **Ping**: 10 requests per minute

## 🚀 Getting Started

1. **Prerequisites**
   - Java 21
   - Maven
   - PostgreSQL database with PostGIS extension
   - Docker (optional)
   - Azure account (for deployment)

2. **Local Development**

   ```bash
   # Clone the repository
   git clone [repository-url]
   
   # Navigate to service directory
   cd new_listings_service
   
   # Set up environment variables
   # Configure database connections and API credentials
   
   # Run the application
   ./mvnw spring-boot:run
   ```

3. **Docker Deployment**

   ```bash
   # Build the application
   ./mvnw package
   
   # Build Docker image
   docker build -t ineedhousing/new-listings-service .
   
   # Run container
   docker run -p 8080:8080 --env-file .env ineedhousing/new-listings-service
   ```

4. **Production Deployment**

   The service is automatically deployed to Azure Container Apps via GitHub Actions when changes are pushed to the `production` branch.

## 📝 API Endpoints

### Webhook Endpoints
- `POST /v1/webhooks/new-listings` - Receive new listing events (rate limited: 5 requests per 10 minutes)

### Health & Status
- `POST /v1/ping` - Basic connectivity test (rate limited: 10 requests per minute)

## 🔌 Integration

The New Listings Service integrates with other INeedHousing services:

- **Backend Service**: Sends processed listing data and email notifications
- **Email Service**: Sends new listings notification emails
- **Keymaster Service**: Authenticates service-to-service communication
- **Google Geocoding API**: Converts addresses to geographic coordinates
- **Third-Party APIs**: RentCast, Zillow, and Airbnb for data collection

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
- **API Security**: Secure third-party API integration with credential management
- **Database Security**: Encrypted connections and credential management
- **Rate Limiting**: Built-in rate limiting on all endpoints
- **Input Validation**: Comprehensive data sanitization and validation

## 🐳 Container Support

The service is optimized for containerized deployments:

- **Docker**: Standard Docker container with Spring Boot
- **Azure Container Apps**: Optimized for Azure cloud deployment
- **Health Checks**: Readiness and liveness probes for container orchestration
- **Graceful Shutdown**: Proper shutdown handling for container environments

## 🚀 Deployment

### Automated Deployment

- **GitHub Actions**: Automated deployment to Azure Container Apps
- **Production Branch**: Deploys when code is pushed to `production` branch
- **Smart Change Detection**: Only deploys when new listings service changes are detected
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
