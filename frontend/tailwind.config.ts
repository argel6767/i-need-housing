import type { Config } from "tailwindcss";
import motion from 'tailwindcss-motion';

export default {
    darkMode: ["class"],
    content: [
    "./src/pages/**/*.{js,ts,jsx,tsx,mdx}",
    "./src/components/**/*.{js,ts,jsx,tsx,mdx}",
    "./src/app/**/*.{js,ts,jsx,tsx,mdx}",
  ],
  theme: {
  	extend: {
  		colors: {
  			primary: '#176087',
  			secondary: '#bb4430',
  			background: '#ffffff',
  			foreground: '#000000'
  		},
  		fontFamily: {
  			sans: [
  				'Arial',
  				'sans-serif'
  			]
  		},
  		backgroundImage: {
			'hero-pattern': "linear-gradient(rgba(0, 0, 0, 0.5), rgba(0, 0, 0, 0.5)), url('/hero.jpg')"
		},
    animation: {
      fade: 'fadeIn .5s ease-in-out',
    },

    keyframes: {
      fadeIn: {
        from: { opacity: "0" },
        to: { opacity: "1" },
      },
    },
  	}
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
    require('daisyui'),
      require("tailwindcss-animate"),
      require('tailwindcss-motion')
],
} satisfies Config;
