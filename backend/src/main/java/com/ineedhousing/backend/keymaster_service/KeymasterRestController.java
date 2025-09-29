package com.ineedhousing.backend.keymaster_service;

import com.ineedhousing.backend.keymaster_service.models.responses.RegisteredServiceDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/keymaster-service")
public class KeymasterRestController {

    private final KeymasterRestService keymasterRestService;

    public KeymasterRestController(KeymasterRestService keymasterRestService) {
        this.keymasterRestService = keymasterRestService;
    }

    @PostMapping("/register-service/{service}")
    public ResponseEntity<RegisteredServiceDto> registerService(@PathVariable String service) {
        RegisteredServiceDto dto = keymasterRestService.registerNewService(service);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }
}
