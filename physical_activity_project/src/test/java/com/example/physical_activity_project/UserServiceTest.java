package com.example.physical_activity_project;

import com.example.physical_activity_project.model.Role;
import com.example.physical_activity_project.model.User;
import com.example.physical_activity_project.repository.IUserRepository;
import com.example.physical_activity_project.services.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private IUserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private Role role;
    private User user;
    private User savedUser;

    @BeforeEach
    void setUp() {
        role = new Role();
        role.setName("Prueba");
        role.setDescription("Rol de prueba");

        // Usuario nuevo
        user = new User();
        user.setId(null);
        user.setName("Karol");
        user.setInstitutionalEmail("karol@icesi.edu.co");
        user.setPassword("12345");
        user.setRole(role);

        // Usuario guardado
        savedUser = new User();
        savedUser.setId(1l);
        savedUser.setName("Karol");
        savedUser.setInstitutionalEmail("karol@icesi.edu.co");
        savedUser.setPassword("$2a$hashedpassword");
        savedUser.setRole(role);
    }

    @Test
    void saveUser_success() {
        when(passwordEncoder.encode(any())).thenReturn("hashedpassword");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        User result = userService.save(user);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Karol", result.getName());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void saveUser_withoutRole_throwsException() {
        user.setRole(null);
        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.save(user));
        assertEquals("Usuario debe tener un rol asignado", exception.getMessage());
    }

    @Test
    void getAllUsers_success() {
        when(userRepository.findAll()).thenReturn(Arrays.asList(savedUser));

        List<User> users = userService.getAllUsers();

        assertNotNull(users);
        assertEquals(1, users.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void getUserById_success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(savedUser));

        Optional<User> userOptional = userService.getUserById(1L);

        assertTrue(userOptional.isPresent());
        assertEquals("Karol", userOptional.get().getName());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void deleteById_success() {
        when(userRepository.existsById(1L)).thenReturn(true);
        doNothing().when(userRepository).deleteById(1L);

        userService.deleteById(1L);

        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteById_userNotFound_throwsException() {
        when(userRepository.existsById(1L)).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.deleteById(1L));
        assertEquals("Usuario no encontrado", exception.getMessage());
    }

    @Test
    void findByEmail_success() {
        when(userRepository.findByInstitutionalEmail("karol@icesi.edu.co")).thenReturn(Optional.of(savedUser));

        Optional<User> userOptional = userService.findByEmail("karol@icesi.edu.co");

        assertTrue(userOptional.isPresent());
        assertEquals("Karol", userOptional.get().getName());
        verify(userRepository, times(1)).findByInstitutionalEmail("karol@icesi.edu.co");
    }

    @Test
    void changeUserRole_success() {
        Role newRole = new Role();
        newRole.setName("NuevoRol");
        newRole.setDescription("Rol actualizado");

        when(userRepository.findById(1L)).thenReturn(Optional.of(savedUser));
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        User updatedUser = userService.changeUserRole(1L, newRole);

        assertNotNull(updatedUser);
        assertEquals("NuevoRol", updatedUser.getRole().getName());
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(savedUser);
    }

    @Test
    void changeUserRole_userNotFound_throwsException() {
        Role newRole = new Role();
        newRole.setName("NuevoRol");

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> userService.changeUserRole(1L, newRole));

        assertEquals("Usuario no encontrado", exception.getMessage());
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void changeUserRole_newRoleIsNull_throwsException() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(savedUser));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> userService.changeUserRole(1L, null));

        assertEquals("El nuevo rol no puede ser nulo", exception.getMessage());
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void initializedUsers_encodesPasswords() {
        User rawUser = new User();
        rawUser.setId(2L);
        rawUser.setName("RawUser");
        rawUser.setInstitutionalEmail("raw@icesi.edu.co");
        rawUser.setPassword("plaintext");
        rawUser.setRole(role);

        when(userRepository.findAll()).thenReturn(Arrays.asList(rawUser));
        when(passwordEncoder.encode("plaintext")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(rawUser);

        userService.initializedUsers();

        assertTrue(rawUser.getPassword().startsWith("encodedPassword"));
        verify(userRepository, times(1)).save(rawUser);
    }
}
