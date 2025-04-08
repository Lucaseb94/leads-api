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
        StringBuilder sb = new StringBuilder();
        sb.append("Olá, ").append(nomeAdvogado).append("!\n\n")
                .append("Segue a lista de leads compatíveis:\n\n");

        for (Lead l : leads) {
            sb.append("- Nome: ").append(l.getNome()).append("\n")
                    .append("  Email: ").append(l.getEmail()).append("\n")
                    .append("  Telefone: ").append(l.getTelefone()).append("\n")
                    .append("  Área: ").append(l.getAreaInteresse()).append("\n")
                    .append("  Região: ").append(l.getRegiao()).append("\n")
                    .append("  Criado em: ").append(l.getDataRegistro()).append("\n\n");
        }
        return sb.toString();
    }
}
