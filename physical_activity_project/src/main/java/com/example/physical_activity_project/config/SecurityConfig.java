package com.example.physical_activity_project.config;

import com.example.physical_activity_project.security.CustomUserDetailsService;
import com.example.physical_activity_project.security.filters.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;



@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public UserDetailsService userDetailsService() {
        return new CustomUserDetailsService();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }

    @Bean
    @Order(1)
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .securityMatcher("/mvc/**")
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/mvc/**").permitAll()
                        .requestMatchers("/mvc/login", "/css/**", "/js/**").permitAll()
                        .requestMatchers("/h2-console/**").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/mvc/login")           // URL personalizada para mostrar login
                        .loginProcessingUrl("/mvc/login")  // URL que procesa el login
                        .defaultSuccessUrl("/mvc/users", true)  // Redirección después del login exitoso
                        .failureUrl("/mvc/login?error")    // Redirección en caso de error
                        .usernameParameter("username") // Nombre del campo username
                        .passwordParameter("password") // Nombre del campo password
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/mvc/login?logout")
                        .permitAll()
                )
                .build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain securityRestFilterChain(HttpSecurity http) throws Exception {
        return http
                .securityMatcher("/api/**")
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/api/**").permitAll()
                        .requestMatchers("/h2-console/**").permitAll()
                        .anyRequest().authenticated()
                )
                .headers(headers -> headers.frameOptions(frame -> frame.disable())) // Para acceder a H2 Console
                .sessionManagement(t -> t.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // No tener sesiones stateful
                .build();
    }

    private CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true); // Permitir credenciales, esto significa que las cookies, encabezados de autorización o certificados TLS pueden ser incluidos en las solicitudes
        configuration.addAllowedOriginPattern("*"); // Permitir cualquier origen
        configuration.addAllowedHeader("*"); // Permitir cualquier encabezado
        configuration.addAllowedMethod("*"); // Permitir cualquier método (GET, POST, PUT, DELETE, etc.)
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
