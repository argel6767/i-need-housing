package com.ineedhousing.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.metrics.buffering.BufferingApplicationStartup;
import org.springframework.cache.annotation.EnableCaching;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;

@Theme(variant = Lumo.DARK)
@SpringBootApplication
@EnableCaching
public class BackendApplication implements AppShellConfigurator {

	public static void main(String[] args) {
		SpringApplication application = new SpringApplication(BackendApplication.class);
		application.setApplicationStartup(new BufferingApplicationStartup(10000));
		application.run(args);	
	
	}

}
