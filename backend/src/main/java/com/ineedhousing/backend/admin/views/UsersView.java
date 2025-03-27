package com.ineedhousing.backend.admin.views;

import com.ineedhousing.backend.admin.AdminService;
import com.ineedhousing.backend.admin.components.GridCreator;
import com.ineedhousing.backend.admin.components.SideNavigation;
import com.ineedhousing.backend.user.User;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
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
        Div main = new Div();
        main.add(new H1("Current Users"));
        main.add(SideNavigation.getSideNav());
        main.setSizeFull();
        Grid<User> users = GridCreator.buildUserGrid(adminService.getAllUsers());
        main.add(users);
        add(main);
    }

}
