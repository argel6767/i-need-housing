package com.ineedhousing.backend.admin.views;

import java.util.List;

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
    
    public AdminAuthView() {
        Div body = new Div();
        H1 header = new H1("Welcome");
        Text welcomeMessage = new Text("Enter your admin details to continue");
        FormLayout form = new FormLayout();
        NativeLabel email = new NativeLabel("Email");
        Input emailInput = new Input();
        emailInput.setType("email");
        NativeLabel password = new NativeLabel("Password");
        Input passwordInput = new Input();
        passwordInput.setType("password");
        form.add(List.of(email, emailInput, password, passwordInput));
    
        body.add(List.of(header, welcomeMessage, form));
        add(body);
    }
    
}
