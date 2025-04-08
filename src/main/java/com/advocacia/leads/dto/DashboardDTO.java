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

    public long getTotalUsuarios() {
        return totalUsuarios;
    }

    public void setTotalUsuarios(long totalUsuarios) {
        this.totalUsuarios = totalUsuarios;
    }

    public long getUsuariosAtivos() {
        return usuariosAtivos;
    }

    public void setUsuariosAtivos(long usuariosAtivos) {
        this.usuariosAtivos = usuariosAtivos;
    }

    public long getUsuariosInativos() {
        return usuariosInativos;
    }

    public void setUsuariosInativos(long usuariosInativos) {
        this.usuariosInativos = usuariosInativos;
    }

    public long getTotalLeads() {
        return totalLeads;
    }

    public void setTotalLeads(long totalLeads) {
        this.totalLeads = totalLeads;
    }

    public Map<String, Long> getLeadsUltimos7Dias() {
        return leadsUltimos7Dias;
    }

    public void setLeadsUltimos7Dias(Map<String, Long> leadsUltimos7Dias) {
        this.leadsUltimos7Dias = leadsUltimos7Dias;
    }

    public Map<String, Long> getLeadsPorArea() {
        return leadsPorArea;
    }

    public void setLeadsPorArea(Map<String, Long> leadsPorArea) {
        this.leadsPorArea = leadsPorArea;
    }
}
