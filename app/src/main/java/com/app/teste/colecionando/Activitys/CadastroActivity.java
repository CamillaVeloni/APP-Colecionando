package com.app.teste.colecionando.Activitys;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.app.teste.colecionando.Ajuda.UsuárioFirebase;
import com.app.teste.colecionando.ConfiguraçãoFirebase.ConfigFirebase;
import com.app.teste.colecionando.Modelos.Usuário;
import com.app.teste.colecionando.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.pd.chocobar.ChocoBar;

import studio.carbonylgroup.textfieldboxes.ExtendedEditText;
import studio.carbonylgroup.textfieldboxes.TextFieldBoxes;

public class CadastroActivity extends AppCompatActivity {

    private Button btnCadastro, btnVoltarLogin;
    private ExtendedEditText cadastroNome, cadastroEmail, cadastroSenha;
    private FirebaseAuth mAuth;
    private Usuário usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        btnCadastro = findViewById(R.id.btnCadastrar);
        btnVoltarLogin = findViewById(R.id.btnVoltarLogin);
        cadastroNome = findViewById(R.id.txtNome_cadastro);
        cadastroEmail = findViewById(R.id.txtEmail_cadastro);
        cadastroSenha = findViewById(R.id.txtSenha_cadastro);
        usuario = new Usuário();

        btnCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // VERIFICAR SE TODOS OS CAMPOS FORAM PREENCHIDOS
                if(!cadastroNome.getText().toString().isEmpty() && !cadastroEmail.getText().toString().isEmpty()
                        && !cadastroSenha.getText().toString().isEmpty()){
                    usuario.setEmail(cadastroEmail.getText().toString());
                    usuario.setNome(cadastroNome.getText().toString());
                    usuario.setSenha(cadastroSenha.getText().toString());
                    cadastrarUsuarioFirebase();
                }else{
                    chocoBarPadrao("Preencha todos os campos!");
                }
            }
        });
        btnVoltarLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void cadastrarUsuarioFirebase(){ // CADASTRAR USUÁRIO NO FIREBASE
        mAuth = ConfigFirebase.getmAuth();
        mAuth.createUserWithEmailAndPassword(
                usuario.getEmail(), usuario.getSenha()
        ).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) { // Com esse método é possivel verificar se foi
                                                                     // cadastrado o usuário
                if (task.isSuccessful()){
                    // ADICIONANDO UM NOME NO PERFIL DO USUÁRIO -- VALE RESSALTAR QUE É O DISPLAY NAME
                    UsuárioFirebase.atualizarNomeUsuario(usuario.getNome());

                    chocoBarPadrao("Cadastro realizado com sucesso");

                    finish();

                }else{
                    // TRATAMENTO DE EXCEÇÃO //
                    String exceção;
                    try {
                        throw task.getException();
                    }catch (FirebaseAuthWeakPasswordException e){ // Tratamento quando a senha não é forte
                        exceção = "A senha tem que ter no mínimo 6 caracteres"; // < 6

                    }catch (FirebaseAuthInvalidCredentialsException e){ // Tratamento quando o e-mail está
                        exceção = "Insira um e-mail válido";            // fora do padrão

                    }catch (FirebaseAuthUserCollisionException e){      // Tratamento quando o e-mail já foi
                        exceção = "O e-mail inserido já possui cadastro"; // cadastrado

                    }catch (Exception e){ // Quando não é nenhum dos de cima
                        exceção = "Não foi possivel ser feito o cadastro. " + e.getMessage();
                        e.printStackTrace(); // escreve no console qual foi o erro
                    }
                    chocoBarPadrao(exceção);
                }
            }
        });

    }

    private void chocoBarPadrao(String text){
        ChocoBar.builder().setActivity(CadastroActivity.this)
                .setText(text)
                .setDuration(ChocoBar.LENGTH_SHORT)
                .build()
                .show();
    }

    private Context getContext() { // RETORNAR CONTEXTO DA TELA
        return this;
    }
}
