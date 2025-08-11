# INeedHousing Frontend

A modern, responsive web application built with Next.js and React, providing a seamless user experience for finding and managing housing listings.

## 🚀 Features

### User Authentication

- User registration and login
- Email verification system
- Password reset functionality
- Session management with JWT tokens

### Housing Search

- Advanced search filters
- Geographic search with Google Maps integration
- Multi-preference search capabilities
- Interactive housing listings display
- Real-time search results

### User Experience

- Responsive design optimized for all devices
- Interactive maps with Google Maps API
- Image carousels for property photos
- Loading states and error handling
- Form validation and error messages

### User Management

- User profile management
- Search preferences configuration
- Favorite listings management
- User dashboard with personalized content

## 🛠️ Technical Stack

- **Framework**: Next.js 15.2.4
- **Language**: TypeScript 5
- **UI Library**: React 19
- **Styling**: Tailwind CSS 3.4.1 + DaisyUI 4.12.23
- **Data Fetching**: React Query (@tanstack/react-query 5.66.0)
- **State Management**: React Context API (GlobalContext)
- **Maps**: Google Maps API with @react-google-maps/api
- **Testing**: Jest 29.7.0 + React Testing Library 16.2.0
- **Build Tool**: Turbopack (Next.js built-in)
- **Deployment**: Vercel with automated deployments

## 📦 Key Dependencies

### Core Dependencies

- `next`: ^15.2.4 - React framework
- `react`: ^19.0.0 - UI library
- `react-dom`: ^19.0.0 - React DOM rendering
- `typescript`: ^5 - Type safety

### UI & Styling

- `tailwindcss`: ^3.4.1 - Utility-first CSS framework
- `daisyui`: ^4.12.23 - Tailwind CSS component library
- `class-variance-authority`: ^0.7.1 - Component styling variants
- `tailwind-merge`: ^2.6.0 - Tailwind class merging utility
- `tailwindcss-animate`: ^1.0.7 - Animation utilities

### Data & State

- `@tanstack/react-query`: ^5.66.0 - Data fetching and caching
- `axios`: ^1.7.9 - HTTP client

### Maps & Location

- `@react-google-maps/api`: ^2.20.5 - Google Maps integration
- `react-google-autocomplete`: ^2.7.5 - Google Places autocomplete

### UI Components

- `embla-carousel-react`: ^8.5.2 - Image carousel functionality
- `lucide-react`: ^0.469.0 - Icon library
- `@radix-ui/react-slot`: ^1.1.1 - Component primitives
- `clsx`: ^2.1.1 - Conditional className utility

### Analytics & Performance

- `@vercel/analytics`: ^1.5.0 - Vercel analytics
- `@vercel/speed-insights`: ^1.2.0 - Performance monitoring

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
│   │   ├── new-user/      # New user setup (requires auth)
│   │   └── settings/      # User settings page
│   ├── globals.css        # Global styles
│   ├── layout.tsx         # Root layout
│   ├── not-found.tsx      # 404 page
│   └── providers.tsx      # Context providers
├── components/            # Reusable components
│   ├── Card.tsx           # Property card component
│   ├── Carousel.tsx       # Image carousel
│   ├── Form.tsx           # Form components
│   ├── Map.tsx            # Google Maps integration
│   ├── Navbar.tsx         # Navigation bar
│   ├── ProtectedRoute.tsx # Authentication protection wrapper
│   └── ...                # Other components
├── endpoints/             # API endpoint configurations
├── hooks/                 # Custom React hooks
├── interfaces/            # TypeScript interfaces
└── utils/                 # Utility functions
```

## 🎨 UI Components

### Core Components

- `Navbar`: Main navigation component with authentication state
- `Map`: Interactive Google Maps integration with property markers
- `HousingsList`: Display of housing listings with filtering
- `HousingSearch`: Advanced search functionality
- `Form`: Reusable form components with validation
- `Carousel`: Image carousel for property photos
- `VerificationCode`: Email verification component
- `Loading`: Loading state component
- `Footer`: Page footer with links and information

### Context Providers

- `GlobalContext`: Global state management for user data
- `GoogleMapContext`: Google Maps state management

## 🚀 Getting Started

1. **Prerequisites**
   - Node.js 18+ (recommended: Node.js 20+)
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
   # Start development server with Turbopack
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

The project uses Jest and React Testing Library for comprehensive testing:

```bash
# Run tests
npm run test

# Run tests in watch mode
npm run test -- --watch

# Run tests with coverage
npm run test -- --coverage
```

## 🔧 Configuration

Key configuration files:

- `next.config.ts`: Next.js configuration with Turbopack
- `tailwind.config.ts`: Tailwind CSS configuration with DaisyUI
- `jest.config.ts`: Jest testing configuration
- `tsconfig.json`: TypeScript configuration
- `eslint.config.mjs`: ESLint configuration
- `postcss.config.mjs`: PostCSS configuration

## 📝 Environment Variables

Required environment variables:

- `NEXT_PUBLIC_GOOGLE_MAPS_API_KEY`: Google Maps API key
- `NEXT_PUBLIC_API_URL`: Backend API URL

## 🚀 Deployment

### Automated Deployment

- **Vercel**: Automatic deployments on every push to production branch
- **GitHub Integration**: Seamless CI/CD pipeline
- **Preview Deployments**: Automatic preview deployments for pull requests

### Manual Deployment

- Build the application: `npm run build`
- Deploy to Vercel or other hosting platforms
- Configure environment variables in deployment platform

## 🔒 Security Features

- JWT token-based authentication
- Protected routes with authentication guards
- Secure API communication with backend
- Environment variable protection

## 📱 Responsive Design

- Mobile-first approach
- Optimized for all screen sizes
- Touch-friendly interactions
- Progressive Web App capabilities

## 👥 Authors

- Argel Hernandez Amaya

## 📞 Support

For support, please contact the project maintainers or open an issue in the repository.
