package com.ineedhousing.backend.admin.views;

                
import com.ineedhousing.backend.admin.AdminService;
import com.ineedhousing.backend.admin.components.Navigation;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;


@Route("/admin")
@PageTitle("Admin Dashboard")
public class AdminDashboardView extends VerticalLayout{
    
    private final AdminService adminService;

    public AdminDashboardView(AdminService adminService) {
        this.adminService = adminService;
        VerticalLayout main = new VerticalLayout();
        main.add(new H1("Admin Dashboard"));
        main.add(Navigation.getSideNav());
        main.setSizeFull();
        main.setAlignItems(FlexComponent.Alignment.CENTER);
        main.setPadding(true);
        main.setSpacing(true);
        add(main);
    }

}
