# INeedHousing Backend

A comprehensive Spring Boot backend application serving as the main API for the INeedHousing platform, providing robust features for property management, user authentication, search functionality, and microservice orchestration.

## 🚀 Features

### Core Features

- **User Management**
  - User registration and authentication with email verification
  - Role-based access control (Admin and User roles)
  - User profile management and updates
  - User profile pictures stored in Azure Blob Storage
  - User type management (intern, new grad, etc.)

### Housing Listings

- Comprehensive property listing management
- Advanced search capabilities with multiple filter types
- Geographic search using Hibernate Spatial and PostGIS
- Paginated listing results (v2 endpoints)
- Favorite listings functionality
- Admin listing management and deletion

### Security & Authentication

- JWT-based authentication with Http-Only cookies
- Spring Security integration with custom filters
- Service-to-service authentication via Keymaster integration
- Rate limiting with Resilience4j on all endpoints
- Secure password handling with BCrypt

### User Preferences & Search

- Advanced user preference management
- Multiple preference creation methods (coordinates, addresses)
- Exact, non-strict, and specific preference filtering
- Multi-preference search capabilities
- Google Geocoding API integration for address processing

### Admin Dashboard

- Vaadin-based admin interface (will be separated out for a stand alone Admin Dashboard Web App using Next.js in the future)
- Service management and monitoring
- Live log streaming from microservices
- User management and oversight
- Listing management and retrieval
- Keymaster service integration for service registration

### Microservice Integration

- **Cron Job Service**: WebSocket integration for live logs and maintenance operations
- **New Listings Service**: REST client integration for data ingestion
- **Keymaster Service**: Service registration and API token management
- **Third-party APIs**: RentCast, Zillow, and Airbnb integration (deprecated in favor for New Listings Service)

### Additional Features

- Email service integration with verification codes
- Azure Blob Storage integration for file management
- Caching implementation with Caffeine
- WebSocket support for real-time features
- Comprehensive exception handling and logging

## 🛠️ Technical Stack

- **Framework**: Spring Boot 3.4.4
- **Language**: Java 21
- **Database**: PostgreSQL with Hibernate Spatial 6.2.6.Final
- **Security**: Spring Security + JWT
- **UI Framework**: Vaadin 24.6.2
- **Build Tool**: Maven
- **Containerization**: Docker
- **Cloud Services**: Azure Blob Storage, Azure App Service

## 📦 Dependencies

Key dependencies include:

- **Spring Boot Starters**: Web, Data JPA, Security, Mail, Cache, DevTools
- **Hibernate Spatial 6.2.6.Final**: Geographic queries and PostGIS integration
- **JWT (jjwt 0.11.5)**: Token-based authentication
- **Resilience4j 2.0.2**: Rate limiting and circuit breakers
- **Vaadin 24.6.2**: Admin dashboard UI components with dark theme
- **Lombok**: Reducing boilerplate code
- **Azure Blob SDK 1.2.33**: File storage integration
- **Caffeine**: High-performance caching
- **Java-WebSocket 1.5.4**: WebSocket client for microservice communication
- **Hypersistence Utils 3.9.0**: Hibernate utilities
- **Commons Validator 1.7**: Input validation
- **BouncyCastle 1.80**: Cryptographic operations

## 🏗️ Project Structure

```txt
src/main/java/com/ineedhousing/backend/
├── admin/                 # Admin dashboard and management
│   ├── components/        # Vaadin UI components
│   ├── exceptions/        # Admin-specific exceptions
│   ├── models/           # Admin DTOs and models
│   └── views/            # Vaadin views and forms
├── apis/                  # Third-party API integrations
│   ├── requests/         # API request DTOs
│   └── exceptions/       # API-specific exceptions
├── azure/                 # Azure Blob Storage integration
│   └── blob/             # Blob storage services
├── auth/                  # Authentication and authorization
│   ├── requests/         # Auth request DTOs
│   ├── responses/        # Auth response DTOs
│   └── exceptions/       # Auth-specific exceptions
├── configs/               # Application configuration
├── constants/             # Application constants
├── cron_job_service/      # Cron job microservice integration
│   ├── model/            # Cron service DTOs
│   ├── rest/             # REST client configuration
│   └── ws/               # WebSocket integration
├── email/                 # Email service implementation
│   └── models/           # Email DTOs
├── exception/             # Global exception handling
├── favorite_listings/     # Favorite listings functionality
│   └── requests/         # Favorite listing DTOs
├── geometry/              # Geographic data handling
│   ├── dto/              # Geometry DTOs
│   └── exceptions/       # Geometry exceptions
├── housing_listings/      # Property listing management
│   ├── dto/              # Listing DTOs
│   ├── exceptions/       # Listing exceptions
│   └── utils/            # Listing utilities
├── jwt/                   # JWT token handling
├── keymaster_service/     # Keymaster microservice integration
│   └── models/           # Keymaster DTOs
├── model/                 # Shared data models
├── new_listings_service/  # New listings microservice integration
├── ping_services/         # Service health monitoring
│   └── models/           # Ping event models
├── user/                  # User management
│   ├── requests/         # User request DTOs
│   └── responses/        # User response DTOs
└── user_search_preferences/ # User search preferences
    ├── requests/         # Preference request DTOs
    ├── responses/        # Preference response DTOs
    ├── exceptions/       # Preference exceptions
    └── utils/            # Preference utilities
```

## 🔧 Configuration

The application uses environment variables for configuration. Key configuration files:

- `azure.env`: Azure service configuration
- `application.properties`: Core application settings
- Environment-specific configurations for different deployment stages

## 🐳 Docker Support

The application includes:

- `Dockerfile` for containerization
- `compose.yaml` for Docker Compose setup
- `.dockerignore` for optimized builds
- Spring Boot Docker Compose starter for easy local development

## 🚀 Getting Started

1. **Prerequisites**
   - Java 21
   - Maven
   - PostgreSQL
   - Docker (optional)
   - Azure account for cloud services
   - Python 3 for deployment scripts

2. **Setup**

   ```bash
   # Clone the repository
   git clone [repository-url]
   
   # Run Python script for local development
   python scripts/run_backend.py
   ```

3. **Docker Deployment**

   ```bash
   # Build the Docker image
   docker build -t ineedhousing-backend .
   
   # Run with Docker Compose
   docker-compose up
   ```

4. **Production Deployment**

   The backend is automatically deployed to Azure App Service via GitHub Actions when changes are pushed to the `production` branch.

## 🔒 Security

The application implements:

- JWT-based authentication
- Role-based access control
- Secure password storage
- Rate limiting for API endpoints
- Http-Only cookies for enhanced security

## 📝 API Documentation

The application provides the following REST API endpoints:

### Authentication (`/auths`)

- `POST /register` - User registration with email verification
- `POST /login` - User login with JWT cookie
- `POST /verify` - Email verification with code
- `POST /logout` - User logout (clears cookie)
- `POST /resend` - Resend verification email
- `PUT /password` - Update user password
- `POST /forgot/{email}` - Password reset request
- `PUT /reset` - Reset forgotten password
- `POST /cookie-status` - Check JWT cookie validity

### Users (`/users`)

- `GET /me` - Get current user details
- `PUT /me` - Update user information
- `PUT /type` - Update user type (intern, new grad, etc.)
- `DELETE /me` - Delete current user account

### Housing Listings (`/listings`)

- `GET /area` - Get listings in geographic area
- `GET /v2/area` - Get paginated listings in area
- `GET /{id}` - Get specific listing details
- `DELETE /{id}` - Delete listing (admin only)
- `POST /filter/exact` - Exact preference matching
- `GET /filter/v2/exact` - Paginated exact preference matching
- `POST /filter/non-strict` - Non-strict preference matching
- `POST /filter/specific` - Specific preference filtering
- `POST /preferences/multi` - Multi-preference search

### User Preferences (`/preferences`)

- `POST /` - Create user preferences
- `POST /coordinates` - Create preferences with coordinates
- `POST /addresses` - Create preferences with addresses
- `PUT /` - Update user preferences
- `PUT /filters` - Update preference filters
- `GET /me` - Get current user preferences

### Favorite Listings (`/favorites`)

- `GET /me` - Get user's favorite listings
- `POST /listings` - Add listings to favorites
- `DELETE /listings/{id}` - Remove specific favorite
- `DELETE /` - Remove all favorites

### Admin (`/admin`)

- `POST /login` - Admin authentication
- `POST /keymaster-service/register-service/{service}` - Register new service

### Service Health (`/ping`)

- `GET /ping` - Service health check

## 🧪 Testing

The project includes:

- Unit tests
- Integration tests
- Comprehensive test coverage for all major components

## 🚀 Deployment

### Automated Deployment

- **GitHub Actions**: Automated deployment to Azure App Service
- **Production Branch**: Deploys when code is pushed to `production` branch
- **Smart Change Detection**: Only deploys when backend changes are detected
- **Azure Integration**: Seamless deployment to Azure cloud services

### Manual Deployment

- Use the provided Python scripts in the `/scripts` directory
- Docker-based deployment for containerized environments

## 📄 License

This project is licensed under the terms specified in the project's license file.

## 👥 Authors

- Argel Hernandez Amaya

## 📞 Support

For support, please contact the project maintainers or open an issue in the repository.
