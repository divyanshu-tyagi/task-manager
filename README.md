# Task Manager API

![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.0-green)
![Kotlin](https://img.shields.io/badge/Kotlin-2.1-purple)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-blue)
![JWT](https://img.shields.io/badge/Auth-JWT-orange)
![AI](https://img.shields.io/badge/AI-Gemini-red)

A production-ready REST API for managing tasks with JWT authentication and AI-powered features.

🔗 **Live Demo:** [Swagger UI](https://your-app.railway.app/swagger-ui/index.html)

---

## Features

- 🔐 **JWT Authentication** — secure register and login with BCrypt password hashing
- ✅ **Task Management** — full CRUD with status and priority tracking
- 🔍 **Smart Filtering** — filter by status, priority, and keyword search
- 📄 **Pagination** — paginated results for scalable data retrieval
- 🤖 **AI Integration** — Gemini AI powered priority suggestion and description enhancement
- 🛡️ **Authorization** — users can only access their own tasks
- 📖 **Swagger UI** — fully interactive API documentation
- 🧪 **Unit Tested** — core business logic tested with Mockito

---

## Tech Stack

| Layer | Technology |
|-------|-----------|
| Language | Kotlin 2.1 |
| Framework | Spring Boot 4.0 |
| Database | PostgreSQL 16 |
| Migrations | Flyway |
| Authentication | JWT (jjwt 0.12.6) |
| AI | Google Gemini API |
| Documentation | Swagger / OpenAPI 3 |
| Testing | JUnit 5 + Mockito |
| Deployment | Railway |

---

## Project Structure
```
src/main/kotlin/com/taskmanager/
├── api/
│   ├── dto/          # Request/Response DTOs
│   ├── AuthController.kt
│   ├── TaskController.kt
│   ├── AiController.kt
│   └── GlobalExceptionHandler.kt
├── config/
│   ├── SecurityConfig.kt
│   └── SwaggerConfig.kt
├── domain/
│   ├── User.kt
│   └── Task.kt
├── repository/
│   ├── UserRepository.kt
│   └── TaskRepository.kt
├── security/
│   ├── JwtService.kt
│   ├── JwtAuthFilter.kt
│   ├── AuthUtils.kt
│   └── CustomUserDetailsService.kt
└── service/
    ├── TaskService.kt
    ├── UserService.kt
    └── GeminiService.kt
```

---

## Getting Started

### Prerequisites
- Java 21
- Docker Desktop
- Gemini API key (free at [aistudio.google.com](https://aistudio.google.com))

### Run Locally

1. **Clone the repository**
```bash
   git clone https://github.com/yourusername/task-manager-api.git
   cd task-manager-api
```

2. **Start PostgreSQL**
```bash
   docker-compose up -d
```

3. **Set environment variables** in IntelliJ Run Configuration:
```
   JWT_SECRET=your-secret-key-minimum-32-characters
   GEMINI_API_KEY=your-gemini-api-key
```

4. **Run the application**
```bash
   ./gradlew bootRun
```

5. **Open Swagger UI**
```
   http://localhost:8080/swagger-ui/index.html
```

---

## API Endpoints

### Authentication (Public)
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/auth/register` | Register a new user |
| POST | `/api/auth/login` | Login and receive JWT token |

### Tasks (Requires Bearer Token)
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/tasks` | Get my tasks (paginated) |
| GET | `/api/tasks?search=bug` | Search tasks by title |
| GET | `/api/tasks?status=TODO` | Filter by status |
| GET | `/api/tasks?priority=HIGH` | Filter by priority |
| GET | `/api/tasks?page=0&size=10` | Paginate results |
| GET | `/api/tasks/{id}` | Get task by ID |
| POST | `/api/tasks` | Create a new task |
| PUT | `/api/tasks/{id}` | Update a task |
| DELETE | `/api/tasks/{id}` | Delete a task |

### AI Features (Requires Bearer Token)
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/ai/suggest-priority` | AI suggests task priority |
| POST | `/api/ai/tasks/{id}/enhance` | AI enhances task description |

---

## Security

- Passwords hashed with **BCrypt** — never stored as plain text
- **JWT tokens** expire after 24 hours
- All secrets loaded from **environment variables** — never hardcoded
- Users can only access **their own tasks** — enforced at service layer
- **Stateless** authentication — no server-side sessions

---

## Environment Variables

| Variable | Description | Required |
|----------|-------------|----------|
| `JWT_SECRET` | Secret key for JWT signing (min 32 chars) | ✅ Yes |
| `JWT_EXPIRATION` | Token expiry in ms (default: 86400000) | No |
| `GEMINI_API_KEY` | Google Gemini API key | ✅ Yes |
| `DATABASE_URL` | PostgreSQL connection URL | ✅ Production |
| `DATABASE_USERNAME` | Database username | ✅ Production |
| `DATABASE_PASSWORD` | Database password | ✅ Production |

---

## Running Tests
```bash
# Run all tests
./gradlew test

# Run unit tests only
./gradlew test --tests "com.taskmanager.service.TaskServiceTest"

# View test report
open build/reports/tests/test/index.html
```

---

## License
MIT