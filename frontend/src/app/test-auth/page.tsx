'use client'

import { useState } from 'react';
import { Button } from '@/components/ui/button';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { useAuth } from '@/contexts/AuthContext';
import { authApi } from '@/lib/api';

export default function TestAuthPage() {
  const { user, isAuthenticated, logout } = useAuth();
  const [testResult, setTestResult] = useState<string>('');
  const [isLoading, setIsLoading] = useState(false);

  const testBackendConnection = async () => {
    setIsLoading(true);
    try {
      // Test the backend API
      const response = await fetch('http://localhost:8080/auth/health', {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
        },
      });
      
      if (response.ok) {
        const data = await response.json();
        setTestResult(`✅ Backend connected! Response: ${JSON.stringify(data, null, 2)}`);
      } else {
        setTestResult(`❌ Backend connection failed. Status: ${response.status}`);
      }
    } catch (error: any) {
      setTestResult(`❌ Backend connection error: ${error.message}`);
    } finally {
      setIsLoading(false);
    }
  };

  const testFrontendAPI = async () => {
    setIsLoading(true);
    try {
      const response = await fetch('/api/test');
      const data = await response.json();
      setTestResult(`✅ Frontend API working! Response: ${JSON.stringify(data, null, 2)}`);
    } catch (error: any) {
      setTestResult(`❌ Frontend API error: ${error.message}`);
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="min-h-screen bg-gray-50 p-8">
      <div className="max-w-4xl mx-auto">
        <h1 className="text-3xl font-bold text-gray-900 mb-8">Authentication Test Page</h1>
        
        <div className="grid md:grid-cols-2 gap-6">
          <Card>
            <CardHeader>
              <CardTitle>Authentication Status</CardTitle>
            </CardHeader>
            <CardContent>
              <div className="space-y-4">
                <div>
                  <strong>Status:</strong> {isAuthenticated ? '✅ Authenticated' : '❌ Not Authenticated'}
                </div>
                {user && (
                  <div>
                    <strong>User:</strong> {user.firstName} {user.lastName} ({user.username})
                  </div>
                )}
                <div>
                  <strong>Roles:</strong> {user?.roles?.join(', ') || 'None'}
                </div>
                {isAuthenticated && (
                  <Button onClick={logout} variant="destructive">
                    Logout
                  </Button>
                )}
              </div>
            </CardContent>
          </Card>

          <Card>
            <CardHeader>
              <CardTitle>API Tests</CardTitle>
            </CardHeader>
            <CardContent>
              <div className="space-y-4">
                <Button 
                  onClick={testBackendConnection} 
                  disabled={isLoading}
                  className="w-full"
                >
                  Test Backend Connection
                </Button>
                <Button 
                  onClick={testFrontendAPI} 
                  disabled={isLoading}
                  variant="outline"
                  className="w-full"
                >
                  Test Frontend API
                </Button>
                {testResult && (
                  <div className="mt-4 p-4 bg-gray-100 rounded-md">
                    <pre className="text-sm overflow-auto">{testResult}</pre>
                  </div>
                )}
              </div>
            </CardContent>
          </Card>
        </div>

        <Card className="mt-6">
          <CardHeader>
            <CardTitle>Quick Actions</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="flex space-x-4">
              <Button asChild>
                <a href="/auth/login">Go to Login</a>
              </Button>
              <Button asChild variant="outline">
                <a href="/auth/register">Go to Register</a>
              </Button>
              <Button asChild variant="outline">
                <a href="/dashboard">Go to Dashboard</a>
              </Button>
            </div>
          </CardContent>
        </Card>
      </div>
    </div>
  );
} 