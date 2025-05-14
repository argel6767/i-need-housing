package com.ineedhousing.backend.admin.views;


import com.vaadin.flow.component.html.H1;
import org.springframework.context.annotation.Lazy;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route("/admin/settings")
@PageTitle("Admin Settings")
@Lazy
public class SettingsView extends VerticalLayout {

    public SettingsView() {
        VerticalLayout main = new VerticalLayout();
        main.add(new H1("Admin Settings"));
    }

}
