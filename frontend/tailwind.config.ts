import type { Config } from "tailwindcss";

export default {
  content: [
    "./src/pages/**/*.{js,ts,jsx,tsx,mdx}",
    "./src/components/**/*.{js,ts,jsx,tsx,mdx}",
    "./src/app/**/*.{js,ts,jsx,tsx,mdx}",
  ],
  theme: {
    extend: {
      colors: {
        primary: '#176087', // Lapis Lazuli
        secondary: '#bb4430', // Persian red
        background: "var(--background)",
        foreground: "var(--foreground)",
      },
      fontFamily: {
        sans: ['Arial', 'sans-serif'],
    },
    backgroundImage: {
      'hero-pattern': "linear-gradient(rgba(0, 0, 0, 0.5), rgba(0, 0, 0, 0.5)), url('/hero.jpg')",
    }
  },
  },
  daisyui: {
    themes: [
      {
        mytheme: {
          primary: '#176087',
          secondary: '#bb4430',
        },
      },
    ],
  },
  plugins: [
    require('daisyui')
  ],
} satisfies Config;
