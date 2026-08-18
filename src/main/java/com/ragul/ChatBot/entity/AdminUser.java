package com.ragul.ChatBot.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "admin_users",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_admin_username",
                        columnNames = "username"
                )
        }
)
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String username;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(nullable = false, length = 50)
    private String role;

    @Column(nullable = false)
    @Builder.Default
    private boolean enabled = true;

}
