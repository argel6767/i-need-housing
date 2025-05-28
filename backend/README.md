# INeedHousing Backend

A robust Spring Boot backend application for a housing listing platform, providing comprehensive features for property management, user authentication, and search functionality.

## 🚀 Features

### Core Features

- **User Management**
  - User registration and authentication
  - Role-based access control (Admin and User roles)
  - User profile management
    - User profile pictures housed in Azure Blob Container
  - User search preferences

### Housing Listings

- Property listing management
- Advanced search capabilities
- Favorite listings functionality
- Geographic search using spatial data

### Security

- JWT-based authentication
- Http-Only Cookies
- Spring Security integration
- Role-based authorization
- Secure password handling

### Additional Features

- Email service integration
- Rate limiting with Resilience4j
- Caching implementation
- Admin dashboard functionality

## 🛠️ Technical Stack

- **Framework**: Spring Boot 3.4.4
- **Language**: Java 21
- **Database**: PostgreSQL with Hibernate Spatial
- **Security**: Spring Security + JWT
- **UI Framework**: Vaadin 24.6.2
- **Build Tool**: Maven
- **Containerization**: Docker

## 📦 Dependencies

Key dependencies include:

- Spring Boot Starters (Web, Data JPA, Security, Mail, Cache)
- Hibernate Spatial for geographic queries
- JWT for authentication
- Resilience4j for rate limiting
- Vaadin for UI components
- Lombok for reducing boilerplate code
- Azure Blob SDK

## 🏗️ Project Structure

```txt
src/main/java/com/ineedhousing/backend/
├── admin/           # Admin-specific functionality
├── apis/            # External API calls for listing gathering
├── azure/           # Azure services management
├── auth/            # Authentication and authorization
├── configs/         # Configuration classes
├── email/           # Email service implementation
├── exception/       # Custom exception handling
├── favorite_listings/ # Favorite listings functionality
├── geometry/        # Geographic data handling
├── housing_listings/ # Property listing management
├── jwt/            # JWT token handling
├── user/           # User management
└── user_search_preferences/ # User search preferences
```

## 🔧 Configuration

The application uses environment variables for configuration. Key configuration files:

- `dev.env`: Development environment configuration
- `prod.env`: Production environment configuration
- `application.properties`: Core application settings

## 🐳 Docker Support

The application includes:

- `Dockerfile` for containerization
- `compose.yaml` for Docker Compose setup
- `.dockerignore` for optimized builds

## 🚀 Getting Started

1. **Prerequisites**
   - Java 21
   - Maven
   - PostgreSQL
   - Docker (optional)
   - a dev.env file with environment variables
   - Python 3

2. **Setup**

   ```bash
   # Clone the repository
   git clone [repository-url]
   
   # Run Python script
   python scripts/run_backend.py
   ```

3. **Docker Deployment**

   ```bash
   # Build the Docker image
   docker build -t ineedhousing-backend .
   
   # Run with Docker Compose
   docker-compose up
   ```

## 🔒 Security

The application implements:

- JWT-based authentication
- Role-based access control
- Secure password storage
- Rate limiting for API endpoints

## 📝 API Documentation

The application provides the following REST API endpoints:

### Authentication (`/auths`)

- `POST /register` - User registration
- `POST /login` - User login
- `POST /verify` - Email verification
- `POST /logout` - User logout
- `POST /resend` - Resend verification email
- `PUT /password` - Update password
- `POST /forgot/{email}` - Password reset request
- `PUT /reset` - Reset password

### Users (`/users`)

- `GET /me` - Get user details
- `PUT /me` - Update user information
- `PUT /type` - Update user type
- `DELETE /me` - Delete user

### Profile Pictures (`/profile-pictures`)
- `GET /url` - Get user's profile picture's SAS URL
- `PUT /url` - Update user's profile picture's SAS URL
- `PUT /upload` - Update user's profile picture
- `GET /me` - Get user's Profile Picture entity
- `POST /` - Create user's Profile Picture entity
- `DELETE /me` - Delete user's Profile Picture entity

### Housing Listings (`/listings`)

- `GET /area` - Get listings in area
- `GET /{id}` - Get specific listing
- `DELETE /{id}` - Delete listing
- `POST /filter/exact` - Exact search filter
- `POST /filter/non-strict` - Non-strict search filter
- `POST /filter/specific` - Specific search filter
- `POST /preferences/multi` - Multi-preference search

### User Preferences (`/preferences`)

- `POST /` - Create preferences
- `POST /coordinates` - Set coordinates
- `POST /addresses` - Set addresses
- `PUT /filters` - Update preferences
- `PUT /` - Bulk update preferences
- `GET /me` - Get user preferences

### Favorite Listings (`/favorites`)

- `GET /me` - Get user favorites
- `PUT /listings` - Update favorite listings
- `POST /listings` - Add to favorites
- `DELETE /` - Remove from favorites

## 🧪 Testing

The project includes:

- Unit tests
- Integration tests

## 📄 License

This project is licensed under the terms specified in the project's license file.

## 👥 Authors

- Argel Hernandez Amaya

## 📞 Support

For support, please contact the project maintainers or open an issue in the repository.
