import { render } from '@testing-library/react';
import { describe, it, expect } from 'vitest';
import { Navbar } from '../../src/components/Navbar';
import Link from 'next/link';

describe('Navbar Component', () => {
  it('should render the navbar container with the appropriate classes', () => {
    const { container } = render(<Navbar />);
    const navbarDiv = container.querySelector('.navbar');
    expect(navbarDiv).to.exist;
    expect(navbarDiv).to.have.class('bg-background');
    expect(navbarDiv).to.have.class('border-b-2');
    expect(navbarDiv).to.have.class('border-gray-200');
    expect(navbarDiv).to.have.class('shadow-md');
  });

  it('should render the navbar-start with the correct link', () => {
    const { getByText } = render(<Navbar />);
    const homeLink = getByText('INeedHousing');
    expect(homeLink).to.exist;
    expect(homeLink).to.have.class('btn');
    expect(homeLink).to.have.class('btn-ghost');
    expect(homeLink).to.have.class('text-xl');
    expect(homeLink).to.have.class('text-primary');
    expect(homeLink).to.have.class('font-bold');
    expect(homeLink.closest('a')).to.have.attribute('href', '/');
  });

  it('should render the menu in the navbar-center', () => {
    const { getByRole } = render(<Navbar />);
    const menu = getByRole('list');
    expect(menu).to.exist;
    expect(menu).to.have.class('menu');
    expect(menu).to.have.class('menu-horizontal');
    expect(menu).to.have.class('px-1');
    expect(menu).to.have.class('items-center');
    expect(menu).to.have.class('text-lg');
  });

  it('should render the About link', () => {
    const { getByText } = render(<Navbar />);
    const aboutLink = getByText('About');
    expect(aboutLink).to.exist;
    expect(aboutLink.closest('a')).to.have.attribute('href', '/about');
  });

  it('should render the Source Code link', () => {
    const { getByText } = render(<Navbar />);
    const sourceCodeLink = getByText('Source Code');
    expect(sourceCodeLink).to.exist;
    expect(sourceCodeLink.closest('a')).to.have.arguments('href', 'https://github.com/argel6767/i-need-housing');
  });

  it('should render the Image component with the correct source and alt text', () => {
    const { getByAltText } = render(<Navbar />);
    const image = getByAltText('Icon');
    expect(image).to.exist;
    expect(image).to.have.attribute('src');
    expect(image).to.have.attribute('alt', 'Icon');
  });

  it('should render the Sign In button', () => {
    const { getByText } = render(<Navbar />);
    const signInButton = getByText('Sign In');
    expect(signInButton).to.exist;
    expect(signInButton).to.have.class('btn');
    expect(signInButton).to.have.class('btn-primary');
    expect(signInButton.closest('a')).to.have.attribute('href', '/sign-in');
  });
});
