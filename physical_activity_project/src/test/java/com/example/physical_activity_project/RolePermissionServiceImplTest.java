package com.example.physical_activity_project;

import com.example.physical_activity_project.model.Permission;
import com.example.physical_activity_project.model.Role;
import com.example.physical_activity_project.model.RolePermission;
import com.example.physical_activity_project.repository.IRolePermissionRepository;
import com.example.physical_activity_project.services.impl.RolePermissionServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RolePermissionServiceImplTest {

    @Mock
    private IRolePermissionRepository rolePermissionRepository;

    @InjectMocks
    private RolePermissionServiceImpl rolePermissionService;

    private RolePermission rolePermission;
    private Role role;
    private Permission permission;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        role = new Role();
        role.setId(1L);
        role.setName("Admin");

        permission = new Permission();
        permission.setId(10L);
        permission.setName("CREATE_USER");

        rolePermission = new RolePermission();
        rolePermission.setId(100L);
        rolePermission.setRole(role);
        rolePermission.setPermission(permission);
    }


    @Test
    void findAll_returnsListOfRolePermissions() {
        when(rolePermissionRepository.findAll()).thenReturn(List.of(rolePermission));

        List<RolePermission> result = rolePermissionService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Admin", result.get(0).getRole().getName());
        assertEquals("CREATE_USER", result.get(0).getPermission().getName());
        verify(rolePermissionRepository, times(1)).findAll();
    }


    @Test
    void findPermissionIdsByRoleId_returnsListOfIds() {
        when(rolePermissionRepository.findPermissionIdsByRoleId(1L)).thenReturn(List.of(10L, 11L));

        List<Long> result = rolePermissionService.findPermissionIdsByRoleId(1L);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(10L));
        verify(rolePermissionRepository, times(1)).findPermissionIdsByRoleId(1L);
    }

    @Test
    void findPermissionIdsByRoleId_returnsEmptyList_whenNoPermissions() {
        when(rolePermissionRepository.findPermissionIdsByRoleId(99L)).thenReturn(List.of());

        List<Long> result = rolePermissionService.findPermissionIdsByRoleId(99L);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(rolePermissionRepository, times(1)).findPermissionIdsByRoleId(99L);
    }
}