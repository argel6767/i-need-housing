
import type { Metadata } from "next";
import { Geist, Geist_Mono } from "next/font/google";
import "./globals.css";
import Providers from "./providers";
import { GlobalProvider } from "@/components/GlobalContext";
import { SpeedInsights } from "@vercel/speed-insights/next";
import { Analytics } from "@vercel/analytics/react";
import { GoogleMapsProvider } from "@/components/GoogleMapContext";

const geistSans = Geist({
  variable: "--font-geist-sans",
  subsets: ["latin"],
});

const geistMono = Geist_Mono({
  variable: "--font-geist-mono",
  subsets: ["latin"],
});

export const metadata: Metadata = {
  title: "INeedHousing",
  description: "Find housing with INeedHousing",
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="en">
      <body
        className={`${geistSans.variable} ${geistMono.variable} antialiased`}
      >
        <GoogleMapsProvider>
          <GlobalProvider>
            <Providers>
                {children}
            </Providers>
              <SpeedInsights/>
              <Analytics />
          </GlobalProvider>
        </GoogleMapsProvider>
      </body>
    </html>
  );
}
