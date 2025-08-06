// Authentication Types
export interface User {
  id: number;
  username: string;
  email: string;
  firstName: string;
  lastName: string;
  primaryRole: string;
  roles: string[];
  enabled: boolean;
  createdAt: string;
  updatedAt: string;
}

export interface LoginRequest {
  username: string;
  password: string;
}

export interface RegisterRequest {
  username: string;
  email: string;
  password: string;
  firstName: string;
  lastName: string;
  roles?: string[];
  confirmPassword?: string; // Added for form validation
}

export interface LoginResponse {
  token: string;
  refreshToken: string;
  user: User;
  primaryRole: string;
  roles: string[];
}

export interface RegisterResponse {
  message: string;
  user: User;
  primaryRole: string;
  roles: string[];
  token: string;
  refreshToken: string;
}

export interface AuthState {
  user: User | null;
  token: string | null;
  refreshToken: string | null;
  isAuthenticated: boolean;
  isLoading: boolean;
  error: string | null;
}

export interface AuthContextType extends AuthState {
  login: (credentials: LoginRequest) => Promise<void>;
  register: (userData: RegisterRequest) => Promise<void>;
  logout: () => void;
  refreshAuth: () => Promise<void>;
  clearError: () => void;
}

// API Response Types
export interface ApiResponse<T> {
  data: T;
  message: string;
  timestamp: string;
}

export interface ApiError {
  message: string;
  details?: Record<string, any>;
  timestamp: string;
} 