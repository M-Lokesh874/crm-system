# 🚀 CRM System - Render Deployment Guide

## 📋 Prerequisites
- Render account (free tier)
- GitHub repository connected to Render
- Gmail account for SMTP (optional, for email notifications)

## 🗄️ Step 1: Set up PostgreSQL Database

1. **Go to [render.com](https://render.com)** and sign in
2. **Click "New +"** → **"PostgreSQL"**
3. **Configure:**
   - **Name**: `crm-postgres-db`
   - **Database**: `crm_db`
   - **User**: `crm_user`
   - **Region**: Choose closest to you
   - **Plan**: Free

4. **After creation, note down:**
   - **Internal Database URL**: `postgresql://crm_user:password@host:port/crm_db`
   - **External Database URL**: For local development
   - **Password**: Auto-generated

## 🔴 Step 2: Set up Redis

1. **Click "New +"** → **"Redis"**
2. **Configure:**
   - **Name**: `crm-redis-cache`
   - **Region**: Same as PostgreSQL
   - **Plan**: Free

3. **Note down:**
   - **Redis URL**: `redis://username:password@host:port`

## 🔧 Step 3: Deploy Auth Service

1. **Go to GitHub** and ensure your repository is up to date
2. **In Render Dashboard:**
   - Click **"New +"** → **"Web Service"**
   - **Connect your GitHub repository**
   - **Select the repository**

3. **Configure Auth Service:**
   - **Name**: `crm-auth-service`
   - **Root Directory**: `backend/auth-service`
   - **Environment**: `Java`
   - **Build Command**: `mvn clean install -DskipTests`
   - **Start Command**: `mvn spring-boot:run`

4. **Environment Variables:**
   ```
   SPRING_PROFILES_ACTIVE=prod
   SPRING_DATASOURCE_URL=<PostgreSQL Internal URL>
   SPRING_DATASOURCE_USERNAME=crm_user
   SPRING_DATASOURCE_PASSWORD=<PostgreSQL Password>
   JWT_SECRET=crmAuthSecretKeyForJWTTokenGeneration2024
   JWT_EXPIRATION=86400000
   REDIS_URL=<Redis URL>
   REDIS_HOST=<Redis Host>
   REDIS_PORT=6379
   REDIS_PASSWORD=<Redis Password>
   ```

5. **Click "Create Web Service"**

## 👥 Step 4: Deploy Customer Service

1. **Create another Web Service**
2. **Configure:**
   - **Name**: `crm-customer-service`
   - **Root Directory**: `backend/customer-service`
   - **Environment**: `Java`
   - **Build Command**: `mvn clean install -DskipTests`
   - **Start Command**: `mvn spring-boot:run`

3. **Environment Variables:**
   ```
   SPRING_PROFILES_ACTIVE=prod
   SPRING_DATASOURCE_URL=<PostgreSQL Internal URL>
   SPRING_DATASOURCE_USERNAME=crm_user
   SPRING_DATASOURCE_PASSWORD=<PostgreSQL Password>
   REDIS_URL=<Redis URL>
   REDIS_HOST=<Redis Host>
   REDIS_PORT=6379
   REDIS_PASSWORD=<Redis Password>
   ```

## 📧 Step 5: Deploy Notification Service

1. **Create another Web Service**
2. **Configure:**
   - **Name**: `crm-notification-service`
   - **Root Directory**: `backend/notification-service`
   - **Environment**: `Java`
   - **Build Command**: `mvn clean install -DskipTests`
   - **Start Command**: `mvn spring-boot:run`

3. **Environment Variables:**
   ```
   SPRING_PROFILES_ACTIVE=prod
   SPRING_DATASOURCE_URL=<PostgreSQL Internal URL>
   SPRING_DATASOURCE_USERNAME=crm_user
   SPRING_DATASOURCE_PASSWORD=<PostgreSQL Password>
   REDIS_URL=<Redis URL>
   REDIS_HOST=<Redis Host>
   REDIS_PORT=6379
   REDIS_PASSWORD=<Redis Password>
   SPRING_MAIL_HOST=smtp.gmail.com
   SPRING_MAIL_PORT=587
   SPRING_MAIL_USERNAME=<Your Gmail>
   SPRING_MAIL_PASSWORD=<Gmail App Password>
   SPRING_MAIL_PROPERTIES_MAIL_SMTP_AUTH=true
   SPRING_MAIL_PROPERTIES_MAIL_SMTP_STARTTLS_ENABLE=true
   ```

## 🔗 Step 6: Update Frontend Environment Variables

1. **Go to Vercel Dashboard**
2. **Select your frontend project**
3. **Go to Settings** → **Environment Variables**
4. **Add:**
   ```
   NEXT_PUBLIC_API_URL=https://crm-customer-service.onrender.com
   NEXT_PUBLIC_AUTH_URL=https://crm-auth-service.onrender.com
   ```

## 🧪 Step 7: Test the Complete System

1. **Test Auth Service:**
   ```bash
   curl -X POST https://crm-auth-service.onrender.com/auth/register \
     -H "Content-Type: application/json" \
     -d '{"username":"test","password":"test123","email":"test@example.com","fullName":"Test User"}'
   ```

2. **Test Customer Service:**
   ```bash
   curl https://crm-customer-service.onrender.com/api/v1/customers
   ```

3. **Test Frontend:**
   - Visit your Vercel URL
   - Try registering a new user
   - Test login functionality
   - Test customer management

## 🔧 Troubleshooting

### Common Issues:
1. **Build Failures**: Check Maven dependencies
2. **Database Connection**: Verify PostgreSQL URL and credentials
3. **Redis Connection**: Verify Redis URL and credentials
4. **CORS Issues**: Check frontend environment variables
5. **Email Not Working**: Verify Gmail app password

### Logs:
- Check Render service logs for errors
- Verify environment variables are set correctly
- Test database connectivity

## 🎯 Success Indicators

✅ **All services deploy successfully**
✅ **Database tables are created automatically**
✅ **Frontend can connect to backend**
✅ **User registration works**
✅ **Customer management works**
✅ **Email notifications work (optional)**

## 🚀 Next Steps

After successful deployment:
1. **Monitor performance**
2. **Set up custom domain (optional)**
3. **Configure monitoring and alerts**
4. **Scale up as needed**

---

**🎉 Congratulations! Your CRM system is now production-ready!** 