# INeedHousing Frontend

A modern, responsive web application built with Next.js and React, providing a seamless user experience for finding and managing housing listings.

## 🚀 Features

### User Authentication

- User registration and login
- Email verification system
- Password reset functionality
- Session management

### Housing Search

- Advanced search filters
- Geographic search with Google Maps integration
- Multi-preference search capabilities
- Interactive housing listings display

### User Experience

- Responsive design
- Interactive maps
- Image carousels for property photos
- Loading states and error handling
- Form validation and error messages

### User Management

- User profile management
- Search preferences configuration
- Favorite listings management
- User dashboard

## 🛠️ Technical Stack

- **Framework**: Next.js 15.2.4
- **Language**: TypeScript
- **UI Library**: React 19
- **Styling**: Tailwind CSS + DaisyUI
- **Data Fetching**: React Query
- **State Management**: React Context API (GlobalContext)
- **Maps**: Google Maps API
- **Testing**: Jest + React Testing Library
- **Build Tool**: Turbopack

## 📦 Key Dependencies

- `@tanstack/react-query`: Data fetching and caching
- `@react-google-maps/api`: Google Maps integration
- `embla-carousel-react`: Image carousel functionality
- `class-variance-authority`: Component styling
- `lucide-react`: Icon library
- `tailwind-merge`: Utility for merging Tailwind classes
- `tailwindcss-animate`: Animation utilities

## 🏗️ Project Structure

```txt
src/
├── app/                   # Next.js app router pages
│   ├── (public)/          # Public accessible routes
│   │   ├── about/         # About page
│   │   ├── sign-in/       # Sign in page
│   │   └── sign-up/       # Sign up page
│   ├── (protected)/       # Authentication-protected routes
│   │   ├── home/          # Home page (requires auth)
│   │   └── new-user/      # New user setup (requires auth)
│   └── utils/             # Utility functions
├── components/            # Reusable components
│   ├── Carousel.tsx       # Image carousel
│   ├── Form.tsx           # Form components
│   ├── Map.tsx            # Google Maps integration
│   ├── Navbar.tsx         # Navigation bar
│   ├── ProtectedRoute.tsx # Authentication protection wrapper
│   └── ...                # Other components
├── endpoints/             # API endpoint configurations
├── hooks/                 # Custom React hooks
└── interfaces/            # TypeScript interfaces
```

## 🎨 UI Components

### Core Components

- `Navbar`: Main navigation component
- `Map`: Interactive Google Maps integration
- `HousingsList`: Display of housing listings
- `HousingSearch`: Search functionality
- `Form`: Reusable form components
- `Carousel`: Image carousel for property photos
- `VerificationCode`: Email verification component
- `Loading`: Loading state component
- `Footer`: Page footer

### Context Providers

- `GlobalContext`: Global state management
- `GoogleMapContext`: Google Maps state management

## 🚀 Getting Started

1. **Prerequisites**
   - Node.js
   - npm or yarn
   - Google Maps API key

2. **Installation**

   ```bash
   # Clone the repository
   git clone [repository-url]
   
   # Navigate to frontend directory
   cd frontend
   
   # Install dependencies
   npm install
   
   # Set up environment variables
   cp .env.example .env.local
   # Edit .env.local with your configuration
   ```

3. **Development**

   ```bash
   # Start development server
   npm run dev
   ```

4. **Building for Production**

   ```bash
   # Build the application
   npm run build
   
   # Start production server
   npm run start
   ```

## 🧪 Testing

The project uses Jest and React Testing Library for testing:

```bash
# Run tests
npm run test
```

## 🔧 Configuration

Key configuration files:

- `next.config.ts`: Next.js configuration
- `tailwind.config.ts`: Tailwind CSS configuration
- `jest.config.ts`: Jest testing configuration
- `tsconfig.json`: TypeScript configuration

## 📝 Environment Variables

Required environment variables:

- `NEXT_PUBLIC_GOOGLE_MAPS_API_KEY`: Google Maps API key
- `NEXT_PUBLIC_API_URL`: Backend API URL

## 👥 Authors

- Argel Hernandez Amaya

## 📞 Support

For support, please contact the project maintainers or open an issue in the repository.
