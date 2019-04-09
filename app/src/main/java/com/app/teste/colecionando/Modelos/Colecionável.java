package com.app.teste.colecionando.Modelos;

import com.app.teste.colecionando.Ajuda.UsuárioFirebase;
import com.app.teste.colecionando.ConfiguraçãoFirebase.ConfigFirebase;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

public class Colecionável {

    private String idColecionavel;
    private String nome, descrição;
    private String valor, etiquetaCustomizada;
    private String categoria, localCompra;
    private boolean boolAdquirido;
    private List<String> imagens;

    public Colecionável() {

    }

    public Colecionável(String nome, String descrição, String valor, String etiquetaCustomizada, String categoria, String localCompra, boolean boolAdquirido, List<String> imagens) {
        this.nome = nome;
        this.descrição = descrição;
        this.valor = valor;
        this.etiquetaCustomizada = etiquetaCustomizada;
        this.categoria = categoria;
        this.localCompra = localCompra;
        this.boolAdquirido = boolAdquirido;
        this.imagens = imagens;
    }

    public Colecionável(String nome, String descrição, String valor, String categoria, boolean boolAdquirido) {
        DatabaseReference dataRef = ConfigFirebase.getFirebaseDatabase().
                child("minha_coleção");
        setIdColecionavel(dataRef.push().getKey()); // Automaticamente é gerado o id da coleção (id_colecionável)

        this.nome = nome;
        this.descrição = descrição;
        this.valor = valor;
        this.categoria = categoria;
        this.boolAdquirido = boolAdquirido;
    }

    public String getIdColecionavel() {
        return idColecionavel;
    }

    public void setIdColecionavel(String idColecionavel) {
        this.idColecionavel = idColecionavel;
    }

    public List<String> getImagens() {
        return imagens;
    }

    public void setImagens(List<String> imagens) {
        this.imagens = imagens;
    }

    public String getNome() {
        return nome;
    }

    public String getDescrição() {
        return descrição;
    }

    public String getValor() {
        return valor;
    }

    public String getEtiquetaCustomizada() {
        return etiquetaCustomizada;
    }

    public String getCategoria() {
        return categoria;
    }

    public String getLocalCompra() {
        return localCompra;
    }

    public boolean isBoolAdquirido() {
        return boolAdquirido;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setDescrição(String descrição) {
        this.descrição = descrição;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public void setEtiquetaCustomizada(String etiquetaCustomizada) {
        this.etiquetaCustomizada = etiquetaCustomizada;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public void setLocalCompra(String localCompra) {
        this.localCompra = localCompra;
    }

    public void setBoolAdquirido(boolean boolAdquirido) {
        this.boolAdquirido = boolAdquirido;
    }

    public void salvarColecionável(){ // SALVAR COLECIONÁVEL DENTRO DO FIREBASE
        DatabaseReference databaseReference = ConfigFirebase.getFirebaseDatabase().
                child("minha_coleção");
        databaseReference.child(UsuárioFirebase.getIdentificadorUsuario()) // id_usuário
                .child(getIdColecionavel()) // id_colecionável
                .setValue(this); // setando dados
    }

}
