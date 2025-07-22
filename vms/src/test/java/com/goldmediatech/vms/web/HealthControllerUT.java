package com.goldmediatech.vms.web;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.goldmediatech.vms.configuration.JwtUtil;
import com.goldmediatech.vms.configuration.SecurityConfiguration;
import com.goldmediatech.vms.persistence.UserLoader;
import com.goldmediatech.vms.persistence.UserRepository;
import com.goldmediatech.vms.service.DefaultUserDetailService;

@DisplayName("HealthController Unit Tests")
@WebMvcTest(HealthController.class)
@Import(SecurityConfiguration.class)
public class HealthControllerUT {

    private static final String HEALTH_URL = "/health";

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DefaultUserDetailService defaultUserDetailService;

    @MockitoBean
    private JwtUtil jwtUtil;

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private UserLoader userLoader;

    @Test
    @DisplayName("[HEALTH] Authorized admin role health check")
    void health_whenAdminUser_thenStatus200() throws Exception {
        // GIVEN
        final String token = "mockedToken";
        final String username = "admin";
        final String password = "AdminPassword1@3";
        final String role = "ROLE_ADMIN";
        final UserDetails adminUser = new User(username, password,
                List.of(new SimpleGrantedAuthority(role)));

        // WHEN
        when(jwtUtil.isTokenValid(token)).thenReturn(true);
        when(jwtUtil.extractUserName(token)).thenReturn(username);
        when(defaultUserDetailService.loadUserByUsername(username)).thenReturn(adminUser);

        // THEN
        mockMvc.perform(get(HEALTH_URL)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("[HEALTH] Authorized system role health check")
    void health_whenSystemUser_thenStatus200() throws Exception {
        // GIVEN
        final String token = "mockedToken";
        final String username = "system";
        final String password = "SystemPassword1@3";
        final String role = "ROLE_SYSTEM";
        final UserDetails adminUser = new User(username, password,
                List.of(new SimpleGrantedAuthority(role)));

        // WHEN
        when(jwtUtil.isTokenValid(token)).thenReturn(true);
        when(jwtUtil.extractUserName(token)).thenReturn(username);
        when(defaultUserDetailService.loadUserByUsername(username)).thenReturn(adminUser);

        // THEN
        mockMvc.perform(get(HEALTH_URL)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("[HEALTH] Unauthorized user role health check")
    void health_whenUnauthorizedUser_thenStatus403() throws Exception {
        // GIVEN
        final String token = "mockedToken";
        final String username = "unauthorizedUser";
        final String password = "SomePassword1@3";
        final String role = "ROLE_USER";
        final UserDetails unauthorizedUser = new User(username, password,
                List.of(new SimpleGrantedAuthority(role)));

        // WHEN
        when(jwtUtil.isTokenValid(token)).thenReturn(true);
        when(jwtUtil.extractUserName(token)).thenReturn(username);
        when(defaultUserDetailService.loadUserByUsername(username)).thenReturn(unauthorizedUser);

        // THEN
        mockMvc.perform(get(HEALTH_URL)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("[HEALTH] Not authenticated health check")
    void health_whenNotAuthenticated_thenStatus403() throws Exception {
        // WHEN
        when(jwtUtil.isTokenValid(anyString())).thenReturn(false);

        // THEN
        mockMvc.perform(get(HEALTH_URL))
                .andExpect(status().isForbidden());
    }
}
