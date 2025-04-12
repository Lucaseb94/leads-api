package com.advocacia.leads.infrastructure.auditing;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "audit_log")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String usuario;     // Quem realizou a ação

    private String acao;        // Qual ação (CREATE, UPDATE, DELETE, etc.)

    private String entidade;    // Qual entidade (ex: "Lead")

    private String entidadeId;  // ID do registro da entidade

    @Column(name = "detalhes", columnDefinition = "TEXT")
    private String detalhes;

    private String metodo;      // Nome do método chamado

    private String parametros;  // Parâmetros passados para o método

    private String resultado;   // Resultado retornado pela operação

    private String mensagem;    // Mensagem de sucesso ou erro

    @Column(name = "data_execucao", nullable = false)
    private LocalDateTime dataHora;
}
