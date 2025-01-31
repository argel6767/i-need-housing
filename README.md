# INeedHousing.com

## Plan
INeedHousing.com will be a website that will aide incoming interns find housing in the area their internship will be. INeedHousing.com will also help New-Grad employees
find housing in the area their new jobs will be.

## Data Retrieval and Display
INeedHousing.com will use various renter APIS (Zillow, Airbnb, Craigslist, Rent Cast) to find available rental opportunities.
INeedHousing.com will also utilize the Google Maps API to display the various properties up for rent.

## Tech-Stack
INeedHousing.com will be a full-stack web app utilizing Next.js, Spring Boot, and PostgreSQL.

## Backend
Spring Boot is used as the main architecture for INeedHousing's backend, as well as a PostgreSQL database.
The following will be implemented:
- JWT authentication for endpoints
- Email verification via JavaMail
- Location-based querying via Hibernate Spatial and PostGIS for geospatial data handling
- An admin dashboard build using Vaadin.

### Status
An MVP is currently finished with all aforementioned features implemented. The MVP is hosted as a containerized app on Azure App Service. The source code can be looked over in the `/backend` directory.

## Frontend
NextJs will be used as the framework for the frontend with Tailwind CSS used for styling.
The following will be implemented:
- User registration
- Being able to see housing options on a map via Google Maps API and backend endpoints
- Allow a user to set preferences for housing to get their best fit

### Status
The frontend is currently under development and will be live once an MVP is made. Any changes/updates to the source code can be looked over in the `/frontend` directory.

## Scripts
Scripts are housed in the `scripts` directory. Scripts handle tasks such as injecting environment variables at runtime or building docker images of apps.

## Feedback
Any feedback, suggestions, tips are appreciated and can be sent to my email: [argel6767@gmail.com](mailto:argel6767@gmail.com). Thanks!
