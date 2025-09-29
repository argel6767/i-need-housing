package com.ineedhousing.backend.admin;

import com.ineedhousing.backend.admin.models.AuthenticatedAdminDto;
import com.ineedhousing.backend.auth.requests.AuthenticateUserDto;
import com.ineedhousing.backend.user.responses.UserDto;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@RequestBody AuthenticateUserDto request, HttpServletResponse response) {
        AuthenticatedAdminDto successfulAuthentication = adminService.authenticateAdmin(request);
        response.setHeader("Set-Cookie", successfulAuthentication.cookie());
        return ResponseEntity.ok(successfulAuthentication.userDto());
    }
}
