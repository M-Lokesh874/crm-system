# 🚀 Modern CRM System

A comprehensive Customer Relationship Management system built with **Spring Boot Microservices** and **React.js/Next.js**.

## 🏗️ Architecture

### Backend (Microservices)
- **Auth Service** (Port: 8085) - Authentication & Authorization
- **Customer Service** (Port: 8081) - Customer Management
- **Notification Service** (Port: 8082) - Email & Notifications
- **Discovery Server** (Port: 8761) - Service Discovery
- **API Gateway** (Port: 8080) - Route Management

### Frontend
- **React.js/Next.js** (Port: 3000) - Modern UI with TypeScript
- **Tailwind CSS** - Utility-first styling
- **Radix UI** - Accessible components

## 🛠️ Tech Stack

### Backend
- **Spring Boot 3.2.5** - Main framework
- **Spring Cloud** - Microservices orchestration
- **PostgreSQL** - Primary database
- **RabbitMQ** - Message broker for events
- **JWT** - Authentication tokens
- **JPA/Hibernate** - ORM

### Frontend
- **Next.js 14** - React framework
- **TypeScript** - Type safety
- **Tailwind CSS** - Styling
- **React Hook Form** - Form management
- **Axios** - HTTP client

## 🚀 Quick Start

### Prerequisites
- Java 21
- Node.js 18+
- PostgreSQL
- RabbitMQ

### Backend Setup
```bash
# Start Discovery Server
cd backend/discovery-server
mvn spring-boot:run

# Start Auth Service
cd backend/auth-service
mvn spring-boot:run

# Start Customer Service
cd backend/customer-service
mvn spring-boot:run

# Start Notification Service
cd backend/notification-service
mvn spring-boot:run
```

### Frontend Setup
```bash
cd frontend
npm install
npm run dev
```

## 🌟 Features

### ✅ Implemented
- **User Authentication** - Register, Login, JWT tokens
- **Customer Management** - CRUD operations
- **Event-Driven Architecture** - Welcome emails on registration
- **Responsive UI** - Modern, mobile-friendly design
- **Real-time Dashboard** - Live customer statistics
- **Search & Filter** - Customer search functionality

### 🚧 In Progress
- **Sales Pipeline** - Leads and Opportunities
- **Task Management** - Task creation and tracking
- **Advanced Analytics** - Charts and reports
- **Real-time Notifications** - Live updates

## 📊 API Endpoints

### Auth Service
- `POST /auth/register` - User registration
- `POST /auth/login` - User login
- `GET /auth/profile` - Get user profile

### Customer Service
- `GET /api/v1/customers` - Get all customers
- `POST /api/v1/customers` - Create customer
- `PUT /api/v1/customers/{id}` - Update customer
- `DELETE /api/v1/customers/{id}` - Delete customer

## 🎨 UI/UX Features

- **Modern Design** - Clean, professional interface
- **Responsive Layout** - Works on all devices
- **Loading States** - Better user feedback
- **Form Validation** - Client-side validation
- **Success/Error Notifications** - User feedback
- **Search Functionality** - Find customers quickly

## 🚀 Deployment

### Frontend (Vercel/Netlify)
```bash
# Build the project
cd frontend
npm run build

# Deploy to Vercel
vercel --prod

# Or deploy to Netlify
netlify deploy --prod
```

### Backend (Railway/Render)
```bash
# Deploy to Railway
railway up

# Or deploy to Render
# Connect your GitHub repository
```

## 🔧 Environment Variables

### Frontend
```env
NEXT_PUBLIC_API_URL=http://localhost:8080
NEXT_PUBLIC_AUTH_URL=http://localhost:8085
```

### Backend
```env
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/crm_db
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=root
RABBITMQ_HOST=localhost
RABBITMQ_PORT=5672
```

## 📈 Project Status

- ✅ **Phase 1**: Basic authentication and customer management
- ✅ **Phase 2**: Event-driven architecture and notifications
- ✅ **Phase 3**: Modern UI/UX with responsive design
- 🚧 **Phase 4**: Additional microservices (Sales, Tasks)
- 📋 **Phase 5**: Advanced features and deployment

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Submit a pull request

## 📝 License

This project is licensed under the MIT License.

---

**Built with ❤️ using modern microservices architecture** 