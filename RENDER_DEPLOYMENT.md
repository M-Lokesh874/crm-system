# 🚀 Render Deployment Guide

## Prerequisites
- GitHub repository with your CRM system
- Render account (free tier available)

## Step 1: Set up PostgreSQL Database

1. **Go to Render Dashboard**
2. **Click "New" → "PostgreSQL"**
3. **Configure:**
   - **Name**: `crm-postgres`
   - **Database**: `crm_db`
   - **User**: `crm_user`
   - **Region**: Choose closest to you
4. **Click "Create Database"**
5. **Save the connection details**

## Step 2: Set up Redis (for RabbitMQ replacement)

1. **Go to Render Dashboard**
2. **Click "New" → "Redis"**
3. **Configure:**
   - **Name**: `crm-redis`
   - **Region**: Same as PostgreSQL
4. **Click "Create Redis"**
5. **Save the connection details**

## Step 3: Deploy Auth Service

1. **Go to Render Dashboard**
2. **Click "New" → "Web Service"**
3. **Connect your GitHub repository**
4. **Configure:**
   - **Name**: `crm-auth-service`
   - **Root Directory**: `backend/auth-service`
   - **Environment**: `Java`
   - **Build Command**: `mvn clean install -DskipTests`
   - **Start Command**: `mvn spring-boot:run`
   - **Port**: `8085`

5. **Add Environment Variables:**
   ```
   SPRING_PROFILES_ACTIVE=production
   SPRING_DATASOURCE_URL=<PostgreSQL connection string>
   SPRING_DATASOURCE_USERNAME=<PostgreSQL username>
   SPRING_DATASOURCE_PASSWORD=<PostgreSQL password>
   JWT_SECRET=<generate a strong secret>
   JWT_EXPIRATION=86400000
   RABBITMQ_HOST=<Redis host>
   RABBITMQ_PORT=6379
   RABBITMQ_USERNAME=<Redis username>
   RABBITMQ_PASSWORD=<Redis password>
   ```

6. **Click "Create Web Service"**

## Step 4: Deploy Customer Service

1. **Go to Render Dashboard**
2. **Click "New" → "Web Service"**
3. **Connect your GitHub repository**
4. **Configure:**
   - **Name**: `crm-customer-service`
   - **Root Directory**: `backend/customer-service`
   - **Environment**: `Java`
   - **Build Command**: `mvn clean install -DskipTests`
   - **Start Command**: `mvn spring-boot:run`
   - **Port**: `8081`

5. **Add Environment Variables:**
   ```
   SPRING_PROFILES_ACTIVE=production
   SPRING_DATASOURCE_URL=<PostgreSQL connection string>
   SPRING_DATASOURCE_USERNAME=<PostgreSQL username>
   SPRING_DATASOURCE_PASSWORD=<PostgreSQL password>
   RABBITMQ_HOST=<Redis host>
   RABBITMQ_PORT=6379
   RABBITMQ_USERNAME=<Redis username>
   RABBITMQ_PASSWORD=<Redis password>
   ```

6. **Click "Create Web Service"**

## Step 5: Deploy Notification Service

1. **Go to Render Dashboard**
2. **Click "New" → "Web Service"**
3. **Connect your GitHub repository**
4. **Configure:**
   - **Name**: `crm-notification-service`
   - **Root Directory**: `backend/notification-service`
   - **Environment**: `Java`
   - **Build Command**: `mvn clean install -DskipTests`
   - **Start Command**: `mvn spring-boot:run`
   - **Port**: `8082`

5. **Add Environment Variables:**
   ```
   SPRING_PROFILES_ACTIVE=production
   SPRING_DATASOURCE_URL=<PostgreSQL connection string>
   SPRING_DATASOURCE_USERNAME=<PostgreSQL username>
   SPRING_DATASOURCE_PASSWORD=<PostgreSQL password>
   RABBITMQ_HOST=<Redis host>
   RABBITMQ_PORT=6379
   RABBITMQ_USERNAME=<Redis username>
   RABBITMQ_PASSWORD=<Redis password>
   SPRING_MAIL_HOST=smtp.gmail.com
   SPRING_MAIL_PORT=587
   SPRING_MAIL_USERNAME=<your-email@gmail.com>
   SPRING_MAIL_PASSWORD=<your-app-password>
   ```

6. **Click "Create Web Service"**

## Step 6: Deploy Frontend to Vercel

1. **Install Vercel CLI:**
   ```bash
   npm i -g vercel
   ```

2. **Deploy:**
   ```bash
   cd frontend
   vercel --prod
   ```

3. **Configure Environment Variables in Vercel Dashboard:**
   ```
   NEXT_PUBLIC_API_URL=https://your-customer-service-url.onrender.com
   NEXT_PUBLIC_AUTH_URL=https://your-auth-service-url.onrender.com
   ```

## Step 7: Update CORS Configuration

Update your backend services to allow your Vercel domain:

```java
@Configuration
public class CorsConfig {
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(Arrays.asList(
            "https://your-app.vercel.app",
            "http://localhost:3000"
        ));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
```

## Step 8: Test Deployment

1. **Check all services are running**
2. **Test API endpoints**
3. **Test frontend functionality**
4. **Verify email notifications work**

## Troubleshooting

### Common Issues:

1. **Build Failures**
   - Check Java version (should be 21)
   - Verify Maven dependencies
   - Check for compilation errors

2. **Database Connection**
   - Verify PostgreSQL connection string
   - Check credentials
   - Ensure database is accessible

3. **CORS Errors**
   - Update CORS configuration with Vercel domain
   - Check environment variables

4. **Email Notifications**
   - Configure Gmail app password
   - Verify SMTP settings

## Environment Variables Reference

### PostgreSQL
```
SPRING_DATASOURCE_URL=jdbc:postgresql://host:port/database
SPRING_DATASOURCE_USERNAME=username
SPRING_DATASOURCE_PASSWORD=password
```

### Redis (RabbitMQ replacement)
```
RABBITMQ_HOST=redis-host
RABBITMQ_PORT=6379
RABBITMQ_USERNAME=username
RABBITMQ_PASSWORD=password
```

### JWT
```
JWT_SECRET=your-strong-secret-key
JWT_EXPIRATION=86400000
```

### Email (Gmail)
```
SPRING_MAIL_HOST=smtp.gmail.com
SPRING_MAIL_PORT=587
SPRING_MAIL_USERNAME=your-email@gmail.com
SPRING_MAIL_PASSWORD=your-app-password
```

## Cost Estimation (Free Tier)

- **PostgreSQL**: Free (up to 1GB)
- **Redis**: Free (up to 25MB)
- **Web Services**: Free (3 services, 750 hours/month each)
- **Vercel**: Free (up to 100GB bandwidth)

## Next Steps

1. **Monitor application logs**
2. **Set up custom domain**
3. **Configure SSL certificates**
4. **Set up monitoring and alerts**
5. **Scale up as needed** 