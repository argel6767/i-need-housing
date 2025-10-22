package com.ineedhousing.backend.admin;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RedirectController {

    @Value("${admin.app.domain}")
    private String adminAppDomain;

    @GetMapping("/")
    public String redirectToAdminApp() {
        return "redirect:" + adminAppDomain;
    }
}
