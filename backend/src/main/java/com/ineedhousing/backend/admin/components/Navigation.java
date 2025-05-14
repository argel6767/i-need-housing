package com.ineedhousing.backend.admin.components;

import com.ineedhousing.backend.admin.views.AdminAuthView;
import com.ineedhousing.backend.admin.views.AdminDashboardView;
import com.ineedhousing.backend.admin.views.FindNewListingsView;
import com.ineedhousing.backend.admin.views.ListingsView;
import com.ineedhousing.backend.admin.views.SettingsView;
import com.ineedhousing.backend.admin.views.UsersView;

import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;

import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.RouterLink;


//Builds Side Navigation Component
public class Navigation {

        /**
         * Creates a side nav where each link is stacked
         * @return
         */
        public static SideNav getSideNav() {
                SideNav nav = new SideNav();

                SideNavItem homeLink = new SideNavItem("Home", AdminDashboardView.class, VaadinIcon.HOME_O.create());
                SideNavItem usersLink = new SideNavItem("Users",
                        UsersView.class, VaadinIcon.USERS.create());
                SideNavItem listingsLink = new SideNavItem("Listings", ListingsView.class,
                        VaadinIcon.HOME.create());
                SideNavItem findListings = new SideNavItem("Find Listings",
                        FindNewListingsView.class, VaadinIcon.SEARCH.create());
                SideNavItem settingsLink = new SideNavItem("Settings",
                        SettingsView.class, VaadinIcon.COG.create());
                SideNavItem logoutLink = new SideNavItem("Logout", AdminAuthView.class,
                VaadinIcon.SIGN_OUT.create());
                
                nav.addItem(homeLink,usersLink, listingsLink, findListings, settingsLink, logoutLink);
                return nav;
        }

        /**
         * Creates a Horizontal Nav Bar to navigate various admin panels
         * @return
         */
        public static HorizontalLayout getHorizontalNav() {
                HorizontalLayout nav = new HorizontalLayout();
                nav.setPadding(true);
                nav.setSpacing(true);
                
                RouterLink homeLink = new RouterLink("Home", AdminDashboardView.class);
                homeLink.addComponentAsFirst(new Icon(VaadinIcon.HOME_O));
                RouterLink usersLink = new RouterLink("Users", UsersView.class);
                usersLink.addComponentAsFirst(new Icon(VaadinIcon.USERS));
                RouterLink listingsLink = new RouterLink("Listings", ListingsView.class);
                listingsLink.addComponentAsFirst(new Icon(VaadinIcon.HOME));
                RouterLink findListingsLink = new RouterLink("Find Listings", FindNewListingsView.class);
                findListingsLink.addComponentAsFirst(new Icon(VaadinIcon.SEARCH));
                RouterLink settingsLink = new RouterLink("Settings", SettingsView.class);
                settingsLink.addComponentAsFirst(new Icon(VaadinIcon.COG));
                RouterLink logoutLink = new RouterLink("Logout", AdminAuthView.class);
                logoutLink.addComponentAsFirst(new Icon(VaadinIcon.SIGN_OUT));
                nav.add(homeLink, usersLink, listingsLink, findListingsLink, settingsLink, logoutLink);
                
                return nav;
            }
}
