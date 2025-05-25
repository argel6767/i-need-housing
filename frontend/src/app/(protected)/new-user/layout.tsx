import { BreadCrumbs, PreferencesHeader } from "./UserPreferenceComponents";
import {NewUserRoute} from "@/app/(protected)/new-user/NewUserRoute";
import React from "react";

import {Metadata} from "next";

export const metadata: Metadata = {
    title: 'New User | INeedHousing',
    description: "INeedHousing New User Onboarding"
};

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
