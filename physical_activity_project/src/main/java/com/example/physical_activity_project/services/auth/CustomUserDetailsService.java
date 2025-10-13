package com.example.physical_activity_project.services.auth;

import com.example.physical_activity_project.model.User;
import com.example.physical_activity_project.security.CustomUserDetails;
import com.example.physical_activity_project.services.IUserService;
import com.example.physical_activity_project.services.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserServiceImpl userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findByEmail(username)
                .orElseThrow(() -> {
                    return new UsernameNotFoundException("Usuario no encontrado: " + username);
                });

        return new CustomUserDetails(user);
    }
}
