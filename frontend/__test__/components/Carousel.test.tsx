import React from 'react';
import '@testing-library/jest-dom';
import { render, screen } from '@testing-library/react';
import { Carousel } from '../../src/components/Carousel';

describe('Carousel Component', () => {
    it('renders the Carousel component with all cards', () => {
        render(<Carousel />);

        // Check if all cards are rendered with the correct body text
        expect(screen.getByText('Boston')).toBeInTheDocument();
        expect(screen.getByText('New York')).toBeInTheDocument();
        expect(screen.getByText('San Francisco')).toBeInTheDocument();
        expect(screen.getByText('Dallas')).toBeInTheDocument();
    });

    it('renders the correct number of cards', () => {
        render(<Carousel />);

        // Check if the correct number of cards are rendered
        const cards = screen.getAllByRole('img');
        expect(cards.length).toBe(4);
    });

    it('renders images with the correct alt text', () => {
        render(<Carousel />);

        // Check if images have the correct alt text
        expect(screen.getByAltText('Boston')).toBeInTheDocument();
        expect(screen.getByAltText('New York')).toBeInTheDocument();
        expect(screen.getByAltText('San Francisco')).toBeInTheDocument();
        expect(screen.getByAltText('Dallas')).toBeInTheDocument();
    });
});