package com.advocacia.leads.dto;

public class UserRegistrationRequest {
    private String email;
    private String nome;
    private String senha;
    private String especializacao; // Adicione o campo

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

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
