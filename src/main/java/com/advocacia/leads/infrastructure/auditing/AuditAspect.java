package com.advocacia.leads.infrastructure.auditing;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.Arrays;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class AuditAspect {

    private final AuditLogRepository auditLogRepository;

    private String truncate(String value) {
        int MAX_LENGTH = 150;
        if (value == null) return null;
        return value.length() > MAX_LENGTH ? value.substring(0, MAX_LENGTH) + "..." : value;
    }


    @Pointcut("execution(public * com.advocacia.leads.infrastructure.services.*.*(..))")
    public void auditServiceMethods() {}

    @Around("auditServiceMethods()")
    public Object logAudit(ProceedingJoinPoint joinPoint) throws Throwable {
        LocalDateTime startTime = LocalDateTime.now();
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();

        log.info("[AUDIT] Iniciando método '{}' com parâmetros: {}", methodName, Arrays.toString(args));

        Object result;
        String mensagem = "Sucesso";
        try {
            result = joinPoint.proceed();
            log.info("[AUDIT] Método '{}' finalizado com sucesso. Resultado: {}", methodName, result);
        } catch (Throwable t) {
            mensagem = t.getMessage();
            log.error("[AUDIT] Método '{}' falhou com exceção: {}", methodName, mensagem, t);
            throw t;
        }
        LocalDateTime endTime = LocalDateTime.now();

        String usuarioLogado = "Anonimo"; // Valor padrão


        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken)) {
            usuarioLogado = auth.getName();
        }

        AuditLog auditLog = AuditLog.builder()
                .metodo(methodName)
                .parametros(truncate(args != null ? Arrays.toString(args) : ""))
                .resultado(truncate(result != null ? result.toString() : "void"))
                .mensagem(truncate(mensagem))
                .dataHora(endTime)
                .usuario(usuarioLogado)
                .build();


        auditLogRepository.save(auditLog);

        log.info("[AUDIT] Duração do método '{}': {} ms", methodName, java.time.Duration.between(startTime, endTime).toMillis());
        return result;
    }

}
