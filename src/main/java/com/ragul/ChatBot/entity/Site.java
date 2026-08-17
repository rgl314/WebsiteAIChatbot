package com.ragul.ChatBot.entity;

import jakarta.persistence.*;
import lombok.*;

import javax.annotation.security.DenyAll;



@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "sites")
public class Site {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "site_id", nullable = false, unique = true, length = 100)
    private String siteId;

    @Column(name = "public_key", nullable = false, unique = true, length = 100)
    private String publicKey;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(nullable = false, unique = true, length = 255)
    private String domain;

}
