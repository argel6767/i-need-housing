import { BreadCrumbs, Header } from "./UserPreferenceComponents";

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
      <main
      >
        <div className="motion-translate-x-in-[0%] motion-translate-y-in-[100%] motion-duration-1500">
          <Header/>
          <BreadCrumbs/>
        </div>
          {children}
        
      </main>
  );
}
