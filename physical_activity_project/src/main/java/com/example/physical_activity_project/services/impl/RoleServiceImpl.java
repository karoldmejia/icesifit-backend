package com.example.physical_activity_project.services.impl;

import com.example.physical_activity_project.model.Permission;
import com.example.physical_activity_project.model.Role;
import com.example.physical_activity_project.model.RolePermission;
import com.example.physical_activity_project.repository.IRolePermissionRepository;
import com.example.physical_activity_project.repository.IRoleRepository;
import com.example.physical_activity_project.services.IRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements IRoleService {

    private final IRoleRepository roleRepository;
    private final IRolePermissionRepository rolePermissionRepository;


    public List<Permission> getPermissions(Long roleId) {
        return roleRepository.findPermissionsByRoleId(roleId);
    }
    public Role save(Role role, List<Permission> permissions) {
        if (permissions == null || permissions.isEmpty()) {
            throw new RuntimeException("El rol debe tener al menos un permiso");
        }
        Role savedRole = roleRepository.save(role);
        for (Permission permission : permissions) {
            RolePermission rp = new RolePermission();
            rp.setRole(savedRole);
            rp.setPermission(permission);
            rolePermissionRepository.save(rp);
        }
        return savedRole;
    }

    public Role update(Long roleId, Role roleDetails, List<Permission> permissions) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));

        // Actualizar los datos básicos
        role.setName(roleDetails.getName());
        role.setDescription(roleDetails.getDescription());
        Role updatedRole = roleRepository.save(role);

        // Eliminar permisos antiguos
        rolePermissionRepository.deleteAllByRole(role);

        // Agregar los permisos nuevos
        for (Permission permission : permissions) {
            RolePermission rp = new RolePermission();
            rp.setRole(updatedRole);
            rp.setPermission(permission);
            rolePermissionRepository.save(rp);
        }

        return updatedRole;
    }


    // Obtener todos los roles
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    // Obtener rol por ID
    public Optional<Role> getRoleById(Long id) {
        return roleRepository.findById(id);
    }

    // Obtener rol por nombre
    public Optional<Role> getRoleByName(String name) {
        return roleRepository.findByName(name);
    }

    // Eliminar rol
    public void deleteById(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
        // Primero eliminar todos los permisos asociados
        rolePermissionRepository.deleteAllByRole(role);
        roleRepository.delete(role);
    }


    public Role addPermissionToRole(Long roleId, Permission permission) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));

        // Verificar si ya existe para evitar duplicados
        boolean exists = rolePermissionRepository.findByRoleAndPermission(role, permission).isPresent();
        if (exists) {
            throw new RuntimeException("El permiso ya está asignado a este rol");
        }
        RolePermission rp = new RolePermission();
        rp.setRole(role);
        rp.setPermission(permission);
        rolePermissionRepository.save(rp);

        return role;
    }


    // Remover un permiso de un rol
    public Role removePermissionFromRole(Long roleId, Permission permission) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
        // Contar cuántos permisos tiene el rol
        long totalPermisos = rolePermissionRepository.countByRole(role);

        if (totalPermisos <= 1) {
            throw new RuntimeException(
                    "No se puede eliminar el último permiso del rol. Asigne otro permiso antes o elimine el rol."
            );
        }
        // Verificar si existe esa relación
        RolePermission rp = rolePermissionRepository.findByRoleAndPermission(role, permission)
                .orElseThrow(() -> new RuntimeException("El permiso no está asignado a este rol"));
        rolePermissionRepository.delete(rp);
        return role;
    }


}
