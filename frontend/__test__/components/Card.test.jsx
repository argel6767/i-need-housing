import React from "react";
import { render, screen } from "@testing-library/react";
import { describe, it, expect } from "vitest";
import { Card } from "../../src/components/Card";
import testImage from "../../public/testImage.jpg"; 

describe("Card Component", () => {
  it("renders the image with the correct alt text", () => {
    render(<Card image={testImage} alt="Test Alt Text" />);
    const imageElement = screen.getByAltText("Test Alt Text");
    expect(imageElement).toBeDefined();
  });

  it("renders the body text if provided", () => {
    render(<Card image={testImage} alt="Test Alt Text" body="Test Body Text" />);
    const bodyElement = screen.getByText("Test Body Text");
    expect(bodyElement).toBeDefined();
  });

  it("renders children elements if provided", () => {
    render(
      <Card image={testImage} alt="Test Alt Text">
        <button>Test Button</button>
      </Card>
    );
    const buttonElement = screen.getByText("Test Button");
    expect(buttonElement).toBeDefined();
  });

  it("does not render the body text if not provided", () => {
    render(<Card image={testImage} alt="Test Alt Text" />);
    const bodyElement = screen.queryByText("Test Body Text");
    expect(bodyElement).not.toBeUndefined();
  });

  it("applies hover and transition styles", () => {
    render(<Card image={testImage} alt="Test Alt Text" />);
    const cardElement = screen.getByAltText("Test Alt Text").parentElement?.parentElement;
    expect(cardElement).toHaveClass("hover:scale-105");
    expect(cardElement).toHaveClass("transition-transform");
  });
});
