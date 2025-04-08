package com.advocacia.leads.dto;

import lombok.Data;

import java.util.Map;

@Data
public class DashboardDTO {
    private long totalUsuarios;
    private long usuariosAtivos;
    private long usuariosInativos;
    private long totalLeads;
    private Map<String, Long> leadsPorArea;
    private Map<String, Long> leadsUltimos7Dias;
}
