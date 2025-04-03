import { BreadCrumbs, Header } from "./UserPreferenceComponents";

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="en">
      <body
      >
        <Header/>
        <BreadCrumbs/>
          {children}
        
      </body>
    </html>
  );
}
