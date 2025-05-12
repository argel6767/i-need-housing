import { BreadCrumbs, PreferencesHeader } from "./UserPreferenceComponents";

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
      <main
      >
        <div className="motion-translate-x-in-[0%] motion-translate-y-in-[100%] motion-duration-1500 py-2">
          <PreferencesHeader/>
          <BreadCrumbs/>
        </div>
          {children}
      </main>
  );
}
