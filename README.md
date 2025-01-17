# Meetup: Connections in a Swipe

## Overview
Meetup is a dating app that allows users to find matches based on preferences like age range, gender, and interests. It features a modern React-based frontend integrated with a robust Spring Boot backend.

## Features
- **Google Login**: Secure user authentication with Spring Security and JWT.
- **Preferences Management**: Set preferences for age range, gender, interests, and location.
- **Matches Page**: View a paginated list of matched profiles.
- **Responsive Design**: Optimized for both desktop and mobile devices.

## Technologies Used

### Frontend
- **React**: Used for building a dynamic, component-based user interface.
- **Vite**: A modern frontend build tool for fast development and optimized production builds.
- **Axios**: Handles HTTP requests to interact with backend APIs seamlessly.
- **Material-UI**: Provides pre-styled components for a responsive and modern UI.
- **Tailwind CSS**: A utility-first CSS framework for rapid custom designs.

### Backend
- **Spring Boot**: Manages the backend logic and serves REST APIs.
- **Spring Security**: Ensures secure authentication and authorization using JWT.
- **Hibernate**: Simplifies database interaction with ORM capabilities.
- **PostgreSQL**: A robust relational database to store user and preference data.
- **HikariCP**: A high-performance JDBC connection pool used with Spring Boot.

### Additional Tools
- **Google OAuth**: Integrates Google Login for a smooth authentication experience.
- **Maven**: Manages project dependencies and builds the application.
- **JWT (JSON Web Token)**: Provides secure token-based authentication.

## Pages Overview
1. **Login Page**:
   - Authenticate users using Google Login.
   - Navigate to the Preferences page on successful login.

2. **Preferences Page**:
   - Configure your preferences for matching (age range, gender, interests, and location).
   - Save preferences to the backend for personalized matches.

3. **Matches Page**:
   - Displays a list of users who match your preferences.
   - Highlight common interests and user details.
   - Paginated for smooth browsing.

## Database Prerequisites
1. **PostgreSQL Setup**:
   - Ensure PostgreSQL is installed and running.
   - Create a database named `meetup`.
   - Update `application.properties` in the backend project:
     ```properties
     spring.datasource.url=jdbc:postgresql://localhost:5432/meetup
     spring.datasource.username=your-username
     spring.datasource.password=your-password
     spring.jpa.hibernate.ddl-auto=update
     ```

## Running the Application
### Backend Setup
1. Start the Spring Boot application:
   ```bash
   mvn spring-boot:run
   ```
2. Backend runs on `http://localhost:8080`.

### Frontend Setup
1. React app is built and placed in the `src/main/resources/static` folder.
2. Access the app directly via `http://localhost:8080`.

### How to Use
1. Navigate to `http://localhost:8080`.
2. Login with Google credentials.
3. Set preferences on the Preferences page.
4. View matches on the Matches page.

## API Endpoints
| Endpoint                     | Method | Description                              |
|------------------------------|--------|------------------------------------------|
| `/api/auth/google`           | POST   | Handle Google Login and return JWT.      |
| `/api/preferences`           | GET    | Fetch user preferences.                  |
| `/api/preferences`           | POST   | Add or update preferences.               |
| `/api/matching/matches`      | GET    | Fetch paginated matches.                 |

## Improvements
- **Real-Time Chat**: Enable communication between matched users.
- **Super Like Feature**: Add a premium feature for special matches.
- **Analytics Dashboard**: Track user engagement and match success rates.

## Conclusion
This application is designed for simplicity and scalability, providing an easy-to-use interface and efficient backend logic for matching users. Feel free to explore and test all its features!
