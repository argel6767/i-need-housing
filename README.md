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
- Automated maintenance tasks via cron job service

## Tech Stack

- **Frontend**: Next.js 15.2.4 with Tailwind CSS + DaisyUI
- **Backend**: Spring Boot 3.4.4 with PostgreSQL
- **Microservices**: Quarkus 3.22.3-based cron job service
- **APIs**: Google Maps, Zillow, Airbnb, RentCast
- **Infrastructure**: Azure App Service (Backend), Vercel (Frontend), Azure Container Registry
- **CI/CD**: GitHub Actions with automated deployment to production

## Project Status

The project has successfully reached its MVP milestone and continues to evolve:

- ✅ **Complete MVP web application** - Fully functional and deployed
- ✅ **Frontend deployed** - Accessible on Vercel with modern Next.js 15
- ✅ **Backend services running** - Spring Boot application on Azure App Service
- ✅ **Core features implemented** - All major functionality tested and working
- ✅ **Cron job microservice** - Quarkus-based service for automated maintenance tasks
- ✅ **Automated deployment** - GitHub Actions workflow for production deployments
- 🔄 **Ongoing enhancements** - Continuous improvements and new features

## Project Structure

- `/backend`: Spring Boot 3.4.4 application with PostgreSQL integration
- `/frontend`: Next.js 15.2.4 application with Tailwind CSS and DaisyUI
- `/cron_job_service`: Quarkus 3.22.3 microservice for automated maintenance
- `/scripts`: Utility scripts for deployment and environment management
- `/.github/workflows`: GitHub Actions deployment workflows

## Project Diagram

[![](https://mermaid.ink/img/pako:eNqVV2Fv2zYQ_SsEiw4pIHu2bMeOBgyI7XTNkKRu5HZA52GgJcrSIpMCRSVO0_z3HSmJFmUnRf3F5unu8fh4705-wgEPKfZwlPKHICZCouV8xRB83r5FFztJBSMpmqUJZTIvH5SLv1f48obS8AMv8oRt0F90jc6zDJ3c0J3s_pe_W-F_Sv_zcJuwOcnjNScihDhtQMaCTr4QEibMQZnan0fIz4SCnHIu0ZYzniYyLvFMass4gcgFBDyi88VllZq2aiPYfNiq5ebbINcE8piS4I6ysDTmxXojSBaj5tn-rVzaR67M6KSR7_7Y6lN5wM4Qe3vhL1USlsd5IWNFCXyhX9DnnArIipEN3QLHludVkkvYJQfvOoHaZPkpjIXgUZLSRRLIQlAVopErM6rtdibWNZV2zYuhayY4Q3_yNbpOAsEB8D4JaIs35QMufvkQNjZBlQmdfCqIuCtymynl5gcxDYuUCggzv20vziTcGRWzlBJWZIq5b3CS_QN0SzfAinhElUuLw3t6xTWHUK8-h9uRnrYiZbZ8L-7hBhTDlOmM9BrVBst1PvWbZNis3dAHc1GvMQd-tduePSvYMPhivcGhYs7vyuOpX8fz1UdZFOs0yePG2YzFcvaLdR6IZE2Foq2xQie0u-k6wDiTM5JLB31NUmgjDup2u-jd0eJ9z8WWSKk3NecyRisEZFCQdCmSzWaj_UsD0paGr822Dz2MhtBcJFmTvOJY18jiD1MtC57LjaD-pyvgsgqYthoMVBSjgUw4s9oe6nR-__5huVx8b4j7WJ_7GUe0wl8SfdOmEFW4Kdc6rWOBqqt4upPBPRMG5fExq-NtMdYoltRKkL18jGpKgJbeXgQwd2nHt4RhlmVQXW9Q2UUQ0DyPihTNaUoV62WRVjiWFutz1N_WwxK5qpFaDyusQOoFayjFSuRwy70i2KFu7K0awqgQmhbWUlIdS1gExY8ITJFSBbVSdAWbMmiJp1GmFzuyzaClSzXoOllrHh7s-J7KIG5i2xPzpwvt10qm6WOtS7vfzQQl6jar3WxN29fYEO8UBW317TUEQJ1a0UfLSlcQFNXHNGxMyMOwA1rLcJ-ACJuHaMeadLWlXB0ZuiXa5yzlJDwyeTUfGmGa8rVpTWqBfMkFvAI0GlJbiJVs1QK8CSBfbiHCwp3dvjIe7Wbny8eUVkwHKcnzOY3MmxeCzFPvDe1Ho4g6EMzvqPem504mYb9adh6SUMaem-1-a4M0Bl4FFA3oKBoZoAl1h4T8EIjW76IVSAQwPQMSRetBr_dDkHBdH2YSjeiZCR9MJnQQvBp-9I3QcNR0tLuuxUDT7XDev-hajh7HVqWzl66ZwHtTOYoNa020qpCBDOzgjUhC7EUkzamDtxSUoNb4SQWssIzhPXSFPfgZ0ogUKXTHFXuGuIywr5xvsSdFAZGCF5vY4BRZSCSdJwTebLbGKoAwKGBeMIm9gesOhxoGe094h73xsDt2e0N3MBmcnY7PxiMHP2KvM-lOTvtDdzQ6HU367ng4enbwN71xv9tze2fu4HQ8HA7dXv_MwTRMQDnX5T8a_cfm-X8oUDoj?type=png)](https://mermaid.live/edit#pako:eNqVV2Fv2zYQ_SsEiw4pIHu2bMeOBgyI7XTNkKRu5HZA52GgJcrSIpMCRSVO0_z3HSmJFmUnRf3F5unu8fh4705-wgEPKfZwlPKHICZCouV8xRB83r5FFztJBSMpmqUJZTIvH5SLv1f48obS8AMv8oRt0F90jc6zDJ3c0J3s_pe_W-F_Sv_zcJuwOcnjNScihDhtQMaCTr4QEibMQZnan0fIz4SCnHIu0ZYzniYyLvFMass4gcgFBDyi88VllZq2aiPYfNiq5ebbINcE8piS4I6ysDTmxXojSBaj5tn-rVzaR67M6KSR7_7Y6lN5wM4Qe3vhL1USlsd5IWNFCXyhX9DnnArIipEN3QLHludVkkvYJQfvOoHaZPkpjIXgUZLSRRLIQlAVopErM6rtdibWNZV2zYuhayY4Q3_yNbpOAsEB8D4JaIs35QMufvkQNjZBlQmdfCqIuCtymynl5gcxDYuUCggzv20vziTcGRWzlBJWZIq5b3CS_QN0SzfAinhElUuLw3t6xTWHUK8-h9uRnrYiZbZ8L-7hBhTDlOmM9BrVBst1PvWbZNis3dAHc1GvMQd-tduePSvYMPhivcGhYs7vyuOpX8fz1UdZFOs0yePG2YzFcvaLdR6IZE2Foq2xQie0u-k6wDiTM5JLB31NUmgjDup2u-jd0eJ9z8WWSKk3NecyRisEZFCQdCmSzWaj_UsD0paGr822Dz2MhtBcJFmTvOJY18jiD1MtC57LjaD-pyvgsgqYthoMVBSjgUw4s9oe6nR-__5huVx8b4j7WJ_7GUe0wl8SfdOmEFW4Kdc6rWOBqqt4upPBPRMG5fExq-NtMdYoltRKkL18jGpKgJbeXgQwd2nHt4RhlmVQXW9Q2UUQ0DyPihTNaUoV62WRVjiWFutz1N_WwxK5qpFaDyusQOoFayjFSuRwy70i2KFu7K0awqgQmhbWUlIdS1gExY8ITJFSBbVSdAWbMmiJp1GmFzuyzaClSzXoOllrHh7s-J7KIG5i2xPzpwvt10qm6WOtS7vfzQQl6jar3WxN29fYEO8UBW317TUEQJ1a0UfLSlcQFNXHNGxMyMOwA1rLcJ-ACJuHaMeadLWlXB0ZuiXa5yzlJDwyeTUfGmGa8rVpTWqBfMkFvAI0GlJbiJVs1QK8CSBfbiHCwp3dvjIe7Wbny8eUVkwHKcnzOY3MmxeCzFPvDe1Ho4g6EMzvqPem504mYb9adh6SUMaem-1-a4M0Bl4FFA3oKBoZoAl1h4T8EIjW76IVSAQwPQMSRetBr_dDkHBdH2YSjeiZCR9MJnQQvBp-9I3QcNR0tLuuxUDT7XDev-hajh7HVqWzl66ZwHtTOYoNa020qpCBDOzgjUhC7EUkzamDtxSUoNb4SQWssIzhPXSFPfgZ0ogUKXTHFXuGuIywr5xvsSdFAZGCF5vY4BRZSCSdJwTebLbGKoAwKGBeMIm9gesOhxoGe094h73xsDt2e0N3MBmcnY7PxiMHP2KvM-lOTvtDdzQ6HU367ng4enbwN71xv9tze2fu4HQ8HA7dXv_MwTRMQDnX5T8a_cfm-X8oUDoj)

## Getting Started

Detailed setup instructions can be found in the respective README files:

- [Backend Setup](backend/README.md)
- [Frontend Setup](frontend/README.md)
- [Cron Job Service](cron_job_service/README.md)

## Deployment

The project uses automated deployment via GitHub Actions:

- **Production Branch**: Automatic deployment when code is pushed to `production` branch
- **Smart Change Detection**: Only deploys services with actual changes
- **Azure Integration**: Seamless deployment to Azure App Services
- **Container Registry**: Automated image building and pushing to Azure Container Registry

## Contact

For feedback, suggestions, or inquiries, please contact [argel6767@gmail.com](mailto:argel6767@gmail.com).