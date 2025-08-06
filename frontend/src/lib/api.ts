import axios, { AxiosInstance, AxiosResponse, AxiosError } from 'axios';
import { LoginRequest, RegisterRequest, LoginResponse, RegisterResponse, User, ApiError } from '@/types/auth';

// API Configuration - Phase 1: Direct service connections
const isProduction = process.env.NODE_ENV === 'production';

// Service URLs
const AUTH_SERVICE_URL = isProduction 
  ? 'https://crm-auth-service.onrender.com'
  : 'http://localhost:8085';

const CUSTOMER_SERVICE_URL = isProduction
  ? 'https://crm-customer-service.onrender.com'
  : 'http://localhost:8081';

// Create axios instances for each service
const authApi: AxiosInstance = axios.create({
  baseURL: AUTH_SERVICE_URL,
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json',
  },
});

const customerApi: AxiosInstance = axios.create({
  baseURL: CUSTOMER_SERVICE_URL,
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Request interceptor to add auth token
const addAuthToken = (config: any) => {
  const token = localStorage.getItem('auth_token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
};

authApi.interceptors.request.use(addAuthToken);
customerApi.interceptors.request.use(addAuthToken);

// Response interceptor to handle token refresh
const handleTokenRefresh = async (error: AxiosError, api: AxiosInstance) => {
  const originalRequest = error.config;
  
  if (error.response?.status === 401 && originalRequest) {
    const refreshToken = localStorage.getItem('refresh_token');
    
    if (refreshToken) {
      try {
        const response = await axios.post(`${AUTH_SERVICE_URL}/auth/refresh`, {
          refreshToken,
        });
        
        const { token } = response.data;
        localStorage.setItem('auth_token', token);
        
        // Retry original request
        originalRequest.headers.Authorization = `Bearer ${token}`;
        return api(originalRequest);
      } catch (refreshError) {
        // Refresh failed, redirect to login
        localStorage.removeItem('auth_token');
        localStorage.removeItem('refresh_token');
        window.location.href = '/auth/login';
        return Promise.reject(refreshError);
      }
    }
  }
  
  return Promise.reject(error);
};

authApi.interceptors.response.use(
  (response: AxiosResponse) => response,
  (error: AxiosError) => handleTokenRefresh(error, authApi)
);

customerApi.interceptors.response.use(
  (response: AxiosResponse) => response,
  (error: AxiosError) => handleTokenRefresh(error, customerApi)
);

// Auth API methods - Connect to Auth Service
export const authApiService = {
  // Login
  login: async (credentials: LoginRequest): Promise<LoginResponse> => {
    const response = await authApi.post<LoginResponse>('/auth/login', credentials);
    return response.data;
  },

  // Register
  register: async (userData: RegisterRequest): Promise<RegisterResponse> => {
    const response = await authApi.post<RegisterResponse>('/auth/register', userData);
    return response.data;
  },

  // Get current user profile
  getProfile: async (): Promise<User> => {
    const response = await authApi.get<User>('/auth/profile');
    return response.data;
  },

  // Validate token
  validateToken: async (): Promise<boolean> => {
    try {
      await authApi.get('/auth/validate');
      return true;
    } catch (error) {
      return false;
    }
  },

  // Refresh token
  refreshToken: async (refreshToken: string): Promise<{ token: string }> => {
    const response = await authApi.post<{ token: string }>('/auth/refresh', { refreshToken });
    return response.data;
  },
};

// Customer API methods - Connect to Customer Service
export const customerApiService = {
  // Get all customers
  getCustomers: async (page = 0, size = 10) => {
    const response = await customerApi.get(`/customers?page=${page}&size=${size}`);
    return response.data;
  },

  // Get customer by ID
  getCustomer: async (id: string) => {
    const response = await customerApi.get(`/customers/${id}`);
    return response.data;
  },

  // Create customer
  createCustomer: async (customerData: any) => {
    const response = await customerApi.post('/customers', customerData);
    return response.data;
  },

  // Update customer
  updateCustomer: async (id: string, customerData: any) => {
    const response = await customerApi.put(`/customers/${id}`, customerData);
    return response.data;
  },

  // Delete customer
  deleteCustomer: async (id: string) => {
    const response = await customerApi.delete(`/customers/${id}`);
    return response.data;
  },

  // Get customer statistics
  getCustomerStats: async () => {
    const response = await customerApi.get('/customers/stats');
    return response.data;
  },
};

// Generic API error handler
export const handleApiError = (error: AxiosError): string => {
  if (error.response?.data) {
    const apiError = error.response.data as ApiError;
    return apiError.message || 'An error occurred';
  }
  
  if (error.code === 'NETWORK_ERROR') {
    return 'Network error. Please check your connection.';
  }
  
  return error.message || 'An unexpected error occurred';
};

// Export for backward compatibility
export const authApi = authApiService;
export default authApi; 