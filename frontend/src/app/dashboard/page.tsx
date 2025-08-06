'use client'

import { useState, useEffect } from 'react';
import { Button } from '@/components/ui/button';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { useAuth } from '@/contexts/AuthContext';
import { useRouter } from 'next/navigation';
import ProtectedRoute from '@/components/ProtectedRoute';
import { customerApiService } from '@/lib/api';

interface Customer {
  id: number;
  firstName: string;
  lastName: string;
  email: string;
  phone: string;
  company: string;
  status: string;
  totalRevenue: number;
  totalOrders: number;
  assignedTo: string;
}

export default function DashboardPage() {
  const { user, logout } = useAuth();
  const router = useRouter();
  const [customers, setCustomers] = useState<Customer[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchCustomers();
  }, []);

  const fetchCustomers = async () => {
    try {
      const data = await customerApiService.getCustomers();
      const customersArray = data.content || data;
      setCustomers(customersArray);
    } catch (error) {
      console.error('Error fetching customers:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleLogout = () => {
    logout();
    router.push('/auth/login');
  };

  const handleAddCustomer = () => {
    router.push('/customers');
  };

  const handleViewCustomers = () => {
    router.push('/customers');
  };

  return (
    <ProtectedRoute>
      <div className="min-h-screen bg-gray-50">
        {/* Header */}
        <header className="bg-white shadow-sm border-b">
          <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
            <div className="flex justify-between items-center py-6">
              <div>
                <h1 className="text-2xl font-bold text-gray-900">CRM Dashboard</h1>
                <p className="text-gray-600">Welcome to your CRM system</p>
              </div>
              <div className="flex items-center space-x-4">
                {/* Navigation Menu */}
                <div className="flex items-center space-x-2">
                  <Button variant="ghost" size="sm" onClick={() => router.push('/dashboard')}>
                    Dashboard
                  </Button>
                  <Button variant="ghost" size="sm" onClick={() => router.push('/customers')}>
                    Customers
                  </Button>
                </div>
                <span className="text-sm text-gray-600">
                  {user ? `${user.firstName} ${user.lastName}` : 'User'}
                </span>
                <Button variant="outline" size="sm" onClick={handleLogout}>
                  Logout
                </Button>
              </div>
            </div>
          </div>
        </header>

        {/* Main Content */}
        <main className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mb-8">
            {/* Stats Cards */}
            <Card>
              <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
                <CardTitle className="text-sm font-medium">Total Customers</CardTitle>
                <span className="text-2xl">👥</span>
              </CardHeader>
              <CardContent>
                <div className="text-2xl font-bold">{loading ? '...' : customers.length}</div>
                <p className="text-xs text-muted-foreground">Active customers in system</p>
              </CardContent>
            </Card>

            <Card>
              <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
                <CardTitle className="text-sm font-medium">Active Customers</CardTitle>
                <span className="text-2xl">✅</span>
              </CardHeader>
              <CardContent>
                <div className="text-2xl font-bold">
                  {loading ? '...' : customers.filter(c => c.status === 'ACTIVE').length}
                </div>
                <p className="text-xs text-muted-foreground">Currently active</p>
              </CardContent>
            </Card>

            <Card>
              <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
                <CardTitle className="text-sm font-medium">Total Revenue</CardTitle>
                <span className="text-2xl">💰</span>
              </CardHeader>
              <CardContent>
                <div className="text-2xl font-bold">
                  ${loading ? '...' : customers.reduce((sum, c) => sum + (c.totalRevenue || 0), 0).toLocaleString()}
                </div>
                <p className="text-xs text-muted-foreground">From all customers</p>
              </CardContent>
            </Card>

            <Card>
              <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
                <CardTitle className="text-sm font-medium">Total Orders</CardTitle>
                <span className="text-2xl">📦</span>
              </CardHeader>
              <CardContent>
                <div className="text-2xl font-bold">
                  {loading ? '...' : customers.reduce((sum, c) => sum + (c.totalOrders || 0), 0)}
                </div>
                <p className="text-xs text-muted-foreground">Across all customers</p>
              </CardContent>
            </Card>
          </div>

          {/* Quick Actions */}
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
            <Card>
              <CardHeader>
                <CardTitle>Quick Actions</CardTitle>
              </CardHeader>
              <CardContent className="space-y-4">
                <Button className="w-full justify-start" onClick={handleAddCustomer}>
                  <span className="mr-2">➕</span>
                  Add New Customer
                </Button>
                <Button className="w-full justify-start" variant="outline" onClick={handleViewCustomers}>
                  <span className="mr-2">👥</span>
                  View All Customers
                </Button>
                <Button className="w-full justify-start" variant="outline">
                  <span className="mr-2">🎯</span>
                  Create Lead
                </Button>
                <Button className="w-full justify-start" variant="outline">
                  <span className="mr-2">💰</span>
                  New Opportunity
                </Button>
                <Button className="w-full justify-start" variant="outline">
                  <span className="mr-2">📋</span>
                  Add Task
                </Button>
              </CardContent>
            </Card>

            <Card>
              <CardHeader>
                <CardTitle>Recent Customers</CardTitle>
              </CardHeader>
              <CardContent>
                <div className="space-y-4">
                  {loading ? (
                    <div className="text-center py-4">
                      <div className="animate-spin rounded-full h-6 w-6 border-b-2 border-blue-600 mx-auto"></div>
                      <p className="text-sm text-gray-500 mt-2">Loading...</p>
                    </div>
                  ) : customers.length > 0 ? (
                    customers.slice(0, 3).map((customer) => (
                      <div key={customer.id} className="flex items-center space-x-3">
                        <div className="w-2 h-2 bg-green-500 rounded-full"></div>
                        <div className="flex-1">
                          <p className="text-sm font-medium">{customer.firstName} {customer.lastName}</p>
                          <p className="text-xs text-gray-500">{customer.company || customer.email}</p>
                        </div>
                        <span className={`text-xs px-2 py-1 rounded-full ${
                          customer.status === 'ACTIVE' ? 'bg-green-100 text-green-800' :
                          customer.status === 'INACTIVE' ? 'bg-gray-100 text-gray-800' :
                          'bg-blue-100 text-blue-800'
                        }`}>
                          {customer.status}
                        </span>
                      </div>
                    ))
                  ) : (
                    <div className="text-center py-4">
                      <p className="text-sm text-gray-500">No customers found</p>
                      <Button size="sm" className="mt-2" onClick={handleAddCustomer}>
                        Add First Customer
                      </Button>
                    </div>
                  )}
                </div>
              </CardContent>
            </Card>

            <Card>
              <CardHeader>
                <CardTitle>System Status</CardTitle>
              </CardHeader>
              <CardContent>
                <div className="space-y-4">
                  <div className="flex items-center justify-between">
                    <span className="text-sm">Backend Services</span>
                    <span className="text-green-500 text-sm">✅ Online</span>
                  </div>
                  <div className="flex items-center justify-between">
                    <span className="text-sm">Database</span>
                    <span className="text-green-500 text-sm">✅ Online</span>
                  </div>
                  <div className="flex items-center justify-between">
                    <span className="text-sm">Email Service</span>
                    <span className="text-green-500 text-sm">✅ Online</span>
                  </div>
                  <div className="flex items-center justify-between">
                    <span className="text-sm">Message Queue</span>
                    <span className="text-green-500 text-sm">✅ Online</span>
                  </div>
                </div>
              </CardContent>
            </Card>
          </div>
        </main>
      </div>
    </ProtectedRoute>
  );
} 