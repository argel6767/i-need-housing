package com.ineedhousing.backend.user;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ineedhousing.backend.jwt.JwtUtils;
import com.ineedhousing.backend.user.requests.SetUserTypeRequest;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private JwtUtils jwtUtils;

    private ObjectMapper objectMapper;
    private User testUser;
    private String testEmail;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        testEmail = "test@example.com";
        testUser = new User(testEmail, "hashedPassword");
        
        when(jwtUtils.getCurrentUserEmail()).thenReturn(testEmail);
    }

    @Test
    @WithMockUser
    void getCurrentUser_ShouldReturnUser() throws Exception {
        when(userService.getUserByEmail(testEmail)).thenReturn(testUser);

        mockMvc.perform(get("/users/me"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.email").value(testEmail));
    }

    @Test
    @WithMockUser
    void getCurrentUser_WhenUserNotFound_ShouldReturnNotFound() throws Exception {
        when(userService.getUserByEmail(testEmail))
            .thenThrow(new UsernameNotFoundException("User not found"));

        mockMvc.perform(get("/users/me"))
               .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void updateCurrentUser_ShouldReturnUpdatedUser() throws Exception {
        when(userService.updateUser(testUser, testEmail)).thenReturn(testUser);

        mockMvc.perform(put("/users/me")
               .contentType(MediaType.APPLICATION_JSON)
               .content(objectMapper.writeValueAsString(testUser)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.email").value(testEmail));
    }

    @Test
    @WithMockUser
    void updateCurrentUser_WhenUserNotFound_ShouldReturnNotFound() throws Exception {
        when(userService.updateUser(testUser, testEmail))
            .thenThrow(new UsernameNotFoundException("User not found"));

        mockMvc.perform(put("/users/me")
               .contentType(MediaType.APPLICATION_JSON)
               .content(objectMapper.writeValueAsString(testUser)))
               .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void setUserType_ShouldReturnUpdatedUser() throws Exception {
        SetUserTypeRequest request = new SetUserTypeRequest();
        request.setUserType(UserType.INTERN);

        when(userService.setUserType(request, testEmail)).thenReturn(testUser);

        mockMvc.perform(put("/users/type")
               .contentType(MediaType.APPLICATION_JSON)
               .content(objectMapper.writeValueAsString(request)))
               .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void setUserType_WhenUserNotFound_ShouldReturnNotFound() throws Exception {
        SetUserTypeRequest request = new SetUserTypeRequest();
        request.setUserType(UserType.INTERN);

        when(userService.setUserType(request, testEmail))
            .thenThrow(new UsernameNotFoundException("User not found"));

        mockMvc.perform(put("/users/type")
               .contentType(MediaType.APPLICATION_JSON)
               .content(objectMapper.writeValueAsString(request)))
               .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void deleteCurrentUser_ShouldReturnSuccessMessage() throws Exception {
        when(userService.deleteUser(testEmail)).thenReturn("User deleted successfully");

        mockMvc.perform(delete("/users/me"))
               .andExpect(status().isOk())
               .andExpect(content().string("User deleted successfully"));
    }

    @Test
    @WithMockUser
    void deleteCurrentUser_WhenUserNotFound_ShouldReturnNotFound() throws Exception {
        when(userService.deleteUser(testEmail))
            .thenThrow(new UsernameNotFoundException("User not found"));

        mockMvc.perform(delete("/users/me"))
               .andExpect(status().isNotFound());
    }
}
