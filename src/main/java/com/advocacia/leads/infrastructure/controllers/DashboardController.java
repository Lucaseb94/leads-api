package com.advocacia.leads.infrastructure.controllers;

import com.advocacia.leads.dto.DashboardDTO;
import com.advocacia.leads.infrastructure.services.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DashboardDTO> obterDashboard() {
        DashboardDTO dashboard = dashboardService.gerarDashboard();
        return ResponseEntity.ok(dashboard);
    }
}
