package com.example.physical_activity_project.security;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.physical_activity_project.model.RolePermission;
import com.example.physical_activity_project.model.User;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CustomUserDetails implements UserDetails {

    private final User user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Mapea los permisos de tu rol a GrantedAuthority
        List<SecurityAuthority> authorities = user.getRole().getRolePermissions().stream()
                .map(RolePermission::getPermission)
                .map(SecurityAuthority::new)
                .toList();
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword(); // tu campo se llama password
    }

    @Override
    public String getUsername() {
        return user.getInstitutionalEmail(); // normalmente usamos el email como username
    }

    public User getUser() {
        return user;
    }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }
}
