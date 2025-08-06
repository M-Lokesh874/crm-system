# 🚀 Deployment Guide

## Frontend Deployment (Vercel/Netlify)

### Option 1: Vercel (Recommended)

1. **Install Vercel CLI**
   ```bash
   npm i -g vercel
   ```

2. **Deploy to Vercel**
   ```bash
   cd frontend
   vercel --prod
   ```

3. **Configure Environment Variables**
   - Go to Vercel Dashboard
   - Add environment variables:
     - `NEXT_PUBLIC_API_URL` = Your backend API URL
     - `NEXT_PUBLIC_AUTH_URL` = Your auth service URL

### Option 2: Netlify

1. **Build the project**
   ```bash
   cd frontend
   npm run build
   ```

2. **Deploy to Netlify**
   ```bash
   netlify deploy --prod --dir=out
   ```

## Backend Deployment (Railway/Render)

### Option 1: Railway

1. **Install Railway CLI**
   ```bash
   npm i -g @railway/cli
   ```

2. **Deploy each service**
   ```bash
   # Auth Service
   cd backend/auth-service
   railway up

   # Customer Service
   cd backend/customer-service
   railway up

   # Notification Service
   cd backend/notification-service
   railway up
   ```

### Option 2: Render

1. **Connect GitHub Repository**
2. **Create new Web Service for each microservice**
3. **Configure environment variables**
4. **Set build command**: `mvn clean install`
5. **Set start command**: `mvn spring-boot:run`

## Environment Variables

### Frontend (.env.local)
```env
NEXT_PUBLIC_API_URL=https://your-api-gateway-url.com
NEXT_PUBLIC_AUTH_URL=https://your-auth-service-url.com
```

### Backend (Railway/Render Environment Variables)
```env
SPRING_DATASOURCE_URL=jdbc:postgresql://your-db-url:5432/crm_db
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=your-password
RABBITMQ_HOST=your-rabbitmq-url
RABBITMQ_PORT=5672
RABBITMQ_USERNAME=guest
RABBITMQ_PASSWORD=guest
JWT_SECRET=your-jwt-secret-key
```

## Database Setup

### PostgreSQL (Railway/Render)
1. Create PostgreSQL database
2. Get connection string
3. Update environment variables

### RabbitMQ (Railway/Render)
1. Create RabbitMQ instance
2. Get connection details
3. Update environment variables

## Deployment Checklist

- [ ] Frontend builds successfully
- [ ] Backend services start without errors
- [ ] Database connection works
- [ ] RabbitMQ connection works
- [ ] Environment variables configured
- [ ] CORS settings updated for production URLs
- [ ] SSL certificates configured
- [ ] Domain names configured

## Production Considerations

1. **Security**
   - Use strong JWT secrets
   - Enable HTTPS
   - Configure CORS properly
   - Use environment variables for secrets

2. **Performance**
   - Enable compression
   - Configure caching
   - Use CDN for static assets

3. **Monitoring**
   - Set up logging
   - Configure health checks
   - Monitor application metrics

## Troubleshooting

### Common Issues

1. **CORS Errors**
   - Update CORS configuration with production URLs
   - Check environment variables

2. **Database Connection**
   - Verify connection string
   - Check credentials
   - Ensure database is accessible

3. **Build Failures**
   - Check Node.js version
   - Verify all dependencies
   - Check for TypeScript errors

## Support

For deployment issues, check:
- Platform documentation (Vercel, Railway, Render)
- Application logs
- Environment variable configuration
- Network connectivity 