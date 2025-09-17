package com.example.physical_activity_project;

import com.example.physical_activity_project.model.*;
import com.example.physical_activity_project.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.physical_activity_project.services.impl.RoleServiceImpl;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RoleServiceTest {

    @Mock
    private IRoleRepository roleRepository;

    @Mock
    private IRolePermissionRepository rolePermissionRepository;

    @InjectMocks
    private RoleServiceImpl roleService;

    private Role role;
    private Role savedRole;
    private Permission permiso1;
    private Permission permiso2;

    @BeforeEach
    void setUp() {
        // Permisos de ejemplo
        permiso1 = new Permission();
        permiso1.setId(1L);
        permiso1.setName("PERMISO_1");

        permiso2 = new Permission();
        permiso2.setId(2L);
        permiso2.setName("PERMISO_2");

        // Rol simple
        role = new Role();
        role.setId(null);
        role.setName("PRUEBA");
        role.setDescription("Rol de prueba");

        // Rol guardado (simula base de datos)
        savedRole = new Role();
        savedRole.setId(1L);
        savedRole.setName("PRUEBA");
        savedRole.setDescription("Rol de prueba");
    }

    @Test
    void getPermissions_roleWithoutPermissions_returnsEmptyList() {
        when(roleRepository.findPermissionsByRoleId(1L)).thenReturn(Collections.emptyList());

        List<Permission> permisos = roleService.getPermissions(1L);

        assertNotNull(permisos);
        assertTrue(permisos.isEmpty());
        verify(roleRepository, times(1)).findPermissionsByRoleId(1L);
    }

    @Test
    void saveRole_success() {
        List<Permission> permisos = Arrays.asList(permiso1, permiso2);

        when(roleRepository.save(any(Role.class))).thenReturn(savedRole);
        when(rolePermissionRepository.save(any(RolePermission.class))).thenReturn(null);

        Role result = roleService.save(role, permisos);

        assertNotNull(result);
        assertEquals("PRUEBA", result.getName());

        verify(roleRepository, times(1)).save(role);
        verify(rolePermissionRepository, times(2)).save(any(RolePermission.class));
    }

    @Test
    void saveRole_withoutPermissions_throwsException() {
        Role role = new Role();
        role.setName("RolTest");
        role.setDescription("Rol de prueba");
        // Caso 1: lista de permisos nula
        RuntimeException exception1 = assertThrows(RuntimeException.class, () ->
                roleService.save(role, null));
        assertEquals("El rol debe tener al menos un permiso", exception1.getMessage());
        // Caso 2: lista de permisos vacía
        RuntimeException exception2 = assertThrows(RuntimeException.class, () ->
                roleService.save(role, List.of()));
        assertEquals("El rol debe tener al menos un permiso", exception2.getMessage());
    }


    @Test
    void updateRole_success() {
        Role updatedDetails = new Role();
        updatedDetails.setName("ROL_ACTUALIZADO");
        updatedDetails.setDescription("Descripción actualizada");

        List<Permission> newPermissions = Arrays.asList(permiso1, permiso2);

        when(roleRepository.findById(1L)).thenReturn(Optional.of(savedRole));
        when(roleRepository.save(savedRole)).thenReturn(savedRole);
        doNothing().when(rolePermissionRepository).deleteAllByRole(savedRole);
        when(rolePermissionRepository.save(any(RolePermission.class))).thenReturn(null);

        Role result = roleService.update(1L, updatedDetails, newPermissions);

        assertNotNull(result);
        assertEquals("ROL_ACTUALIZADO", result.getName());
        assertEquals("Descripción actualizada", result.getDescription());

        verify(roleRepository, times(1)).findById(1L);
        verify(roleRepository, times(1)).save(savedRole);
        verify(rolePermissionRepository, times(1)).deleteAllByRole(savedRole);
        verify(rolePermissionRepository, times(2)).save(any(RolePermission.class));
    }

    @Test
    void updateRole_roleNotFound_throwsException() {
        Role updatedDetails = new Role();
        updatedDetails.setName("ROL_ACTUALIZADO");

        List<Permission> newPermissions = Arrays.asList(permiso1, permiso2);

        when(roleRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> roleService.update(1L, updatedDetails, newPermissions));

        assertEquals("Rol no encontrado", exception.getMessage());

        verify(roleRepository, times(1)).findById(1L);
        verify(roleRepository, times(0)).save(any());
        verify(rolePermissionRepository, times(0)).deleteAllByRole(any());
        verify(rolePermissionRepository, times(0)).save(any());
    }


    @Test
    void getAllRoles_success() {
        when(roleRepository.findAll()).thenReturn(Collections.singletonList(savedRole));

        List<Role> roles = roleService.getAllRoles();

        assertNotNull(roles);
        assertEquals(1, roles.size());
        verify(roleRepository, times(1)).findAll();
    }

    @Test
    void getRoleById_success() {
        when(roleRepository.findById(1L)).thenReturn(Optional.of(savedRole));

        Optional<Role> roleOptional = roleService.getRoleById(1L);

        assertTrue(roleOptional.isPresent());
        assertEquals("PRUEBA", roleOptional.get().getName());
        verify(roleRepository, times(1)).findById(1L);
    }

    @Test
    void getRoleByName_success() {
        when(roleRepository.findByName("PRUEBA")).thenReturn(Optional.of(savedRole));

        Optional<Role> roleOptional = roleService.getRoleByName("PRUEBA");

        assertTrue(roleOptional.isPresent());
        assertEquals(1L, roleOptional.get().getId());
        verify(roleRepository, times(1)).findByName("PRUEBA");
    }

    @Test
    void deleteById_success() {
        when(roleRepository.findById(1L)).thenReturn(Optional.of(savedRole));
        doNothing().when(rolePermissionRepository).deleteAllByRole(savedRole);
        doNothing().when(roleRepository).delete(savedRole);

        roleService.deleteById(1L);

        // Verificamos que se eliminaron los permisos asociados
        verify(rolePermissionRepository, times(1)).deleteAllByRole(savedRole);

        // Verificamos que se eliminó el rol
        verify(roleRepository, times(1)).delete(savedRole);
    }

    @Test
    void deleteById_roleNotFound_throwsException() {
        when(roleRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> roleService.deleteById(1L));

        assertEquals("Rol no encontrado", exception.getMessage());
        verify(roleRepository, times(0)).delete(any());
        verify(rolePermissionRepository, times(0)).deleteAllByRole(any());
    }

    @Test
    void addPermissionToRole_success() {
        Permission newPermission = new Permission();
        newPermission.setId(3L);
        newPermission.setName("PERMISO_3");

        when(roleRepository.findById(1L)).thenReturn(Optional.of(savedRole));
        when(rolePermissionRepository.findByRoleAndPermission(savedRole, newPermission))
                .thenReturn(Optional.empty());
        when(rolePermissionRepository.save(any(RolePermission.class))).thenReturn(null);

        Role updatedRole = roleService.addPermissionToRole(1L, newPermission);

        assertNotNull(updatedRole);
        verify(roleRepository, times(1)).findById(1L);
        verify(rolePermissionRepository, times(1)).findByRoleAndPermission(savedRole, newPermission);
        verify(rolePermissionRepository, times(1)).save(any(RolePermission.class));
    }

    @Test
    void addPermissionToRole_roleNotFound_throwsException() {
        Permission newPermission = new Permission();
        newPermission.setId(3L);
        newPermission.setName("PERMISO_3");

        when(roleRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> roleService.addPermissionToRole(1L, newPermission));

        assertEquals("Rol no encontrado", exception.getMessage());
        verify(rolePermissionRepository, times(0)).save(any());
    }

    @Test
    void addPermissionToRole_permissionAlreadyExists_throwsException() {
        Permission existingPermission = new Permission();
        existingPermission.setId(1L);
        existingPermission.setName("EXISTENTE");

        when(roleRepository.findById(1L)).thenReturn(Optional.of(savedRole));
        when(rolePermissionRepository.findByRoleAndPermission(savedRole, existingPermission))
                .thenReturn(Optional.of(new RolePermission()));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> roleService.addPermissionToRole(1L, existingPermission));

        assertEquals("El permiso ya está asignado a este rol", exception.getMessage());
        verify(rolePermissionRepository, times(1)).findByRoleAndPermission(savedRole, existingPermission);
        verify(rolePermissionRepository, times(0)).save(any());
    }


    @Test
    void removePermissionFromRole_success() {
        Permission permisoToRemove = new Permission();
        permisoToRemove.setId(2L);
        permisoToRemove.setName("PERMISO_2");

        RolePermission rp = new RolePermission();
        rp.setPermission(permisoToRemove);
        rp.setRole(savedRole);

        when(roleRepository.findById(1L)).thenReturn(Optional.of(savedRole));
        when(rolePermissionRepository.countByRole(savedRole)).thenReturn(2L);
        when(rolePermissionRepository.findByRoleAndPermission(savedRole, permisoToRemove))
                .thenReturn(Optional.of(rp));
        doNothing().when(rolePermissionRepository).delete(rp);

        Role result = roleService.removePermissionFromRole(1L, permisoToRemove);

        assertNotNull(result);
        verify(roleRepository, times(1)).findById(1L);
        verify(rolePermissionRepository, times(1)).countByRole(savedRole);
        verify(rolePermissionRepository, times(1)).findByRoleAndPermission(savedRole, permisoToRemove);
        verify(rolePermissionRepository, times(1)).delete(rp);
    }

    @Test
    void removePermissionFromRole_lastPermission_throwsException() {
        Permission onlyPermission = new Permission();
        onlyPermission.setId(1L);
        onlyPermission.setName("PERMISO_UNICO");

        when(roleRepository.findById(2L)).thenReturn(Optional.of(savedRole));
        when(rolePermissionRepository.countByRole(savedRole)).thenReturn(1L);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> roleService.removePermissionFromRole(2L, onlyPermission));

        assertEquals("No se puede eliminar el último permiso del rol. Asigne otro permiso antes o elimine el rol.",
                exception.getMessage());
        verify(rolePermissionRepository, times(1)).countByRole(savedRole);
    }

    @Test
    void removePermissionFromRole_permissionNotAssigned_throwsException() {
        Permission nonAssigned = new Permission();
        nonAssigned.setId(5L);
        nonAssigned.setName("NO_ASIGNADO");

        when(roleRepository.findById(1L)).thenReturn(Optional.of(savedRole));
        when(rolePermissionRepository.countByRole(savedRole)).thenReturn(2L);
        when(rolePermissionRepository.findByRoleAndPermission(savedRole, nonAssigned))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> roleService.removePermissionFromRole(1L, nonAssigned));

        assertEquals("El permiso no está asignado a este rol", exception.getMessage());
        verify(rolePermissionRepository, times(1)).findByRoleAndPermission(savedRole, nonAssigned);
        verify(rolePermissionRepository, times(0)).delete(any());
    }

    @Test
    void removePermissionFromRole_roleNotFound_throwsException() {
        Permission anyPermission = new Permission();
        anyPermission.setId(1L);
        anyPermission.setName("CUALQUIERA");

        when(roleRepository.findById(10L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> roleService.removePermissionFromRole(10L, anyPermission));

        assertEquals("Rol no encontrado", exception.getMessage());
    }


}
