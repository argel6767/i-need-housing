package com.ineedhousing.backend.admin.views;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.ineedhousing.backend.admin.AdminService;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Input;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;






@Route("")
@PageTitle("Admin Welcome")
public class AdminAuthView extends VerticalLayout{

    private final AdminService adminService;
    private String Auth

    
    public AdminAuthView(AdminService adminService) {
        this.adminService = adminService;

        Div body = new Div();
        H1 header = new H1("Welcome");
        Text welcomeMessage = new Text("Enter your admin details to continue");
    
        body.add(List.of(header, welcomeMessage, formComponent()));
        add(body);
    }

    private FormLayout formComponent() {
        FormLayout form = new FormLayout();

        NativeLabel emailLabel = new NativeLabel("Email");
        Input emailInput = new Input();
        emailInput.setType("email");

        NativeLabel passwordLabel = new NativeLabel("Password");
        Input passwordInput = new Input();
        passwordInput.setType("password");

        form.add(List.of(emailLabel, emailInput, passwordLabel, passwordInput));
        return form;
    }
    
}
