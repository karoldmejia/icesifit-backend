package com.example.physical_activity_project;

import com.example.physical_activity_project.dto.auth.LoginDTO;
import com.example.physical_activity_project.dto.auth.TokenResponseDTO;
import com.example.physical_activity_project.model.User;
import com.example.physical_activity_project.security.CustomUserDetails;
import com.example.physical_activity_project.services.IJwtService;
import com.example.physical_activity_project.services.impl.AuthServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class AuthServiceImplTest {

    private IJwtService jwtService;
    private UserDetailsService userDetailsService;
    private PasswordEncoder passwordEncoder;
    private AuthServiceImpl authService;

    @BeforeEach
    void setUp() {
        jwtService = mock(IJwtService.class);
        userDetailsService = mock(UserDetailsService.class);
        passwordEncoder = mock(PasswordEncoder.class);
        authService = new AuthServiceImpl(jwtService, userDetailsService, passwordEncoder);
    }

    @Test
    void login_ShouldReturnToken_WhenCredentialsAreValid() {
        LoginDTO login = new LoginDTO("user@test.com", "1234");

        User user = new User();
        user.setId(1L);
        user.setInstitutionalEmail("user@test.com");
        user.setPassword("encoded1234");


        CustomUserDetails customUserDetails = mock(CustomUserDetails.class);
        when(customUserDetails.getPassword()).thenReturn("encoded1234");
        when(customUserDetails.getUser()).thenReturn(user);
        when(customUserDetails.getAuthorities()).thenReturn(null);

        when(userDetailsService.loadUserByUsername("user@test.com")).thenReturn(customUserDetails);
        when(passwordEncoder.matches("1234", "encoded1234")).thenReturn(true);
        when(jwtService.generateToken(eq(user), ArgumentMatchers.any())).thenReturn("mockedToken");

        TokenResponseDTO response = authService.login(login);

        assertNotNull(response);
        assertEquals("mockedToken", response.getAccessToken());
        verify(jwtService).generateToken(eq(user), any());
        verify(passwordEncoder).matches("1234", "encoded1234");
    }

    @Test
    void login_ShouldThrow_WhenUserNotFound() {
        LoginDTO login = new LoginDTO("unknown@test.com", "pass");
        when(userDetailsService.loadUserByUsername("unknown@test.com")).thenReturn(null);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> authService.login(login));

        assertEquals("Usuario no encontrado", ex.getMessage());
        verify(jwtService, never()).generateToken(any(), any());
    }

    @Test
    void login_ShouldThrow_WhenPasswordInvalid() {
        LoginDTO login = new LoginDTO("user@test.com", "wrong");

        CustomUserDetails customUserDetails = mock(CustomUserDetails.class);
        when(customUserDetails.getPassword()).thenReturn("encoded1234");

        when(userDetailsService.loadUserByUsername("user@test.com")).thenReturn(customUserDetails);
        when(passwordEncoder.matches("wrong", "encoded1234")).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> authService.login(login));

        assertEquals("Contraseña incorrecta", ex.getMessage());
        verify(jwtService, never()).generateToken(any(), any());
    }
}