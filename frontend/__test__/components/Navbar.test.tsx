import { render } from '@testing-library/react';
import { describe, it, expect } from 'vitest';
import { Navbar } from '../../src/components/Navbar';


describe('Navbar Component', () => {
  it('should render', () => {
    const { container } = render(<Navbar />);
    const navbarDiv = container.querySelector('.navbar');
    expect(navbarDiv).to.exist;
  });

  it('should render the navbar-start with the correct link', () => {
    const { getAllByTestId } = render(<Navbar />);
    const homeLinks = getAllByTestId('INeedHousing');
    const homeLink = homeLinks[0];
    expect(homeLink).to.exist;
    expect(homeLink).to.have.arguments("navbar bg-background border-b-2 border-gray-200 shadow-md")
  });

  it('should render the menu in the navbar-center', () => {
    const { getAllByRole } = render(<Navbar />);
    const menus = getAllByRole('list');
    const menu = menus[0];
    expect(menu).to.exist;
    expect(menu).toHaveClass('menu');
    expect(menu).toHaveClass('menu-horizontal');
    expect(menu).toHaveClass('px-1');
    expect(menu).toHaveClass('items-center');
    expect(menu).toHaveClass('text-lg');
  });

  it('should render the About link', () => {
    const { getAllByText } = render(<Navbar />);
    const aboutLink = getAllByText('About');
    expect(aboutLink).to.exist;
    expect(aboutLink.closest('a')).toHaveAttribute('href', '/about');
  });

  it('should render the Source Code link', () => {
    const { getByText } = render(<Navbar />);
    const sourceCodeLink = getByText('Source Code');
    expect(sourceCodeLink).to.exist;
    expect(sourceCodeLink.closest('a')).toHaveAttribute('href', 'https://github.com/argel6767/i-need-housing');
  });

  it('should render the Image component with the correct source and alt text', () => {
    const { getByAltText } = render(<Navbar />);
    const image = getByAltText('Icon');
    expect(image).to.exist;
    expect(image).to.have.contain('src');
    expect(image).toHaveAttribute('alt', 'Icon');
  });

  it('should render the Sign In button', () => {
    const { getByText } = render(<Navbar />);
    const signInButton = getByText('Sign In');
    expect(signInButton).to.exist;
    expect(signInButton).toHaveClass('btn');
    expect(signInButton).toHaveClass('btn-primary');
    expect(signInButton.closest('a')).toHaveAttribute('href', '/sign-in');
  });
});
