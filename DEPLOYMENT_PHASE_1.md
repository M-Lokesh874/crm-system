# 🚀 CRM System - Phase 1 Deployment Guide

## 📋 Deployment Strategy: Core Services First

### **Services to Deploy:**
1. **Auth Service** - Authentication & Authorization
2. **Customer Service** - Customer Management
3. **Discovery Server** - Service Registry (for service discovery)

### **Services to Deploy Later:**
- API Gateway (currently having issues)
- Notification Service (excluded for now)
- Sales Service (not implemented yet)
- Task Service (not implemented yet)

---

## 🎯 Phase 1 Deployment Plan

### **Backend Services (Render)**
1. **Discovery Server** - Service registry for microservices
2. **Auth Service** - User authentication & JWT tokens
3. **Customer Service** - Customer CRUD operations

### **Frontend (Vercel)**
- React/Next.js application
- Direct connection to Auth & Customer services

---

## 🔧 Pre-Deployment Checklist

### ✅ Completed:
- [x] Redis configuration removed
- [x] Notification service excluded
- [x] Auth service tested and working
- [x] Customer service tested and working
- [x] Production profiles configured
- [x] Render deployment files prepared

### 📝 To Do:
- [ ] Push code to GitHub
- [ ] Deploy Discovery Server to Render
- [ ] Deploy Auth Service to Render
- [ ] Deploy Customer Service to Render
- [ ] Update frontend API URLs to point to deployed services
- [ ] Deploy frontend to Vercel

---

## 🌐 Service URLs (After Deployment)

### **Backend Services:**
- **Discovery Server**: `https://crm-discovery-service.onrender.com`
- **Auth Service**: `https://crm-auth-service.onrender.com`
- **Customer Service**: `https://crm-customer-service.onrender.com`

### **Frontend:**
- **CRM Application**: `https://crm-system.vercel.app`

---

## 🔄 API Endpoints

### **Auth Service:**
- `POST /auth/register` - User registration
- `POST /auth/login` - User login
- `GET /auth/profile` - Get user profile
- `POST /auth/logout` - User logout

### **Customer Service:**
- `GET /customers` - Get all customers
- `POST /customers` - Create new customer
- `GET /customers/{id}` - Get customer by ID
- `PUT /customers/{id}` - Update customer
- `DELETE /customers/{id}` - Delete customer

---

## 📊 Database Setup

### **PostgreSQL Database (Render):**
- **Database Name**: `crm_db`
- **Username**: `crm_user`
- **Tables**: `users`, `customers`

---

## 🚀 Deployment Steps

### **Step 1: Push to GitHub**
```bash
git add .
git commit -m "Phase 1: Core services ready for deployment"
git push origin main
```

### **Step 2: Deploy Backend Services**
1. Deploy Discovery Server to Render
2. Deploy Auth Service to Render
3. Deploy Customer Service to Render

### **Step 3: Update Frontend Configuration**
Update API URLs in frontend to point to deployed services

### **Step 4: Deploy Frontend**
Deploy React app to Vercel

---

## ✅ Success Criteria

- [ ] Users can register and login
- [ ] Users can create, read, update, delete customers
- [ ] Dashboard shows customer statistics
- [ ] All services are accessible via public URLs
- [ ] Frontend connects successfully to backend services

---

## 🔮 Phase 2 (Future)

- Fix and deploy API Gateway
- Add notification service
- Implement sales and task services
- Add more advanced features

---

**Ready to proceed with Phase 1 deployment! 🎯** 