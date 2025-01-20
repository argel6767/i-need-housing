import type { Config } from "tailwindcss";

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
		}
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
      require("tailwindcss-animate")
],
} satisfies Config;
