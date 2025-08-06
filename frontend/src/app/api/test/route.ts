import { NextResponse } from 'next/server';

export async function GET() {
  return NextResponse.json({
    message: 'Frontend API is working!',
    timestamp: new Date().toISOString(),
    status: 'success'
  });
}

export async function POST(request: Request) {
  const body = await request.json();
  
  return NextResponse.json({
    message: 'Test POST endpoint working!',
    receivedData: body,
    timestamp: new Date().toISOString(),
    status: 'success'
  });
} 