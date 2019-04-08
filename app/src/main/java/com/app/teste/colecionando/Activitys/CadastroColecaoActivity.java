package com.app.teste.colecionando.Activitys;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.app.teste.colecionando.R;
import com.blackcat.currencyedittext.CurrencyEditText;

import java.util.Locale;

public class CadastroColecaoActivity extends AppCompatActivity {

    private Button btnAdd;
    private EditText txtNome, txtDesc, txtComp, txtEtiq;
    private CurrencyEditText txtValor;
    private CheckBox checkAdq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_colecao);

        Toolbar toolbar = findViewById(R.id.toolbar_mColec);
        setSupportActionBar(toolbar);

        // Inicializando componentes
        txtNome = findViewById(R.id.cadastroColec_nome);
        txtDesc = findViewById(R.id.cadastroColec_desc);
        txtComp = findViewById(R.id.cadastroColec_ondeComp);
        txtEtiq = findViewById(R.id.cadastroColec_etiq);
        checkAdq = findViewById(R.id.cadastroColec_checkAd);
        btnAdd = findViewById(R.id.cadastroColec_btnAdd);
        txtValor = findViewById(R.id.cadastroColec_valor);

        // Configurando valor para real
        Locale local = new Locale("pt", "BR");
        txtValor.setLocale(local);

        // Ação de Adicionar na Coleção
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String valor = txtValor.getText().toString();
                Log.d("Teste", "Valor colecionavel: " + valor);
            }
        });
    }

}
