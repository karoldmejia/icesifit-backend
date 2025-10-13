package com.example.physical_activity_project.config;

import com.example.physical_activity_project.model.Role;
import com.example.physical_activity_project.services.auth.CustomUserDetailsService;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.userdetails.User;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Import(PasswordConfig.class)
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;
    private final PasswordEncoder passwordEncoder;


    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(customUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/h2-console/**")
                )
                .authenticationProvider(authenticationProvider())
                .authorizeHttpRequests(authz -> authz
                        // Recursos públicos
                        .requestMatchers("/main.css", "/css/**", "/js/**", "/images/**").permitAll()
                        .requestMatchers("/mvc/login", "/mvc/signup/form").permitAll()
                        .requestMatchers("POST", "/mvc/signup/form").permitAll()
                        .requestMatchers("/h2-console/**").permitAll() // Para H2 en desarrollo

                        // Endpoints específicos por roles
                        .requestMatchers("/mvc/users/").hasRole("Admin")
                        .requestMatchers("/mvc/trainer/**").hasRole("Trainer")
                        .requestMatchers("/mvc/users/add", "/mvc/users/edit/**", "/mvc/users/delete/**")
                        .hasRole("Admin")
                        .requestMatchers("/mvc/roles/**", "/mvc/permissions/**").hasRole("Admin")

                        // Cualquier otra solicitud requiere autenticación
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/mvc/login")
                        .loginProcessingUrl("/mvc/authenticate")
                        .defaultSuccessUrl("/mvc/users", true) //Cambiar a false cuando tengamos vistas diferentes para cada rol
                        .failureUrl("/mvc/login?error=true")
                        .usernameParameter("username")
                        .passwordParameter("password")
                        .permitAll()
                )
                .headers(headers -> headers
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin
                        )
                )
                .logout(logout -> logout
                        .logoutUrl("/mvc/logout")
                        .logoutSuccessUrl("/mvc/login?logout=true")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )
                .build();
    }
}
