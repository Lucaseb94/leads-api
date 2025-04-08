package com.advocacia.leads.infrastructure.services;

import com.advocacia.leads.domain.AreaDireito;
import com.advocacia.leads.domain.Lead;
import com.advocacia.leads.dto.LeadDTO;
import com.advocacia.leads.infrastructure.repositories.LeadRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LeadService {

    private final LeadRepository leadRepository;

    public LeadService(LeadRepository leadRepository) {
        this.leadRepository = leadRepository;
    }

    @Transactional
    public void atualizar(LeadDTO dto) {
        Lead lead = leadRepository.findById(dto.id())
                .orElseThrow(() -> new RuntimeException("Lead não encontrado"));
        lead.setNome(dto.nome());
        lead.setEmail(dto.email());
        lead.setTelefone(dto.telefone());
        lead.setRegiao(dto.regiao());
        // Se Lead possuir o campo de área, você pode atualizá-lo aqui
        leadRepository.save(lead);
    }

    // Novo método: lista os leads filtrados pela área de atuação
    @Transactional(readOnly = true)
    public List<Lead> listarPorArea(AreaDireito area) {
        return leadRepository.findAll().stream()
                .filter(lead -> lead.getAreaInteresse() == area)
                .collect(Collectors.toList());
    }
}
