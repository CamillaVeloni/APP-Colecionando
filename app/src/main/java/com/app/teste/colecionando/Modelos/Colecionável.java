package com.app.teste.colecionando.Modelos;

import com.app.teste.colecionando.Ajuda.UsuárioFirebase;
import com.app.teste.colecionando.ConfiguraçãoFirebase.ConfigFirebase;
import com.google.firebase.database.DatabaseReference;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Colecionável {

    private String idColecionavel, data;
    private String nome, descrição;
    private String valor, etiquetaCustomizada;
    private String categoria, localCompra;
    private boolean boolAdquirido;
    private boolean boolPublico;
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

        Locale local = new Locale("pt", "BR");
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", local);
        String data = dateFormat.format(date);
        this.data = data;

        this.nome = nome;
        this.descrição = descrição;
        this.valor = valor;
        this.categoria = categoria;
        this.boolAdquirido = boolAdquirido;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
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

    public boolean isBoolPublico() {
        return boolPublico;
    }

    public void setBoolPublico(boolean boolPublico) {
        this.boolPublico = boolPublico;
    }


    // ARRUMAR PARTE DE MINHA_COLEÇÃO. 1- ABA PARA COLECIONÁVEL JÁ ADQUIRIDO. 2 - ABA PARA COLECIONÁVEL AINDA NÃO ADQUIRIDO
    public void salvarColecionável(){ // SALVAR COLECIONÁVEL DENTRO DO FIREBASE = NA 'MINHA_COLEÇÃO'

        DatabaseReference databaseReference = ConfigFirebase.getFirebaseDatabase().
                child("minha_coleção");
        databaseReference.child(UsuárioFirebase.getIdentificadorUsuario()) // id_usuário
                .child(getIdColecionavel()) // id_colecionável
                .setValue(this); // setando dados

        if(this.boolPublico){
            salvarColecionávelPublico();
        }
    }

    private void salvarColecionávelPublico(){ // SALVAR COLECIONÁVEL DENTRO DO FIREBASE = NA PARTE PUBLICA -- 'GALERIA_DE_COLECIONAVEIS'
        DatabaseReference databaseReference = ConfigFirebase.getFirebaseDatabase().
                child("galeria_coleções");
        databaseReference.child(getCategoria()) // id_usuário
                .child(getIdColecionavel()) // id_colecionável
                .setValue(this); // setando dados

    }

}
