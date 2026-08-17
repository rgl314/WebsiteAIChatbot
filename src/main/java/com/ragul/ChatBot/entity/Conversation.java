package com.ragul.ChatBot.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "conversations",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_site_conversation",
                        columnNames = {"site_id", "conversation_id"}
                )
        }
)
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Conversation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "site_id", nullable = false)
    private Site site;

    @Column(name = "conversation_id", nullable = false, length = 36)
    private String conversationId;

}
