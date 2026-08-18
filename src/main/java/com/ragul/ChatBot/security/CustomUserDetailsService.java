package com.ragul.ChatBot.security;

import com.ragul.ChatBot.entity.AdminUser;
import com.ragul.ChatBot.repository.AdminUserRepository;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService
        implements UserDetailsService {

    private final AdminUserRepository repository;

    public CustomUserDetailsService(
            AdminUserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        System.out.println(
                "========== USER DETAILS SERVICE =========="
        );

        System.out.println(
                "Username requested: " + username
        );

        AdminUser adminUser =
                repository.findByUsername(username)
                        .orElseThrow(() ->
                                new UsernameNotFoundException(
                                        "User not found: " + username
                                )
                        );

        System.out.println(
                "User found: "
                        + adminUser.getUsername()
        );

        System.out.println(
                "Role: "
                        + adminUser.getRole()
        );

        System.out.println(
                "Enabled: "
                        + adminUser.isEnabled()
        );

        return User.builder()
                .username(adminUser.getUsername())
                .password(adminUser.getPassword())
                .roles(adminUser.getRole())
                .disabled(!adminUser.isEnabled())
                .build();
    }
}