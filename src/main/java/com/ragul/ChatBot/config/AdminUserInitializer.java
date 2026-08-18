package com.ragul.ChatBot.config;

import com.ragul.ChatBot.entity.AdminUser;
import com.ragul.ChatBot.repository.AdminUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminUserInitializer implements CommandLineRunner {

    private final AdminUserRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.admin.username}")
    private String username;

    @Value("${app.admin.password}")
    private String password;

    @Override
    public void run(String... args) throws Exception {
        if (repository.existsByUsername(username)) {
            return;
        }

        AdminUser admin = AdminUser.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .role("ADMIN")
                .build();

        repository.save(admin);

        System.out.println(
                "Initial admin user created: "
                        + username
        );

    }
}
