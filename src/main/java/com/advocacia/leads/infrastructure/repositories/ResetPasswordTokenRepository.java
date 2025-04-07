package com.advocacia.leads.infrastructure.repositories;

import com.advocacia.leads.domain.ResetPasswordToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface ResetPasswordTokenRepository extends JpaRepository<ResetPasswordToken, Long> {

    Optional<ResetPasswordToken> findByToken(String token);

    // Método para limpeza de tokens expirados ou já usados (caso esteja sendo usado em alguma configuração)
    void deleteByExpirationDateBeforeOrUsedTrue(LocalDateTime date);
}
