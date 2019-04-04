package com.app.teste.colecionando.Modelos;

import com.google.firebase.database.Exclude;

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

    @Exclude // faz com que essa váriavel não vá p/ o banco de dados
    public void setSenha(String senha) {
        this.senha = senha;
    }

    @Exclude //
    public String getSenha() {
        return senha;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

}