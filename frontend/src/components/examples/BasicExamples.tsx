'use client'

import { useState } from 'react'
import { Button } from '@/components/ui/button'
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card'

// 1. Simple Component (Like Java Class)
interface GreetingProps {
  name: string;
  role: string;
}

export function Greeting({ name, role }: GreetingProps) {
  return (
    <Card>
      <CardHeader>
        <CardTitle>Hello, {name}!</CardTitle>
      </CardHeader>
      <CardContent>
        <p>Your role is: {role}</p>
      </CardContent>
    </Card>
  );
}

// 2. State Management (Like Instance Variables)
export function Counter() {
  const [count, setCount] = useState(0);
  
  const increment = () => setCount(count + 1);
  const decrement = () => setCount(count - 1);
  
  return (
    <Card>
      <CardHeader>
        <CardTitle>Counter Example</CardTitle>
      </CardHeader>
      <CardContent>
        <p className="text-2xl font-bold mb-4">Count: {count}</p>
        <div className="flex gap-2">
          <Button onClick={increment}>+</Button>
          <Button onClick={decrement}>-</Button>
        </div>
      </CardContent>
    </Card>
  );
}

// 3. Form Handling (Like Java Form Processing)
export function SimpleForm() {
  const [name, setName] = useState('');
  const [email, setEmail] = useState('');
  const [submitted, setSubmitted] = useState(false);
  
  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    setSubmitted(true);
    console.log('Form submitted:', { name, email });
  };
  
  return (
    <Card>
      <CardHeader>
        <CardTitle>Form Example</CardTitle>
      </CardHeader>
      <CardContent>
        {!submitted ? (
          <form onSubmit={handleSubmit} className="space-y-4">
            <div>
              <label className="block text-sm font-medium mb-1">Name:</label>
              <input
                type="text"
                value={name}
                onChange={(e) => setName(e.target.value)}
                className="w-full p-2 border rounded"
                placeholder="Enter your name"
              />
            </div>
            <div>
              <label className="block text-sm font-medium mb-1">Email:</label>
              <input
                type="email"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                className="w-full p-2 border rounded"
                placeholder="Enter your email"
              />
            </div>
            <Button type="submit">Submit</Button>
          </form>
        ) : (
          <div className="text-green-600">
            <p>Thank you, {name}!</p>
            <p>Email: {email}</p>
            <Button onClick={() => setSubmitted(false)} className="mt-2">
              Reset Form
            </Button>
          </div>
        )}
      </CardContent>
    </Card>
  );
}

// 4. Conditional Rendering (Like Java if-else)
export function ConditionalExample({ isLoggedIn, username }: { isLoggedIn: boolean; username?: string }) {
  return (
    <Card>
      <CardHeader>
        <CardTitle>Conditional Rendering</CardTitle>
      </CardHeader>
      <CardContent>
        {isLoggedIn ? (
          <div className="text-green-600">
            <p>✅ Welcome back, {username}!</p>
            <p>You are logged in.</p>
          </div>
        ) : (
          <div className="text-orange-600">
            <p>⚠️ Please log in to continue.</p>
            <Button className="mt-2">Login</Button>
          </div>
        )}
      </CardContent>
    </Card>
  );
}

// 5. List Rendering (Like Java for-each)
export function CustomerList() {
  const customers = [
    { id: 1, name: 'John Doe', email: 'john@example.com', status: 'Active' },
    { id: 2, name: 'Jane Smith', email: 'jane@example.com', status: 'Inactive' },
    { id: 3, name: 'Bob Johnson', email: 'bob@example.com', status: 'Active' },
  ];
  
  return (
    <Card>
      <CardHeader>
        <CardTitle>Customer List (Like Java for-each)</CardTitle>
      </CardHeader>
      <CardContent>
        <div className="space-y-2">
          {customers.map((customer) => (
            <div key={customer.id} className="border p-3 rounded">
              <h4 className="font-semibold">{customer.name}</h4>
              <p className="text-sm text-gray-600">{customer.email}</p>
              <span className={`text-xs px-2 py-1 rounded ${
                customer.status === 'Active' ? 'bg-green-100 text-green-800' : 'bg-red-100 text-red-800'
              }`}>
                {customer.status}
              </span>
            </div>
          ))}
        </div>
      </CardContent>
    </Card>
  );
}

// 6. Main Examples Component
export function BasicExamples() {
  return (
    <div className="container mx-auto p-6 space-y-6">
      <h1 className="text-3xl font-bold text-center mb-8">
        React.js Examples for Java Developers
      </h1>
      
      <div className="grid md:grid-cols-2 gap-6">
        <Greeting name="John" role="Developer" />
        <Counter />
        <SimpleForm />
        <ConditionalExample isLoggedIn={true} username="John" />
        <CustomerList />
        <ConditionalExample isLoggedIn={false} />
      </div>
    </div>
  );
} 