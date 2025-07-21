package com.goldmediatech.vms.web;

import com.goldmediatech.vms.configuration.JwtUtil;
import com.goldmediatech.vms.configuration.SecurityConfiguration;
import com.goldmediatech.vms.service.AuthService;
import com.goldmediatech.vms.service.DefaultUserDetailService;
import com.goldmediatech.vms.service.dto.JwtDto;
import com.goldmediatech.vms.service.dto.UserDto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("AuthController Unit Tests")
@WebMvcTest(AuthController.class)
@Import(SecurityConfiguration.class)
public class AuthControllerTest {

    private static final String AUTH_LOGIN_URL = "/auth/login";

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private DefaultUserDetailService defaultUserDetailService;

    @MockitoBean
    private JwtUtil jwtUtil;

    @Test
    @DisplayName("[LOGIN] Successful login")
    void login_whenCredentialsAreValid_thenStatusIsOk_andJwtTokenIsReturned() throws Exception {
        // GIVEN
        final String token = "mockedToken";
        final UserDto dto = UserDto.builder()
                .username("testuser")
                .password("testpass")
                .build();
        final String requestBody = """
                {
                    "username": "testuser",
                    "password": "testpass"
                }
                """;

        // WHEN
        when(authService.authenticate(dto)).thenReturn(JwtDto.builder().token(token).build());

        // THEN
        mockMvc.perform(post(AUTH_LOGIN_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpectAll(
                        status().isOk(),
                        result -> {
                            var responseBody = result.getResponse().getContentAsString();
                            assert responseBody.contains(token);
                        });
    }

    @Test
    @DisplayName("[LOGIN] Invalid credentials")
    void login_whenCredentialsAreInvalid_thenStatusIsUnauthorized() throws Exception {
        // GIVEN
        final UserDto dto = UserDto.builder()
                .username("invaliduser")
                .password("invalidpass")
                .build();
        final String requestBody = """
                {
                    "username": "invaliduser",
                    "password": "invalidpass"
                }
                """;

        // WHEN
        when(authService.authenticate(dto)).thenThrow(new BadCredentialsException("Bad credentials"));

        // THEN
        mockMvc.perform(post(AUTH_LOGIN_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isUnauthorized());
    }
}
