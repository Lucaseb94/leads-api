package com.advocacia.leads.infrastructure.services;

import com.advocacia.leads.domain.Lead;
import com.advocacia.leads.domain.AreaDireito;
import com.advocacia.leads.infrastructure.repositories.LeadRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LeadService {
    private final LeadRepository leadRepository;

    public LeadService(LeadRepository leadRepository) {
        this.leadRepository = leadRepository;
    }

    public List<Lead> listarPorArea(AreaDireito area) {
        return leadRepository.findByAreaInteresse(area);
    }
}
