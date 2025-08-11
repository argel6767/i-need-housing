# [INeedHousing](https://i-need-housing.vercel.app)

## Overview

INeedHousing.com is a web application designed to help incoming interns and new-grad employees find housing near their workplace. The platform aggregates rental listings from various sources and provides a user-friendly interface for finding suitable accommodations.

## Features

- User registration and authentication
- Location-based property search
- Interactive map interface for property visualization
- Customizable housing preferences
- Email verification system
- Admin dashboard for property management

## Tech Stack

- **Frontend**: Next.js with Tailwind CSS
- **Backend**: Spring Boot with PostgreSQL
- **Microservices**: Quarkus-based cron job service
- **APIs**: Google Maps, Zillow, Airbnb, Craigslist, RentCast
- **Infrastructure**: Azure App Service (Backend), Vercel (Frontend), Azure Container Registry

## Project Status

The project has successfully reached its MVP milestone:

- Complete MVP web application now available and fully functional
- Frontend deployed and accessible on Vercel
- Backend services running on Azure App Service
- Core features implemented and tested
- **New**: Cron job microservice in development for automated maintenance tasks
- Ongoing enhancements and refinements continue as we expand beyond the MVP

## Project Structure

- `/backend`: Spring Boot application with PostgreSQL integration
- `/frontend`: Next.js application with Tailwind CSS
- `/cron-job-service`: Quarkus microservice for automated maintenance
- `/scripts`: Utility scripts for deployment and environment management

## Getting Started

Detailed setup instructions can be found in the respective README files:

- [Backend Setup](backend/README.md)
- [Frontend Setup](frontend/README.md)
- [Cron Job Service](cron_job_service/README.md) *(in development)*

## Contact

For feedback, suggestions, or inquiries, please contact [argel6767@gmail.com](mailto:argel6767@gmail.com).