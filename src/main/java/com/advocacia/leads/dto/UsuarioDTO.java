package com.advocacia.leads.dto;

import com.advocacia.leads.domain.AreaDireito;

public record UsuarioDTO(String nome, String email, AreaDireito especializacao) {}
