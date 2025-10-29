package com.example.physical_activity_project;

import com.example.physical_activity_project.model.User;
import com.example.physical_activity_project.services.impl.JwtServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.lang.reflect.Field;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class JwtServiceImplTest {

    private JwtServiceImpl jwtService;
    private User user;
    private UsernamePasswordAuthenticationToken auth;

    @BeforeEach
    void setUp() throws Exception {
        jwtService = new JwtServiceImpl();

        setField(jwtService, "secretKey", "bXlzZWNyZXRrZXlteXNlY3JldGtleW15c2VjcmV0a2V5bXlzZWNyZXRrZXk="); // Base64
        setField(jwtService, "expirationTime", 1000L * 60 * 60); 

        user = new User();
        user.setId(1L);
        user.setName("Heiner");
        user.setInstitutionalEmail("heiner@uni.edu");

        auth = new UsernamePasswordAuthenticationToken(
                "Heiner",
                null,
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );
    }

    private void setField(Object target, String fieldName, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }

    @Test
    void shouldGenerateAndValidateTokenCorrectly() {

        String token = jwtService.generateToken(user, auth);

        boolean isValid = jwtService.isTokenValid(token);

        assertThat(token).isNotNull();
        assertThat(isValid).isTrue();
    }

    @Test
    void shouldExtractUsernameFromToken() {

        String token = jwtService.generateToken(user, auth);

        String username = jwtService.extractUsername(token);

        assertThat(username).isEqualTo("Heiner");
    }

    @Test
    void shouldExtractAuthoritiesCorrectly() {

        String token = jwtService.generateToken(user, auth);


        var authorities = jwtService.extractAuthorities(token);

        assertThat(authorities)
                .hasSize(1)
                .extracting(SimpleGrantedAuthority::getAuthority)
                .containsExactly("ROLE_USER");
    }

    @Test
    void shouldReturnUserDetailsFromToken() {

        String token = jwtService.generateToken(user, auth);
        var userDetails = jwtService.getUserDetailsFromToken(token);

        assertThat(userDetails.getUsername()).isEqualTo("Heiner");
        assertThat(userDetails.getAuthorities())
        .extracting(a -> ((SimpleGrantedAuthority) a).getAuthority())
        .contains("ROLE_USER");


    }

    @Test
    void shouldReturnFalseForInvalidToken() {
        String invalidToken = "this.is.not.a.valid.jwt";

        boolean isValid = jwtService.isTokenValid(invalidToken);

        assertThat(isValid).isFalse();
    }

    @Test
    void shouldExtractClaimSuccessfully() {
        String token = jwtService.generateToken(user, auth);

        String subject = jwtService.extractClaim(token, claims -> claims.getSubject());

        assertThat(subject).isEqualTo("Heiner");
    }
}
