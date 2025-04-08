package com.advocacia.leads.dto;

import com.advocacia.leads.domain.AreaDireito;

public record LeadDTO(
        Long id,
        String nome,
        String email,
        String telefone,
        String regiao,
        String endereco,           // Campo adicionado
        AreaDireito areaInteresse  // Valor do enum (ex: CIVIL, PENAL, etc.)
) {}
