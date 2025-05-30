'use client';

import { useState, useEffect } from 'react';
import { AuthProvider } from '@/contexts/AuthContext';

export default function ClientLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  const [mounted, setMounted] = useState(false);

  useEffect(() => {
    setMounted(true);
  }, []);

  if (!mounted) {
    return null;
  }

  return <AuthProvider>{children}</AuthProvider>;
} 