package com.app.teste.colecionando.Modelos;

public class Usuário {

    private String nome, email, senha;

    public Usuário() {

    }
    public Usuário(String nome, String email, String senha) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
    }
    public Usuário(String email, String senha) {
        this.email = email;
        this.senha = senha;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getSenha() {
        return senha;
    }
}