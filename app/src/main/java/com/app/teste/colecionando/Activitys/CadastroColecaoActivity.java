package com.app.teste.colecionando.Activitys;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;

import com.app.teste.colecionando.R;

public class CadastroColecaoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_colecao);

        Toolbar toolbar = findViewById(R.id.toolbar_mColec);
        setSupportActionBar(toolbar);


    }

}
