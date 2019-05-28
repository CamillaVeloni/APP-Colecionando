package com.app.teste.colecionando.ConfiguraçãoFirebase;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ConfigFirebase {

    private static DatabaseReference database;
    private static FirebaseAuth mAuth;
    private static StorageReference storage;

    static{
        database = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance().getReference();
    }

    public static DatabaseReference getDatabase() {
        return database;
    }

    public static FirebaseAuth getmAuth() {
        return mAuth;
    }

    public static StorageReference getStorage() {
        return storage;
    }

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

    // MÉTODO PARA RECUPERAR A INSTÂNCIA DO STORAGE //
    public static StorageReference getFirebaseStorage(){
        if (storage == null) { // se for igual a nulo quer dizer que é preciso criar uma
            // instancia do firebaseAuth
            storage = FirebaseStorage.getInstance().getReference(); // colocando o objeto 'mAuth' para poder gerenciar
                                                                    // os usuários
        }
        return storage;
    }

}
