# CRM Frontend - React.js Application

## 🚀 Modern React.js Frontend for CRM System

This is a modern React.js frontend built with Next.js 14, TypeScript, and Tailwind CSS for the CRM system.

## 🛠️ Tech Stack

- **React 18** - Modern React with hooks
- **Next.js 14** - Full-stack React framework with App Router
- **TypeScript** - Type safety and better developer experience
- **Tailwind CSS** - Utility-first CSS framework
- **React Query** - Server state management
- **React Hook Form** - Form handling with validation
- **Lucide React** - Beautiful icons
- **Shadcn/ui** - Reusable UI components

## 📁 Project Structure

```
frontend/
├── src/
│   ├── app/                 # Next.js App Router
│   │   ├── globals.css      # Global styles
│   │   ├── layout.tsx       # Root layout
│   │   └── page.tsx         # Home page
│   ├── components/          # Reusable components
│   │   ├── ui/             # Base UI components
│   │   └── providers.tsx   # App providers
│   ├── lib/                # Utility functions
│   ├── hooks/              # Custom React hooks
│   ├── types/              # TypeScript type definitions
│   └── utils/              # Helper functions
├── public/                 # Static assets
├── package.json            # Dependencies
├── tailwind.config.js      # Tailwind configuration
├── tsconfig.json           # TypeScript configuration
└── next.config.js          # Next.js configuration
```

## 🚀 Getting Started

### Prerequisites

- Node.js 18+ 
- npm or yarn

### Installation

1. **Navigate to frontend directory:**
   ```bash
   cd crm-system/frontend
   ```

2. **Install dependencies:**
   ```bash
   npm install
   ```

3. **Start development server:**
   ```bash
   npm run dev
   ```

4. **Open your browser:**
   ```
   http://localhost:3000
   ```

## 🎨 Features

### ✅ Implemented
- **Modern Landing Page** - Beautiful hero section with features
- **Responsive Design** - Mobile-first approach
- **TypeScript** - Full type safety
- **Tailwind CSS** - Modern styling
- **Component Library** - Reusable UI components
- **React Query Setup** - Ready for API integration

### 🚧 Coming Soon
- **Authentication Pages** - Login/Register forms
- **Dashboard** - Main CRM interface
- **Customer Management** - CRUD operations
- **Sales Pipeline** - Visual sales tracking
- **Task Management** - Todo and reminders
- **Analytics** - Charts and reports

## 🔧 Development

### Available Scripts

```bash
npm run dev          # Start development server
npm run build        # Build for production
npm run start        # Start production server
npm run lint         # Run ESLint
npm run type-check   # Run TypeScript check
```

### Key Concepts for Java Developers

#### 1. **Components (Like Java Classes)**
```tsx
// Similar to Java class
export function CustomerCard({ customer }: { customer: Customer }) {
  return <div>{customer.name}</div>
}
```

#### 2. **Hooks (Like Utility Methods)**
```tsx
// Similar to Java utility methods
function useCustomer(id: string) {
  return useQuery({
    queryKey: ['customer', id],
    queryFn: () => fetchCustomer(id)
  })
}
```

#### 3. **Context (Like Dependency Injection)**
```tsx
// Similar to Spring's @Autowired
const AuthContext = createContext<AuthContextType | null>(null)
```

#### 4. **TypeScript (Like Java Types)**
```tsx
// Similar to Java interfaces
interface Customer {
  id: string
  name: string
  email: string
}
```

## 🎯 Next Steps

1. **Authentication System**
   - Login/Register forms
   - JWT token management
   - Protected routes

2. **Dashboard Layout**
   - Sidebar navigation
   - Header with user info
   - Breadcrumb navigation

3. **API Integration**
   - Connect to backend services
   - Error handling
   - Loading states

4. **CRUD Operations**
   - Customer management
   - Lead management
   - Task management

## 🎨 Design System

### Colors
- **Primary**: Blue (#3B82F6)
- **Secondary**: Gray (#6B7280)
- **Success**: Green (#10B981)
- **Warning**: Yellow (#F59E0B)
- **Error**: Red (#EF4444)

### Typography
- **Font**: Inter (Google Fonts)
- **Headings**: Bold, large sizes
- **Body**: Regular weight, readable

### Components
- **Buttons**: Multiple variants (primary, secondary, outline)
- **Cards**: Content containers with shadows
- **Forms**: Input fields with validation
- **Badges**: Status indicators

## 🔗 Backend Integration

The frontend is configured to communicate with the backend services:

- **API Gateway**: `http://localhost:8080`
- **Auth Service**: `http://localhost:8085`
- **Customer Service**: `http://localhost:8081`
- **Sales Service**: `http://localhost:8082`
- **Task Service**: `http://localhost:8083`

## 🚀 Deployment

### Development
```bash
npm run dev
```

### Production
```bash
npm run build
npm run start
```

### Docker (Coming Soon)
```bash
docker build -t crm-frontend .
docker run -p 3000:3000 crm-frontend
```

## 📚 Learning Resources

### React.js Concepts
- [React Documentation](https://react.dev/)
- [Next.js Documentation](https://nextjs.org/docs)
- [TypeScript Handbook](https://www.typescriptlang.org/docs/)

### Styling
- [Tailwind CSS](https://tailwindcss.com/docs)
- [Shadcn/ui](https://ui.shadcn.com/)

### State Management
- [React Query](https://tanstack.com/query/latest)
- [React Hook Form](https://react-hook-form.com/)

## 🤝 Contributing

1. Follow the existing code structure
2. Use TypeScript for all new code
3. Write meaningful component names
4. Add proper error handling
5. Test your changes

## 📝 License

This project is part of the CRM System built for learning purposes.

---

**Ready to build amazing user experiences! 🚀** 