package com.app.teste.colecionando.Activitys;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.app.teste.colecionando.ConfiguraçãoFirebase.ConfigFirebase;
import com.app.teste.colecionando.R;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = ConfigFirebase.getFirebaseAuth();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar); // usado para que a toolbar funcione bem com versões anteriores

        // CONFIGURAÇÃO DO NAV. DRAWER
        DrawerLayout drawer = findViewById(R.id.drawer_layout); // Recuperando o layout drawer que fica
                                                                // no activity_main

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(     // Criação da barra que fica no menu
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        // Passando: o contexto, o objeto que recupera a interface, toolbar (onde o toggle é configurado),
        // os 2 últimos parametros são para pessoas com deficiencias (textos)
        drawer.addDrawerListener(toggle); // configurar o toggle no drawer
        toggle.syncState(); // o toggle vai ser sincronizado e carregado novamente

        NavigationView navigationView = findViewById(R.id.nav_view); // Nele é que o usuário pode navegar pelas opções
        navigationView.setNavigationItemSelectedListener(this); // Método de navegação na própria activity
    }

    @Override
    public void onBackPressed() { // Método usado quando o usuário pressionar o botão voltar
        DrawerLayout drawer = findViewById(R.id.drawer_layout); // quando o nav está aberto
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    // OPÇÕES PARA O NAVIGATIONDRAWER //
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) { // Implementado por causa do uso da interface
        // Método chamado sempre que o usuário clicar em uma das opções do Nav
        int id = item.getItemId(); // Recuperar o id do item clicado

        if (id == R.id.nav_coleções) { // Verificar qual a opção clicada através do id

        } else if (id == R.id.nav_mColeção) {

        } else if (id == R.id.nav_mConta) {

        } else if (id == R.id.nav_config) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    // MENU //
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflar o menu
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Ações para o action bar ficam aqui

        //noinspection SimplifiableIfStatement
        switch (item.getItemId()) {
            case R.id.action_sair:
                deslogarUsuario();
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void deslogarUsuario(){

        try {
            mAuth.signOut();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
