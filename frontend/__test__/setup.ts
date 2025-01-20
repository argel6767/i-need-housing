import '@testing-library/jest-dom'
import { afterEach, vi } from 'vitest'
import { cleanup } from '@testing-library/react';
import React from 'react'

// Mock next/router
vi.mock('next/router', () => ({
  useRouter() {
    return {
      route: '/',
      pathname: '',
      query: '',
      asPath: '',
      push: vi.fn(),
      replace: vi.fn(),
    }
  },
}))

// Mock next/image
vi.mock('next/image', () => ({
  default: (props: any) => {
    const { src, alt, width, height, ...rest } = props;
    return React.createElement('img', { 
      src, 
      alt, 
      width: width || '100', // Default width if not provided
      height: height || '100', // Default height if not provided
      ...rest 
    });
  },
}));

// Cleanup after each test
afterEach(() => {
  cleanup();
});
