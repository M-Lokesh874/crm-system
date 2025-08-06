'use client'

import Link from 'next/link';
import { Button } from '@/components/ui/button';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { Construction, Home, ArrowLeft } from 'lucide-react';

export default function NotFound() {
  return (
    <div className="min-h-screen bg-gradient-to-br from-blue-50 to-indigo-100 flex items-center justify-center">
      <div className="max-w-md w-full mx-auto px-4">
        <Card className="text-center">
          <CardHeader>
            <div className="mx-auto w-16 h-16 bg-yellow-100 rounded-full flex items-center justify-center mb-4">
              <Construction className="h-8 w-8 text-yellow-600" />
            </div>
            <CardTitle className="text-2xl font-bold text-gray-900">
              Page Not Found
            </CardTitle>
            <p className="text-gray-600 mt-2">
              Sorry, the page you're looking for doesn't exist or is under construction.
            </p>
          </CardHeader>
          <CardContent className="space-y-4">
            <div className="bg-blue-50 border border-blue-200 rounded-lg p-4">
              <h3 className="font-semibold text-blue-900 mb-2">🚧 Work in Progress</h3>
              <p className="text-sm text-blue-700">
                This feature is currently being developed. We're working hard to bring you the best CRM experience!
              </p>
            </div>
            
            <div className="flex flex-col sm:flex-row gap-3">
              <Button asChild className="flex-1">
                <Link href="/">
                  <Home className="h-4 w-4 mr-2" />
                  Go Home
                </Link>
              </Button>
              <Button asChild variant="outline" className="flex-1">
                <Link href="/dashboard">
                  <ArrowLeft className="h-4 w-4 mr-2" />
                  Dashboard
                </Link>
              </Button>
            </div>
            
            <div className="text-xs text-gray-500 mt-4">
              <p>Available pages:</p>
              <div className="flex flex-wrap gap-2 mt-2 justify-center">
                <Link href="/" className="text-blue-600 hover:text-blue-700">Home</Link>
                <span>•</span>
                <Link href="/auth/login" className="text-blue-600 hover:text-blue-700">Login</Link>
                <span>•</span>
                <Link href="/auth/register" className="text-blue-600 hover:text-blue-700">Register</Link>
                <span>•</span>
                <Link href="/dashboard" className="text-blue-600 hover:text-blue-700">Dashboard</Link>
                <span>•</span>
                <Link href="/examples" className="text-blue-600 hover:text-blue-700">Examples</Link>
              </div>
            </div>
          </CardContent>
        </Card>
      </div>
    </div>
  );
} 