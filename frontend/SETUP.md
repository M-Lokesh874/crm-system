# 🚀 Quick Setup Guide - React Frontend

## Prerequisites
- Node.js 18+ installed
- Backend services running (see main README)

## Quick Start

### 1. Install Dependencies
```bash
cd crm-system/frontend
npm install
```

### 2. Start Development Server
```bash
npm run dev
```

### 3. Open Browser
```
http://localhost:3000
```

## 🎯 What You'll See

- **Beautiful Landing Page** with modern design
- **Responsive Layout** that works on mobile/desktop
- **Feature Cards** showcasing CRM capabilities
- **Call-to-Action** buttons for registration

## 🛠️ Development

### Key Files to Explore:
- `src/app/page.tsx` - Main landing page
- `src/components/ui/` - Reusable UI components
- `src/app/layout.tsx` - Root layout with providers
- `tailwind.config.js` - Styling configuration

### Available Scripts:
```bash
npm run dev          # Development server
npm run build        # Production build
npm run lint         # Code linting
npm run type-check   # TypeScript checking
```

## 🔗 Backend Integration

The frontend is configured to connect to your backend services:
- API Gateway: `http://localhost:8080`
- Auth Service: `http://localhost:8085`
- Customer Service: `http://localhost:8081`
- Sales Service: `http://localhost:8082`
- Task Service: `http://localhost:8083`

## 🎨 Customization

### Colors
Edit `tailwind.config.js` to change the color scheme:
```js
colors: {
  primary: {
    DEFAULT: "hsl(var(--primary))",
    // Your custom colors here
  }
}
```

### Components
All UI components are in `src/components/ui/` and can be customized.

## 🚀 Next Steps

1. **Authentication Pages** - Login/Register forms
2. **Dashboard** - Main CRM interface
3. **API Integration** - Connect to backend services
4. **CRUD Operations** - Customer/Lead/Task management

---

**Ready to build amazing user experiences! 🎉** 