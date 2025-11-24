package com.example.physical_activity_project.controller.rest;

import com.example.physical_activity_project.dto.auth.SignUpDTO;
import com.example.physical_activity_project.model.Role;
import com.example.physical_activity_project.model.User;
import com.example.physical_activity_project.services.IRoleService;
import com.example.physical_activity_project.services.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class SignUpController {

    private final IUserService userService;
    private final IRoleService roleService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignUpDTO request) {
        try {

            // 1. Validar si ya existe el correo
            if (userService.findByInstitutionalEmail(request.getInstitutionalEmail()).isPresent()) {
                return ResponseEntity
                        .status(409) // 409 Conflict
                        .body("An account with this email already exists.");
            }

            // 2. Crear el usuario
            User user = new User();
            user.setName(request.getName());
            user.setInstitutionalEmail(request.getInstitutionalEmail());
            user.setPassword(request.getPassword());
            user.setCreatedAt(new Timestamp(System.currentTimeMillis()));

            // 3. Rol por defecto
            Role userRole = roleService.getRoleByName("Admin")
                    .orElseThrow(() -> new RuntimeException("Role not found"));

            user.setRole(userRole);

            // 4. Guardar
            userService.save(user);
            return ResponseEntity.ok("User created successfully");

        } catch (Exception e) {
            return ResponseEntity
                    .status(500)
                    .body("Error creating user: " + e.getMessage());
        }
    }
}
