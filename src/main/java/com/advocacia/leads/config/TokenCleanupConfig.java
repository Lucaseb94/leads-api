package com.advocacia.leads.config;

import com.advocacia.leads.infrastructure.repositories.ResetPasswordTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;

@Configuration
@EnableScheduling
public class TokenCleanupConfig {

    @Autowired
    private ResetPasswordTokenRepository tokenRepository;

    // Executa diariamente às 3 AM
    @Scheduled(cron = "0 0 3 * * ?")
    public void cleanExpiredTokens() {
        tokenRepository.deleteByExpirationDateBeforeOrUsedTrue(LocalDateTime.now());
    }
}
