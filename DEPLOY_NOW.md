# 🚀 DEPLOY CRM SYSTEM TODAY

## Quick Deployment Checklist

### 1. Frontend Deployment (Vercel) ✅
- [x] Frontend code is ready
- [x] Next.js config optimized
- [x] API URL will be updated after backend deployment

**Deploy to Vercel:**
1. Go to [vercel.com](https://vercel.com)
2. Connect your GitHub repository
3. Deploy the `crm-system/frontend` directory
4. Set environment variable: `NEXT_PUBLIC_API_URL=https://your-gateway-url.onrender.com`

### 2. Backend Services Deployment (Render)

#### Step 1: Create PostgreSQL Database
1. Go to [render.com](https://render.com)
2. Create new PostgreSQL service
3. Note down: `DATABASE_URL`, `DB_HOST`, `DB_NAME`, `DB_USER`, `DB_PASSWORD`

#### Step 2: Create Redis Instance
1. Create new Redis service on Render
2. Note down: `REDIS_HOST`, `REDIS_PORT`, `REDIS_PASSWORD`

#### Step 3: Deploy Services

**Auth Service:**
```bash
# Environment Variables for Auth Service:
SPRING_DATASOURCE_URL=${DATABASE_URL}
SPRING_DATASOURCE_USERNAME=${DB_USER}
SPRING_DATASOURCE_PASSWORD=${DB_PASSWORD}
JWT_SECRET=your-super-secret-jwt-key-2024
REDIS_HOST=${REDIS_HOST}
REDIS_PORT=${REDIS_PORT}
REDIS_PASSWORD=${REDIS_PASSWORD}
```

**Customer Service:**
```bash
# Environment Variables for Customer Service:
SPRING_DATASOURCE_URL=${DATABASE_URL}
SPRING_DATASOURCE_USERNAME=${DB_USER}
SPRING_DATASOURCE_PASSWORD=${DB_PASSWORD}
REDIS_HOST=${REDIS_HOST}
REDIS_PORT=${REDIS_PORT}
REDIS_PASSWORD=${REDIS_PASSWORD}
```

**Notification Service:**
```bash
# Environment Variables for Notification Service:
SPRING_DATASOURCE_URL=${DATABASE_URL}
SPRING_DATASOURCE_USERNAME=${DB_USER}
SPRING_DATASOURCE_PASSWORD=${DB_PASSWORD}
REDIS_HOST=${REDIS_HOST}
REDIS_PORT=${REDIS_PORT}
REDIS_PASSWORD=${REDIS_PASSWORD}
SPRING_MAIL_USERNAME=your-email@gmail.com
SPRING_MAIL_PASSWORD=your-app-password
```

### 3. Update Frontend API URL
After all services are deployed, update the frontend environment variable:
- `NEXT_PUBLIC_API_URL=https://your-gateway-url.onrender.com`

### 4. Test the System
1. Register a new user
2. Login with the user
3. Create a customer
4. Check if welcome email is sent

## 🎯 Current Status
- ✅ Frontend: Ready for Vercel deployment
- ✅ Backend: All services configured for Render
- ✅ Redis: Event messaging configured for production
- ✅ Database: PostgreSQL ready
- ✅ Email: SMTP configured for notifications

## 🚨 Important Notes
1. **Redis vs RabbitMQ**: We're using Redis for production (Render doesn't support RabbitMQ)
2. **Profiles**: Services use `prod` profile for production deployment
3. **Event Messaging**: Welcome emails will work via Redis pub/sub
4. **CORS**: Configured for cross-origin requests

## 📞 Need Help?
If you encounter any issues during deployment, check:
1. Service logs on Render
2. Environment variables are set correctly
3. Database connection is working
4. Redis connection is established

**Ready to deploy! 🚀** 