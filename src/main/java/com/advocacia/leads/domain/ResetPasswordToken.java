package com.advocacia.leads.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "reset_password_token")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResetPasswordToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String token;

    // Se desejar, você pode armazenar o email do usuário também
    @Column(nullable = false)
    private String email;

    @Column(name = "expiration_date", nullable = false)
    private LocalDateTime expirationDate;

    @Column(nullable = false)
    private boolean used;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;
}
