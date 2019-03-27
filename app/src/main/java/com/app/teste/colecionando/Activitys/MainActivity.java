package com.app.teste.colecionando.Activitys;

import android.os.Bundle;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.app.teste.colecionando.R;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar); // usado para que a toolbar funcione bem com versões anteriores

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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
        // Método chamado sempre que o usuário clicar em uma das opções do NavigationView
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
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Ações para o action bar ficam aqui
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }else if(id == R.id.action_sair){

        }

        return super.onOptionsItemSelected(item);
    }
}
