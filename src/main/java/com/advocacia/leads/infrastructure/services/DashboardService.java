package com.advocacia.leads.infrastructure.services;

import com.advocacia.leads.dto.DashboardDTO;
import com.advocacia.leads.infrastructure.repositories.LeadRepository;
import com.advocacia.leads.infrastructure.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DashboardService {

    private final UsuarioRepository usuarioRepository;
    private final LeadRepository leadRepository;

    public DashboardDTO gerarDashboard() {
        log.info("Iniciando geração de dados do dashboard...");

        DashboardDTO dto = new DashboardDTO();

        try {
            // Informações de usuários
            long totalUsuarios = usuarioRepository.count();
            long usuariosAtivos = usuarioRepository.countByAtivoTrue();
            long usuariosInativos = usuarioRepository.countByAtivoFalse();
            dto.setTotalUsuarios(totalUsuarios);
            dto.setUsuariosAtivos(usuariosAtivos);
            dto.setUsuariosInativos(usuariosInativos);

            log.debug("Usuários: total={}, ativos={}, inativos={}", totalUsuarios, usuariosAtivos, usuariosInativos);

            // Informações de leads
            long totalLeads = leadRepository.count();
            dto.setTotalLeads(totalLeads);
            log.debug("Total de leads: {}", totalLeads);

            // Agrupar leads por área de interesse (com tratamento de null)
            Map<String, Long> porArea = leadRepository.findAll().stream()
                    .collect(Collectors.groupingBy(
                            lead -> {
                                try {
                                    return lead.getAreaInteresse() != null
                                            ? lead.getAreaInteresse().name()
                                            : "NÃO INFORMADO";
                                } catch (Exception e) {
                                    log.warn("Erro ao acessar areaInteresse de um lead: {}", e.getMessage());
                                    return "ERRO";
                                }
                            },
                            Collectors.counting()
                    ));

            dto.setLeadsPorArea(porArea);
            log.debug("Leads por área: {}", porArea);

            // Leads registrados nos últimos 7 dias
            Map<String, Long> ultimos7Dias = new LinkedHashMap<>();
            LocalDate hoje = LocalDate.now();
            for (int i = 6; i >= 0; i--) {
                LocalDate dia = hoje.minusDays(i);
                long count = leadRepository.countByDataRegistroBetween(
                        dia.atStartOfDay(), dia.plusDays(1).atStartOfDay()
                );
                ultimos7Dias.put(dia.toString(), count);
            }

            dto.setLeadsUltimos7Dias(ultimos7Dias);
            log.debug("Leads dos últimos 7 dias: {}", ultimos7Dias);

            log.info("Dashboard gerado com sucesso.");

        } catch (Exception e) {
            log.error("Erro ao gerar dados do dashboard: {}", e.getMessage(), e);
        }

        return dto;
    }
}
