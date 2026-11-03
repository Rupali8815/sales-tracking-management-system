# Sales Tracking & Management System

A full stack web application to manage leads, deals, sales, targets and reports for a sales team, with JWT authentication and role-based access control.

**Tech stack:** React 18 (Vite) · Spring Boot 3 (Java 17) · Spring Security + JWT · Spring Data JPA / Hibernate · MySQL · Swagger (springdoc)

## Screenshots

| Login | Dashboard |
|---|---|
| ![Login](screenshots/login.png) | ![Dashboard](screenshots/dashboard.png) |

| Leads | Sales |
|---|---|
| ![Leads](screenshots/leads.png) | ![Sales](screenshots/sales.png) |

## Features

- **Authentication:** stateless JWT login, refresh token, and real logout (tokens issued before logout are rejected).
- **Role-based access control:** Super Admin, Admin, Manager, Sales Executive and Viewer, each with its own permissions.
- **Record-level ownership:** a Sales Executive sees and edits only their own leads and deals. Managers and Admins can see everything and re-assign.
- **Lead to Deal to Sale flow:** convert a lead into a deal, close it as won or lost, and record the sale (transactional, with duplicate-sale protection). Closed deals are locked.
- **Sales page:** payment status updates, plus monthly and yearly totals.
- **Targets and reports:** sales targets per user, with report charts (Recharts).
- **Audit logging:** who did what and when; lead activity history timeline.
- **API docs:** Swagger UI for all endpoints.

## Architecture

```
React (Axios + interceptor, protected routes)
        |  REST + JWT
Controller -> Service -> Repository -> MySQL
        |
Security filters (JWT authentication -> authorization -> CurrentUser)
```

- Layered architecture with DTOs and mappers.
- Method security using `@PreAuthorize`.
- Global exception handling (400, 403, 404, 409).

## Project structure

```
backend/    Spring Boot application (com.company.salestracker)
frontend/   React + Vite application
```

## Getting started

### Prerequisites
- Java 17, Maven
- Node.js 18+
- MySQL 8

### 1. Database
```sql
CREATE DATABASE sales_tracker;
```

### 2. Backend
Edit `backend/src/main/resources/application.properties` with your own MySQL username, password and a JWT secret, then run:
```bash
cd backend
mvn spring-boot:run
```
The API starts at `http://localhost:8080`. Swagger UI: `http://localhost:8080/swagger-ui/index.html`

### 3. Frontend
```bash
cd frontend
npm install
npm run dev
```
The app opens at `http://localhost:3000`.

## Roles and permissions

| Role | Access |
|---|---|
| Super Admin / Admin | Full access, user and role management, audit logs |
| Manager | All leads, deals and sales; can re-assign; targets and reports |
| Sales Executive | Own leads, deals and sales only |
| Viewer | Read-only |

## What I learned

- Designing JWT authentication and role/permission-based authorization in Spring Security.
- Enforcing business rules (ownership, locked records, duplicate-sale checks) in the service layer with transactions.
- Connecting a React app to a secured API, including silent token refresh.

## Known limitations / future work

- Logout revocation list is in memory (move it to Redis or a database table for multiple instances).
- Forgot-password flow is UI only.
- Add unit and integration tests (JUnit, Mockito).
- Docker setup and deployment.

## Author

**Rupali Yadav** · [LinkedIn](https://linkedin.com/in/rupali-yadav09) · rupaliy383@gmail.com
