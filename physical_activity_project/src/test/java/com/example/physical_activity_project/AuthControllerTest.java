package com.example.physical_activity_project;
import com.example.physical_activity_project.dto.LoginRequest;
import com.example.physical_activity_project.model.Permission;
import com.example.physical_activity_project.model.Role;
import com.example.physical_activity_project.model.User;
import com.example.physical_activity_project.repository.IUserRepository;
import com.example.physical_activity_project.services.IPermissionService;
import com.example.physical_activity_project.services.IRoleService;
import com.example.physical_activity_project.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private IRoleService roleService;

    @MockBean
    private JwtUtil jwtUtil;

    private User testUser;

    @Autowired
    private IPermissionService permissionService;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();

        // Crear permisos de ejemplo y guardarlos
        Permission permiso1 = new Permission();
        permiso1.setName("PERMISO_1");
        permiso1 = permissionService.save(permiso1);

        List<Permission> permisos = List.of(permiso1);

        // Crear rol y asignarle permisos
        Role role = new Role();
        role.setName("USER");
        role.setDescription("Rol de prueba");
        role = roleService.save(role, permisos);

        // Crear usuario de prueba
        testUser = new User();
        testUser.setName("Carlos Pérez");
        testUser.setInstitutionalEmail("carlos.perez@icesi.edu.co");
        testUser.setPassword(passwordEncoder.encode("trainer123"));
        testUser.setRole(role);

        userRepository.save(testUser);

        when(jwtUtil.generateToken(any(), any())).thenReturn("mockedJwtToken");
    }

    @Test
    void loginSuccess_shouldReturnToken() throws Exception {
        String loginRequest = """
            {
                "institutionalEmail": "carlos.perez@icesi.edu.co",
                "password": "trainer123"
            }
            """;

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("mockedJwtToken"));
    }

    @Test
    void loginFail_shouldReturnUnauthorized() throws Exception {
        String loginRequest = """
            {
                "institutionalEmail": "carlos.perez@icesi.edu.co",
                "password": "wrongPassword"
            }
            """;

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequest))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void loginFail_userNotFound_shouldThrowException() throws Exception {
        String loginRequest = """
        {
            "institutionalEmail": "nonexistent@icesi.edu.co",
            "password": "anyPassword"
        }
        """;

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequest))
                .andExpect(status().isUnauthorized()); // RuntimeException mapea a 500
    }

}
