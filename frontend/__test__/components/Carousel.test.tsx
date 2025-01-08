import React from "react";
import { getByRole, getByText, render } from '@testing-library/react';
import { describe, it, expect } from 'vitest';
import { Carousel } from '../../src/components/Carousel';
import { Card } from '../../src/components/Card';

describe('Carousel Component', () => {
  it('should render four Card components', () => {
    const { getAllByRole } = render(<Carousel />);
    const cards = getAllByRole('img');
    expect(cards).to.have.lengthOf(4);
  });

  it('should pass correct props to the Card components', () => {
    const { getByAltText } = render(<Carousel />);

    const bostonCard = getByAltText('Boston');
    expect(bostonCard).to.exist;

    const newYorkCard = getByAltText('New York');
    expect(newYorkCard).to.exist;

    const sanFranCard = getByAltText('San Francisco');
    expect(sanFranCard).to.exist;

    const dallasCard = getByAltText('Dallas');
    expect(dallasCard).to.exist;
  });

  it('should render correct footer content in the Card components', () => {
    const { getByRole } = render(<Carousel />);

    const bostonFooter = getByRole('footer');
    expect(bostonFooter).to.exist;

    const newYorkFooter = getByRole('footer');
    expect(newYorkFooter).to.exist;

    const sanFranFooter = getByRole('footer');
    expect(sanFranFooter).to.exist;

    const dallasFooter = getByRole('footer');
    expect(dallasFooter).to.exist;
  });
});
