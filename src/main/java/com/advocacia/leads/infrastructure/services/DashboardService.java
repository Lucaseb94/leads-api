package com.advocacia.leads.infrastructure.services;

import com.advocacia.leads.domain.AreaDireito;
import com.advocacia.leads.dto.DashboardDTO;
import com.advocacia.leads.infrastructure.repositories.LeadRepository;
import com.advocacia.leads.infrastructure.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final UsuarioRepository usuarioRepository;
    private final LeadRepository leadRepository;

    public DashboardDTO gerarDashboard() {
        DashboardDTO dto = new DashboardDTO();

        dto.setTotalUsuarios(usuarioRepository.count());
        dto.setUsuariosAtivos(usuarioRepository.countByAtivoTrue());
        dto.setUsuariosInativos(usuarioRepository.countByAtivoFalse());
        dto.setTotalLeads(leadRepository.count());

        Map<String, Long> porArea = leadRepository.findAll().stream()
                .collect(Collectors.groupingBy(
                        lead -> lead.getAreaInteresse().name(),
                        Collectors.counting()
                ));
        dto.setLeadsPorArea(porArea);

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

        return dto;
    }
}
