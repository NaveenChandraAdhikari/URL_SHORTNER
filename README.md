# URL_SHORTNER
# URL Shortener

A full-stack URL shortener application built with **Spring Boot** (backend) and **React** (frontend). This project allows users to shorten long URLs, track click analytics, and manage their shortened links.

---

## Features

- **Shorten URLs**: Convert long URLs into short, easy-to-share links.
- **User Authentication**: Secure login and registration using **JWT** (JSON Web Tokens).
- **OAuth2 Login**: Sign in with **Google** or **GitHub** for a seamless experience.
- **Dashboard**: View and manage all your shortened URLs in one place.
- **Click Analytics**: Track the number of clicks and view detailed analytics for each shortened URL.
- **Customizable**: Set expiration dates or custom aliases for your shortened URLs (optional).

---

## Technologies Used

### Backend
- **Spring Boot**: REST API for URL shortening and user authentication.
- **Spring Security**: Secure endpoints and handle JWT-based authentication.
- **Spring Data JPA**: Manage database operations.
- **H2 Database**: In-memory database for development (can be replaced with MySQL/PostgreSQL).
- **OAuth2**: Integration with Google and GitHub for third-party login.

### Frontend
- **React**: Frontend framework for building the user interface.
- **React Router**: Handle routing and navigation.
- **Axios**: Make HTTP requests to the backend API.
- **Context API**: Manage global state (e.g., user authentication).
- **Tailwind CSS**: Styling and responsive design.

---
