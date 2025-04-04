package com.advocacia.leads.dto;

public class UserRegistrationRequest {
    private String email;
    private String senha;
    private String especializacao; // Adicione o campo

    // Getters e Setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getEspecializacao() {
        return especializacao; // Agora retorna um valor
    }

    public void setEspecializacao(String especializacao) {
        this.especializacao = especializacao;
    }
}
