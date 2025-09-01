package com.ineedhousing.backend.admin.views;

import java.util.List;

import com.ineedhousing.backend.admin.models.AuthenticatedAdminDto;
import com.vaadin.flow.server.VaadinService;
import org.springframework.context.annotation.Lazy;

import com.ineedhousing.backend.admin.AdminService;
import com.ineedhousing.backend.auth.requests.AuthenticateUserDto;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;






@Route("")
@PageTitle("Admin Sign In")
@Lazy
public class AdminAuthView extends VerticalLayout{

    private final AdminService adminService;
    AuthenticateUserDto request = new AuthenticateUserDto();


    
    public AdminAuthView(AdminService adminService) {
        this.adminService = adminService;

        Div body = new Div();
        H1 header = new H1("Welcome");
        Text welcomeMessage = new Text("Enter your admin details to continue");
    
        body.add(List.of(header, welcomeMessage, authComponent()));
        setAlignItems(FlexComponent.Alignment.CENTER);
        add(body);
    }

    private FormLayout authComponent() {
        FormLayout form = new FormLayout();

        EmailField emailField = new EmailField();
        emailField.setLabel("Email");
        emailField.getElement().setAttribute("name","email");
        emailField.setErrorMessage("Enter a valid email");
        emailField.setClearButtonVisible(true);

        PasswordField passwordField = new PasswordField();
        passwordField.setLabel("Password");

        Button login = new Button("Sign In");
        login.addClickListener(event -> {
            request.setUsername(emailField.getValue());
            request.setPassword(passwordField.getValue());
            loginAdmin(request, form);
        });

        form.add(emailField);
        form.add(passwordField);
        form.add(List.of(emailField, passwordField, login));

        return form;
    }

    private void loginAdmin(AuthenticateUserDto request, FormLayout form) {
        try {
            AuthenticatedAdminDto response = adminService.authenticateAdmin(request);
            VaadinService.getCurrentResponse().setHeader("Set-Cookie", response.cookie()); //sets http-only cookie
            UI.getCurrent().navigate(AdminDashboardView.class);
        }
        catch (Exception e) {
            Notification.show(e.getMessage(), 3000, Notification.Position.MIDDLE);
        }
    }
    
}
