package com.ineedhousing.backend.admin.views;

import com.ineedhousing.backend.admin.AdminService;
import com.ineedhousing.backend.admin.components.GridCreator;
import com.ineedhousing.backend.admin.components.Navigation;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route("/admin/users")
@PageTitle("Current Users")
public class UsersView extends VerticalLayout{

    private final AdminService adminService;
    
    public UsersView(AdminService adminService) {
        setSizeFull();
        this.adminService = adminService;
        VerticalLayout main = new VerticalLayout();
        main.add(new H1("Current Users"));
        main.add(Navigation.getHorizontalNav());
        main.setSizeFull();
        main.setPadding(true);
        main.setSpacing(true);
        main.add(GridCreator.buildUserGrid(adminService.getAllUsers()));
        add(main);
    }

}
