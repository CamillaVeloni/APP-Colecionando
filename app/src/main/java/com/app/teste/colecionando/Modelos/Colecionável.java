package com.app.teste.colecionando.Modelos;

import android.support.annotation.NonNull;
import android.util.Log;

import com.app.teste.colecionando.Ajuda.UsuárioFirebase;
import com.app.teste.colecionando.ConfiguraçãoFirebase.ConfigFirebase;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Colecionável implements Serializable {

    private String idColecionavel, data;
    private String nome, descrição;
    private String valor, etiquetaCustomizada;
    private String categoria, localCompra;
    private boolean boolAdquirido;
    private boolean boolPublico;
    private List<String> imagens;

    public Colecionável() {

    }

    public Colecionável(String nome, String descrição, String valor, String categoria, boolean boolAdquirido) {
        DatabaseReference dataRef = ConfigFirebase.getDatabase().
                child("minha_coleção");
        setIdColecionavel(dataRef.push().getKey()); // Automaticamente é gerado o id da coleção (id_colecionável)

        Locale local = new Locale("pt", "BR");
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", local);

        this.data = dateFormat.format(date);
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
    public void salvarColecionável(){ // SALVAR E ATUALIZAR COLECIONÁVEL DENTRO DO FIREBASE = NA 'MINHA_COLEÇÃO'

        DatabaseReference dataRef = ConfigFirebase.getDatabase()
                .child("minha_coleção");
        dataRef.child(UsuárioFirebase.getIdentificadorUsuario()) // id_usuário
                .child(this.idColecionavel) // id_colecionável
                .setValue(this); // setando dados

        if(this.boolPublico){
            salvarColecionávelPublico();
        }
    }

    private void salvarColecionávelPublico(){ // SALVAR E ATUALIZAR COLECIONÁVEL DENTRO DO FIREBASE = NA PARTE PUBLICA -- 'GALERIA_DE_COLECIONAVEIS'
        DatabaseReference dataRef = ConfigFirebase.getDatabase()
                .child("galeria_coleções");
        dataRef.child(this.categoria) // categoria
                .child(this.idColecionavel) // id_colecionável
                .setValue(this); // setando dados

    }

    public void removerColecionável(){ // REMOVENDO COLECIONÁVEL DO DATABASE DO FIREBASE = NA MINHA_COLEÇÃO
        DatabaseReference dataRef = ConfigFirebase.getDatabase()
                .child("minha_coleção")
                .child(UsuárioFirebase.getIdentificadorUsuario())
                .child(this.idColecionavel);

        dataRef.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("Remover", "onSuccess: Sucesso em remover do Database Minha_Coleção");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("Remover", "On Failure: Não foi possivel remover do Database Minha_Coleção" +
                        e.getMessage());
            }
        });

        if(this.boolPublico){
            removerColecionávelPublico(); // DELETAR COLECIONÁVEL DO DATABASE DENTRO DE GALERIA_COLEÇÕES
        }

        for (int i  = 0; i < this.imagens.size(); i++){ // DELETAR IMAGENS DENTRO DO STORAGE
            StorageReference photoRef = ConfigFirebase.getStorage() // ele só vai até .getReference() precisado do .getStorage()
                    .getStorage().getReferenceFromUrl(imagens.get(i));

            photoRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d("Remover", "onSuccess: Sucesso em remover a img do Storage pelo URL");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("Remover", "onFailure: Não foi possível deletar a img do Storage pelo URL" +
                            e.getMessage());
                }
            });
        }

    }


    private void removerColecionávelPublico(){

        DatabaseReference dataRef = ConfigFirebase.getDatabase()
                .child("galeria_coleções")
                .child(this.categoria)
                .child(this.idColecionavel);

        dataRef.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("Remover", "onSuccess: Sucesso em remover do Database Galeria_Coleções");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("Remover", "On Failure: Não foi possivel remover do Galeria_Coleções" +
                        e.getMessage());
            }
        });

    }


}
