package com.advocacia.leads.infrastructure.scheduling;

import com.advocacia.leads.domain.AreaDireito;
import com.advocacia.leads.domain.Lead;
import com.advocacia.leads.domain.Usuario;
import com.advocacia.leads.infrastructure.repositories.LeadRepository;
import com.advocacia.leads.infrastructure.repositories.UsuarioRepository;
import com.advocacia.leads.infrastructure.services.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DistribuicaoLeadService {

    private final UsuarioRepository usuarioRepository;
    private final LeadRepository leadRepository;
    private final EmailService emailService;

    // Agendado para 06:00 a cada 2 dias
    @Scheduled(cron = "0 0 6 */2 * *")
    public void enviarResumoLeads() {
        LocalDateTime seisDiasAtras = LocalDateTime.now().minusDays(6);
        List<Usuario> advogados = usuarioRepository.findAll().stream()
                .filter(u -> u.getRoles().equals("ROLE_ADVOGADO") && u.isAtivo())
                .toList();

        for (Usuario advogado : advogados) {
            // Aqui, se o campo 'especializacao' do advogado já for do tipo AreaDireito:
            AreaDireito areaEspecializacao = advogado.getEspecializacao();
            if (areaEspecializacao == null) {
                areaEspecializacao = AreaDireito.OUTROS;
            }

            List<Lead> leads = leadRepository.findRecentesPorAreaERegiao(
                    areaEspecializacao, advogado.getRegiao(), seisDiasAtras
            );

            if (!leads.isEmpty()) {
                String corpo = gerarCorpo(leads, advogado.getNome());
                emailService.enviar(advogado.getEmail(), "Resumo de Leads", corpo);
            }
        }
    }

    public void distribuirNovoLead(Lead lead) {
        // Como lead.getAreaInteresse() já é do tipo AreaDireito, usamos direto:
        AreaDireito areaEspecializacao = lead.getAreaInteresse();
        if (areaEspecializacao == null) {
            areaEspecializacao = AreaDireito.OUTROS;
        }

        // Verifique se os advogados são encontrados.
        List<Usuario> advogados = usuarioRepository.findByEspecializacaoAndRegiao(
                areaEspecializacao,
                lead.getRegiao()
        );

        System.out.println("Advogados encontrados: " + advogados.size());
        for (Usuario advogado : advogados) {
            System.out.println("Preparando e-mail para: " + advogado.getEmail());
            String corpo = gerarCorpo(List.of(lead), advogado.getNome());
            emailService.enviar(advogado.getEmail(), "Novo Lead Recebido", corpo);
        }
    }


    private String gerarCorpo(List<Lead> leads, String nomeAdvogado) {
        if (nomeAdvogado == null || nomeAdvogado.isBlank()) {
            nomeAdvogado = "Dr";
        }

        StringBuilder sb = new StringBuilder();

        // Início do HTML
        sb.append("<html>");
        sb.append("<head>");
        sb.append("<style>");
        // Exemplo simples de CSS; customize conforme sua necessidade
        sb.append("body { font-family: Arial, sans-serif; }");
        sb.append("h2 { color: #2E86C1; }");
        sb.append("table { width: 100%; border-collapse: collapse; }");
        sb.append("th, td { padding: 8px; text-align: left; border-bottom: 1px solid #ddd; }");
        sb.append("tr:hover { background-color: #f5f5f5; }");
        sb.append("</style>");
        sb.append("</head>");
        sb.append("<body>");

        // Saudação e título
        sb.append("<h2>Olá, ").append(nomeAdvogado).append("!</h2>");
        sb.append("<p>Segue a lista de leads compatíveis:</p>");

        // Estrutura de tabela para os leads
        sb.append("<table>");
        sb.append("<thead>");
        sb.append("<tr>");
        sb.append("<th>Nome</th>");
        sb.append("<th>Email</th>");
        sb.append("<th>Telefone</th>");
        sb.append("<th>Área</th>");
        sb.append("<th>Região</th>");
        sb.append("<th>Criado em</th>");
        sb.append("</tr>");
        sb.append("</thead>");
        sb.append("<tbody>");

        // Formatação de data (usando LocalDateTime e DateTimeFormatter)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        for (Lead l : leads) {
            sb.append("<tr>");
            sb.append("<td>").append(l.getNome() != null ? l.getNome() : "Não informado").append("</td>");
            sb.append("<td>").append(l.getEmail() != null ? l.getEmail() : "Não informado").append("</td>");
            sb.append("<td>").append(l.getTelefone() != null ? l.getTelefone() : "Não informado").append("</td>");
            // Note que l.getAreaInteresse() é um enum; o método toString() dele normalmente retorna o nome do valor.
            sb.append("<td>").append(l.getAreaInteresse() != null ? l.getAreaInteresse().toString() : "Não informado").append("</td>");
            sb.append("<td>").append(l.getRegiao() != null ? l.getRegiao() : "Não informado").append("</td>");
            sb.append("<td>");
            if (l.getDataRegistro() != null) {
                sb.append(l.getDataRegistro().format(formatter));
            } else {
                sb.append("Data desconhecida");
            }
            sb.append("</td>");
            sb.append("</tr>");
        }

        sb.append("</tbody>");
        sb.append("</table>");

        // Rodapé do email
        sb.append("<p>Atenciosamente,<br/>Equipe Leads API</p>");

        // Fim do HTML
        sb.append("</body>");
        sb.append("</html>");

        return sb.toString();
    }
}
