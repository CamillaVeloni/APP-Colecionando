package com.app.teste.colecionando.Activitys;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.app.teste.colecionando.ConfiguraçãoFirebase.ConfigFirebase;
import com.app.teste.colecionando.Modelos.Usuário;
import com.app.teste.colecionando.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.pd.chocobar.ChocoBar;

import studio.carbonylgroup.textfieldboxes.ExtendedEditText;

public class LoginActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    ExtendedEditText loginSenha, loginEmail;
    Button btnEntrar, cadastrarUp;

    @Override
    protected void onStart() {
        FirebaseUser usuárioLogado = mAuth.getCurrentUser(); // VERIFICAR SE O USUÁRIO JÁ TÁ LOGADO
        if (usuárioLogado != null){
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        }
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginEmail = findViewById(R.id.txtEmail_login);
        loginSenha = findViewById(R.id.txtSenha_login);
        cadastrarUp = findViewById(R.id.btnCadastrarUp);
        btnEntrar = findViewById(R.id.btnLogin);
        mAuth = ConfigFirebase.getmAuth();

        cadastrarUp.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) { // ENTRAR NA TELA DE CADASTRO //
                Intent intent = new Intent(LoginActivity.this, CadastroActivity.class);
                startActivity(intent);
            }
        });

        btnEntrar.setOnClickListener(new View.OnClickListener() { // VERIFICAR SE TODOS OS CAMPOS FORAM PREENCHIDOS
            @Override
            public void onClick(View v) {

                if (!loginEmail.getText().toString().isEmpty() && !loginSenha.getText().toString().isEmpty()){
                    Usuário us = new Usuário(loginEmail.getText().toString(), loginSenha.getText().toString());
                    logarUsuario(us);

                }else{
                    chocoBarPadrao("Preencha o(s) campo(s)!");
                    //Toast.makeText(getContext(), "Preencha o(s) campo(s)!", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public void logarUsuario(Usuário us){
        mAuth.signInWithEmailAndPassword(us.getEmail(), us.getSenha()).addOnCompleteListener(
                new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) { // Verificar se a autenticação deu certo ou não
                if(task.isSuccessful()){
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);

                }else{
                    String exceção;
                    try{
                        throw task.getException();
                    }catch (FirebaseAuthInvalidUserException e){ // quando o e-mail do usuário não está cadastrado
                        exceção = "E-mail ainda não cadastrado";        // ou foi desabilitado

                    }catch (FirebaseAuthInvalidCredentialsException e){ // quando a senha está errada
                        exceção = "O e-mail e a senha não correspondem!";

                    }catch (Exception e){
                        exceção = "Não foi possivel fazer o login. " + e.getMessage();
                        e.printStackTrace();
                    }
                    //Toast.makeText(getContext(), exceção, Toast.LENGTH_SHORT).show();
                    chocoBarPadrao(exceção);
                }
            }
        });
    }

    private void chocoBarPadrao(String text){
        ChocoBar.builder().setActivity(LoginActivity.this)
                .setText(text)
                .setDuration(ChocoBar.LENGTH_SHORT)
                .build()
                .show();
    }


    private Context getContext() {
        return this;
    }
}
