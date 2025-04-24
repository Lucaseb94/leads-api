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

import java.util.Arrays;
import java.util.List;

@RestController
@CrossOrigin(origins = "https://lucaseb94.github.io")
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
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> listarLeadsPorArea(@PathVariable String area) {
        // Log explícito para confirmar o valor recebido:
        System.out.println("Valor recebido para a área: " + area);

        try {
            AreaDireito areaEnum = AreaDireito.valueOf(area.toUpperCase());
            return ResponseEntity.ok(leadService.listarPorArea(areaEnum));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body("Área inválida: " + area + ". Valores válidos: " + Arrays.toString(AreaDireito.values()));
        }
    }

    @PutMapping
    public ResponseEntity<String> atualizarLead(@RequestBody LeadDTO leadDTO) {
        leadService.atualizar(leadDTO);
        return ResponseEntity.ok("Lead atualizado com sucesso.");
    }
}
