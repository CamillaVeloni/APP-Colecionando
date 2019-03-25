package com.app.teste.colecionando.Activitys;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.app.teste.colecionando.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Dando a responsabilidade da action bar p/ o toolbar
        Toolbar toolBar = findViewById(R.id.tool_bar);
        toolBar.setTitle("Colecionando");
        // Método para que a toolbar funcione em versões anteriores do android
        setSupportActionBar(toolBar);
    }
}
