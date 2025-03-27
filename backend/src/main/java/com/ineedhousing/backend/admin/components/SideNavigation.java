package com.ineedhousing.backend.admin.components;

import com.ineedhousing.backend.admin.views.FindNewListingsView;
import com.ineedhousing.backend.admin.views.ListingsView;
import com.ineedhousing.backend.admin.views.SettingsView;
import com.ineedhousing.backend.admin.views.UsersView;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;

//Builds Side Navigation Component
public class SideNavigation {

        public static SideNav getSideNav() {
                SideNav nav = new SideNav();

                SideNavItem usersLink = new SideNavItem("Users",
                        UsersView.class, VaadinIcon.USERS.create());
                SideNavItem listingsLink = new SideNavItem("Listings", ListingsView.class,
                        VaadinIcon.HOME.create());
                SideNavItem findListings = new SideNavItem("Find Listings",
                        FindNewListingsView.class, VaadinIcon.GLASSES.create());
                SideNavItem settingsLink = new SideNavItem("Settings",
                        SettingsView.class, VaadinIcon.COG.create());
                
                nav.addItem(usersLink, listingsLink, findListings, settingsLink); 

                return nav;
        }
}
