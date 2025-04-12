package com.advocacia.leads.infrastructure.auditing;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;

    private String truncate(String value, int maxLength) {
        if (value == null) return null;
        return value.length() > maxLength ? value.substring(0, maxLength) : value;
    }


    public AuditLogService(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    public void registrar(String usuario, String acao, String entidade, String entidadeId, String detalhes) {
        AuditLog log = new AuditLog();

        // Trunca os valores para que não ultrapassem 255 caracteres
        log.setUsuario(truncate(usuario, 255));
        log.setAcao(truncate(acao, 255));
        log.setEntidade(truncate(entidade, 255));
        log.setEntidadeId(truncate(entidadeId, 255));

        // Se a ação indicar um resultado muito grande, você pode optar por uma mensagem padrão,
        // ou ainda truncar o valor de 'detalhes'
        if (detalhes != null && detalhes.length() > 255) {
            log.setDetalhes("obteve lista muito grande de leads");
            System.out.println("INPUT: obteve lista muito grande de leads");
        } else {
            log.setDetalhes(truncate(detalhes, 255));
        }

        // Se houver outros campos que armazenem dados dinâmicos, você também deve truncá-los:
        // Exemplo, se você tiver 'metodo', 'parametros', 'resultado' e 'mensagem'
        log.setMetodo(truncate(log.getMetodo(), 255));
        log.setParametros(truncate(log.getParametros(), 255));
        log.setResultado(truncate(log.getResultado(), 255));
        log.setMensagem(truncate(log.getMensagem(), 255));

        log.setDataHora(LocalDateTime.now());

        auditLogRepository.save(log);
    }
}
