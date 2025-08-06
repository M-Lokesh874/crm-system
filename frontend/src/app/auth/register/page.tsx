'use client'

import { useState } from 'react';
import { useRouter } from 'next/navigation';
import { RegisterForm } from '@/components/auth/RegisterForm';
import { RegisterRequest } from '@/types/auth';
import { useAuth } from '@/contexts/AuthContext';

export default function RegisterPage() {
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const router = useRouter();
  const { register } = useAuth();

  const handleRegister = async (userData: RegisterRequest) => {
    setIsLoading(true);
    setError(null);
    
    try {
      await register(userData);
      console.log('Registration successful');
      router.push('/dashboard');
    } catch (err: any) {
      setError(err.message || 'Registration failed. Please try again.');
      console.error('Registration error:', err);
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="min-h-screen bg-gray-50 flex items-center justify-center py-12 px-4 sm:px-6 lg:px-8">
      <div className="max-w-md w-full space-y-8">
        <div className="text-center">
          <h1 className="text-3xl font-bold text-gray-900">CRM System</h1>
          <p className="mt-2 text-sm text-gray-600">
            Create your account to get started
          </p>
        </div>
        
        <RegisterForm 
          onSubmit={handleRegister}
          isLoading={isLoading}
          error={error}
        />
      </div>
    </div>
  );
} 