package com.ineedhousing.backend.admin.views;

import java.util.List;
import java.util.Optional;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Point;

import com.ineedhousing.backend.admin.AdminService;
import com.ineedhousing.backend.admin.components.SideNavigation;
import com.ineedhousing.backend.apis.AirbnbApiService;
import com.ineedhousing.backend.apis.RentCastAPIService;
import com.ineedhousing.backend.housing_listings.HousingListing;
import com.ineedhousing.backend.user.User;
import com.ineedhousing.backend.user.UserService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.virtuallist.VirtualList;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

@Route("/admin")
@PageTitle("Admin Dashboard")
public class AdminDashboardView extends VerticalLayout{
    
    private final AdminService adminService;
    private final ListingRetrievalForm listingRetrievalForm;


    public AdminDashboardView(AdminService adminService, ListingRetrievalForm listingRetrievalForm) {
        setSizeFull();
        this.adminService = adminService;
        this.listingRetrievalForm = listingRetrievalForm;
        Div main = new Div();
        main.add(new H1("Admin Dashboard"));
        main.add(SideNavigation.getSideNav());
        main.setSizeFull();
        add(main);
    }

}
