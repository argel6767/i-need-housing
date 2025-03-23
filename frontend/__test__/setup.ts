import '@testing-library/jest-dom'
import { cleanup } from '@testing-library/react';
import React from 'react'

// Mock next/router
jest.mock('next/router', () => ({
  useRouter() {
    return {
      route: '/',
      pathname: '',
      query: '',
      asPath: '',
      push: jest.fn(),
      replace: jest.fn(),
    }
  },
}))

// Mock next/image
jest.mock('next/image', () => ({
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
