
import '@testing-library/jest-dom';
import { render, screen } from "@testing-library/react";
import { Card } from "../../src/components/Card";
import testImage from "../../public/testImage.jpg"; 

describe("Card Component", () => {
  it("renders the image with the correct alt text", () => {
    render(<Card image={testImage} alt="Test Alt Text" />);
    const imageElement = screen.getByAltText("Test Alt Text");
    expect(imageElement).toBeInDocument();
  });

  it("renders the body text if provided", () => {
    render(<Card image={testImage} alt="Test Alt Text" body="Test Body Text" />);
    const bodyElement = screen.getByText("Test Body Text");
    expect(bodyElement).toBeInDocument();
  });

  it("renders children elements if provided", () => {
    render(
      <Card image={testImage} alt="Test Alt Text">
        <button>Test Button</button>
      </Card>
    );
    const buttonElement = screen.getByText("Test Button");
    expect(buttonElement).toContainHTML('<button>Test Button</button>');
  });

  it("does not render the body text if not provided", () => {
    render(<Card image={testImage} alt="Test Alt Text" />);
    const bodyElement = screen.queryByText("Test Body Text");
    expect(bodyElement).not.toBeInDocument();
  });

  it("applies hover and transition styles", () => {
    render(<Card image={testImage} alt="Test Alt Text" />);
    const cardElement = screen.getByAltText("Test Alt Text").parentElement?.parentElement;
    expect(cardElement).toHaveClass("card bg-base-200 shadow-xl");
  });
});
