package com.ineedhousing.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;

@Theme(variant = Lumo.DARK)
@SpringBootApplication
@EnableCaching
public class BackendApplication implements AppShellConfigurator {

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

}
