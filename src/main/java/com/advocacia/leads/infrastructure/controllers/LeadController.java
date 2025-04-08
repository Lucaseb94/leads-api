package com.advocacia.leads.infrastructure.controllers;

import com.advocacia.leads.domain.AreaDireito;
import com.advocacia.leads.domain.Lead;
import com.advocacia.leads.dto.LeadDTO;
import com.advocacia.leads.infrastructure.services.LeadService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/leads")
public class LeadController {

    private final LeadService leadService;

    public LeadController(LeadService leadService) {
        this.leadService = leadService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Lead criarLead(@Valid @RequestBody LeadDTO leadDTO) {
        return leadService.criarLead(leadDTO);
    }

    @GetMapping("/{area}")
    @PreAuthorize("hasAuthority('ROLE_ADVOGADO')")
    public List<Lead> listarLeadsPorArea(@PathVariable String area) {
        AreaDireito areaEnum = AreaDireito.valueOf(area.toUpperCase());
        return leadService.listarPorArea(areaEnum);
    }

    @PutMapping
    public ResponseEntity<String> atualizarLead(@RequestBody LeadDTO leadDTO) {
        leadService.atualizar(leadDTO);
        return ResponseEntity.ok("Lead atualizado com sucesso.");
    }
}
