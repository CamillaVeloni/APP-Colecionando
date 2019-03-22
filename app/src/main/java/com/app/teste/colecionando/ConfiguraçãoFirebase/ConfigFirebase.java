package com.app.teste.colecionando.ConfiguraçãoFirebase;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ConfigFirebase {

    private static DatabaseReference database;
    private static FirebaseAuth mAuth;

    // MÉTODO PARA RECUPERAR A INSTÂNCIA DO BANCO DE DADOS DO FIREBASE //
    public static DatabaseReference getFirebaseDatabase(){
        if (database == null) { // se for igual a nulo quer dizer que é preciso criar uma
            // instancia do firebaseDatabase
            database = FirebaseDatabase.getInstance().getReference(); // colocando o database p/
            // poder gerenciar o banco de dados
        }
        return database;
    }

    // MÉTODO PARA RECUPERAR A INSTÂNCIA DO AUTENTICADOR DO FIREBASE //
    public static FirebaseAuth getFirebaseAuth(){
        if (mAuth == null) { // se for igual a nulo quer dizer que é preciso criar uma
            // instancia do firebaseAuth
            mAuth = FirebaseAuth.getInstance(); // colocando o objeto 'mAuth' para poder gerenciar
            // os usuários
        }
        return mAuth;
    }

}
