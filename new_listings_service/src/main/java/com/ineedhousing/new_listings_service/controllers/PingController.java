package com.ineedhousing.new_listings_service.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1")
public class PingController {

    @PostMapping("/ping")
    public String ping() {
        return "Service successfully pinged. Hello!";
    }
}
