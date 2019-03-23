package com.app.teste.colecionando.Activitys;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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

public class CadastroActivity extends AppCompatActivity {

    private Button btnCadastro;
    private TextInputEditText cadastroNome, cadastroEmail, cadastroSenha;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        btnCadastro = findViewById(R.id.btnCadastro);
        cadastroNome = findViewById(R.id.cadastroNome);
        cadastroEmail = findViewById(R.id.cadastroEmail);
        cadastroSenha = findViewById(R.id.cadastroSenha);

        btnCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // VERIFICAR SE TODOS OS CAMPOS FORAM PREENCHIDOS
                if(!cadastroNome.getText().toString().isEmpty() && !cadastroEmail.getText().toString().isEmpty()
                        && !cadastroSenha.getText().toString().isEmpty()){
                    Usuário usuario = new Usuário(cadastroNome.getText().toString(),
                            cadastroEmail.getText().toString(), cadastroSenha.getText().toString());
                    cadastrarUsuarioFirebase(usuario);
                }else{
                    Toast.makeText(getContext(), "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void cadastrarUsuarioFirebase(Usuário us){ // CADASTRAR USUÁRIO NO FIREBASE
        mAuth = ConfigFirebase.getFirebaseAuth();
        mAuth.createUserWithEmailAndPassword(
                us.getEmail(), us.getSenha()
        ).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) { // Com esse método é possivel verificar se foi
                                                                     // cadastrado o usuário
                if (task.isSuccessful()){
                    Toast.makeText(getContext(),
                            "Cadastro realizado com sucesso", Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    // TRATAMENTO DE EXCEÇÃO //
                    String exceção = "";
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
                    Toast.makeText(getContext(), exceção, Toast.LENGTH_SHORT).show();

                }
            }
        });

    }

    private Context getContext() { // RETORNAR CONTEXTO DA TELA
        return this;
    }
}
