package com.advocacia.leads.infrastructure.auditing;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;

    public AuditLogService(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    public void registrar(String usuario, String acao, String entidade, String entidadeId, String detalhes) {
        AuditLog log = new AuditLog();
        log.setUsuario(usuario);
        log.setAcao(acao);
        log.setEntidade(entidade);
        log.setEntidadeId(entidadeId);
        log.setDetalhes(detalhes);
        log.setDataHora(LocalDateTime.now());

        auditLogRepository.save(log);
    }
}
