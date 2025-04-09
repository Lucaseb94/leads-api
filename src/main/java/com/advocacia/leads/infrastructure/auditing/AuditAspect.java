package com.advocacia.leads.infrastructure.auditing;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.Arrays;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class AuditAspect {

    private final AuditLogRepository auditLogRepository;

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

        AuditLog auditLog = AuditLog.builder()
                .metodo(methodName)
                .parametros(args != null ? Arrays.toString(args) : "")
                .resultado(result != null ? result.toString() : "void")
                .mensagem(mensagem)
                .dataHora(endTime)
                .usuario("usuario_exemplo")
                .build();

        auditLogRepository.save(auditLog);

        log.info("[AUDIT] Duração do método '{}': {} ms", methodName, java.time.Duration.between(startTime, endTime).toMillis());
        return result;
    }
}
