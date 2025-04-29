package com.ineedhousing.backend.admin.views;

import org.springframework.context.annotation.Lazy;

import com.ineedhousing.backend.admin.AdminService;
import com.ineedhousing.backend.admin.components.GridCreator;
import com.ineedhousing.backend.admin.components.Navigation;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route("/admin/listings")
@PageTitle("Current Listings")
@Lazy
public class ListingsView extends VerticalLayout {

    private final AdminService adminService;

    public ListingsView(AdminService adminService) {
        setSizeFull();
        this.adminService = adminService;
        VerticalLayout main = new VerticalLayout();
        main.add(new H1("Listings Saved In Database"));
        main.add(Navigation.getHorizontalNav());
        main.setSizeFull();
        main.add(GridCreator.buildListingsGrid(adminService.getAllListings()));
        main.setPadding(true);
        main.setSpacing(true);
        add(main);
    }

}
