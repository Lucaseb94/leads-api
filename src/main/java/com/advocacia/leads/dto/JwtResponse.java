package com.advocacia.leads.dto;

public class JwtResponse {
    private String token;

    public JwtResponse() {
        // construtor vazio obrigatório para o Jackson
    }

    public JwtResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
