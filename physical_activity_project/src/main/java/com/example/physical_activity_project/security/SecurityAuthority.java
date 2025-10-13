package com.example.physical_activity_project.security;

import com.example.physical_activity_project.model.Permission;
import com.example.physical_activity_project.model.RolePermission;
import org.springframework.security.core.GrantedAuthority;


import lombok.AllArgsConstructor;

import java.util.Collection;
import java.util.List;

@AllArgsConstructor
public class SecurityAuthority implements GrantedAuthority {

    private final Permission permission;

    @Override
    public String getAuthority() {
        return permission.getName();
    }

}

