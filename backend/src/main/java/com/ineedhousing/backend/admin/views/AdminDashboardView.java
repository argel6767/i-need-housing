package com.ineedhousing.backend.admin.views;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route("/admin")
@PageTitle("Admin Dashboard")
public class AdminDashboardView extends VerticalLayout{

    public AdminDashboardView() {
        add(new Div("Yo mama"));
    }
}
