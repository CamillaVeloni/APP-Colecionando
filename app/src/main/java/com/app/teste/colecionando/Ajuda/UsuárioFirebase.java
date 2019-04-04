package com.app.teste.colecionando.Ajuda;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.app.teste.colecionando.Activitys.CadastroActivity;
import com.app.teste.colecionando.ConfiguraçãoFirebase.ConfigFirebase;
import com.app.teste.colecionando.Fragments.ContaFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class UsuárioFirebase {

    static FirebaseAuth usuario;

    public static String getIdentificadorUsuario(){ // RETORNANDO O IDENTIFICADOR ID
        usuario = ConfigFirebase.getFirebaseAuth();

        return usuario.getCurrentUser().getUid();
    }

    public static FirebaseUser getUsuarioAtual(){ // RETORNANDO USUÁRIO COM TODOS OS DADOS
        usuario = ConfigFirebase.getFirebaseAuth();

        return  usuario.getCurrentUser();
    }

    public static boolean atualizarFotoUsuario(Uri urlFoto){ // MÉTODO P/ ATUALIZAR A FOTO DE PERFIL DO USUÁRIO DENTRO DO FIREBASE

        try{
            FirebaseUser usuario = getUsuarioAtual();
            // Criar UserProfileChangeRequest que é usado para atualizar informações do usuário
            UserProfileChangeRequest profileChangeRequestPhoto = new UserProfileChangeRequest.Builder().setPhotoUri(urlFoto).build();

            usuario.updateProfile(profileChangeRequestPhoto).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(!task.isSuccessful()){
                        Log.d("Perfil Usuário", "Ocorreu algum erro ao atualizar a foto de perfil do usuário"); // debugar depois
                    }
                }
            });
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }



    }
    public static boolean atualizarNomeUsuario(String nome){

        try {
            FirebaseUser usuario = getUsuarioAtual();
            UserProfileChangeRequest profileChangeRequestName = new UserProfileChangeRequest.Builder().setDisplayName(nome).build();

            usuario.updateProfile(profileChangeRequestName).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(!task.isSuccessful()){
                        Log.d("Perfil Usuário", "Ocorreu algum erro ao atualizar o nome do usuário");
                    }
                }
            });
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }


}
