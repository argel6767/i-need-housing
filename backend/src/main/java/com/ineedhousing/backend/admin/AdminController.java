package com.ineedhousing.backend.admin;

import com.ineedhousing.backend.admin.models.AuthenticatedAdminDto;
import com.ineedhousing.backend.auth.requests.AuthenticateUserDto;
import com.ineedhousing.backend.keymaster_service.KeymasterRestService;
import com.ineedhousing.backend.keymaster_service.models.responses.RegisteredServiceDto;
import com.ineedhousing.backend.user.responses.UserDto;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;
    private final KeymasterRestService keymasterRestService;

    public AdminController(AdminService adminService, KeymasterRestService keymasterRestService) {
        this.adminService = adminService;
        this.keymasterRestService = keymasterRestService;
    }

    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@RequestBody AuthenticateUserDto request, HttpServletResponse response) {
        AuthenticatedAdminDto successfulAuthentication = adminService.authenticateAdmin(request);
        response.setHeader("Set-Cookie", successfulAuthentication.cookie());
        return ResponseEntity.ok(successfulAuthentication.userDto());
    }

    @PostMapping("/keymaster-service/register-service/{service}")
    public ResponseEntity<RegisteredServiceDto> registerService(@PathVariable String service) {
        RegisteredServiceDto dto = keymasterRestService.registerNewService(service);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }
}
