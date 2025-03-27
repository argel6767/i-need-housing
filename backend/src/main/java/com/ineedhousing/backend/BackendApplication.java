package com.ineedhousing.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;

@Theme(variant = Lumo.DARK)
@SpringBootApplication
public class BackendApplication implements AppShellConfigurator {

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

}
