import { BreadCrumbs, PreferencesHeader } from "./UserPreferenceComponents";
import {NewUserRoute} from "@/app/(protected)/new-user/NewUserRoute";
import React from "react";

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
        <NewUserRoute>
          <main
          >
            <div className="motion-translate-x-in-[0%] motion-translate-y-in-[100%] motion-duration-1500 py-2">
              <PreferencesHeader/>
              <BreadCrumbs/>
            </div>
              {children}
          </main>
          </NewUserRoute>
  );
}
