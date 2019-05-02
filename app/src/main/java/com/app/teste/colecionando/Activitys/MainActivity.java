package com.app.teste.colecionando.Activitys;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.app.teste.colecionando.ConfiguraçãoFirebase.ConfigFirebase;
import com.app.teste.colecionando.Fragments.ColecoesFragment;
import com.app.teste.colecionando.Fragments.ContaFragment;
import com.app.teste.colecionando.Fragments.MinhaColecaoFragment;
import com.app.teste.colecionando.R;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth mAuth;
    private MinhaColecaoFragment fragMinhaColecão;
    private FragmentTransaction transaction;
    public FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = ConfigFirebase.getFirebaseAuth();
        fragMinhaColecão = new MinhaColecaoFragment();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar); // usado para que a toolbar funcione bem com versões anteriores

        fab = findViewById(R.id.fab);

        // CONFIGURAÇÃO DO NAV. DRAWER
        DrawerLayout drawer = findViewById(R.id.drawer_layout); // Recuperando o layout drawer que fica
                                                                // no activity_main

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(     // Criação da barra que fica no menu
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        // Passando: o contexto, o objeto que recupera a interface, toolbar (onde o toggle é configurado),
        // os 2 últimos parametros são para pessoas com deficiencias (textos)
        drawer.addDrawerListener(toggle); // configurar o toggle no drawer
        toggle.syncState(); // o toggle vai ser sincronizado e carregado novamente

        NavigationView navigationView = findViewById(R.id.nav_view); // Referência do layout de navegação
        navigationView.setNavigationItemSelectedListener(this); // Método de navegação na própria activity

        // EDITANDO INFORMAÇÕES DO NAV_HEADER_MAIN (FOTO PERFIL, NOME, EMAIL)
        FirebaseUser user = mAuth.getCurrentUser();

        View headerView = navigationView.getHeaderView(0); // Referência da view de cabeçalho
        TextView nome_nav = headerView.findViewById(R.id.txtNome_nav_header);
        TextView email_nav = headerView.findViewById(R.id.txtEmail_nav_header);
        CircleImageView foto_nav = headerView.findViewById(R.id.image_nav_header);

        nome_nav.setText(user.getDisplayName());
        email_nav.setText(user.getEmail());

        Uri url = user.getPhotoUrl(); // pegando url da foto perfil no firebase
        if (url != null) { // Verificar se tem uma foto de perfil no firebase
            Glide.with(MainActivity.this).load(url).into(foto_nav); // Carregar img do firebase p/ imgview
        }else{
            foto_nav.setImageResource(R.drawable.perfil_nav); // img padrão do perfil
        }

        if (savedInstanceState == null) {
            setTitle(R.string.title_activity_main);
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.FrameLayoutContainer, fragMinhaColecão)
                    .commit();
        }/*
        // SETANDO O FRAGMENT PRINCIPAL - MINHA COLEÇÃO //
        transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.FrameLayoutContainer, fragMinhaColecão);
        transaction.commit();*/
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
        // Verificar qual a opção clicada através do id
        if (id == R.id.nav_mColeção) { // Entrar fragment Minha Coleção
            setTitle(R.string.title_activity_main);
            transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.FrameLayoutContainer, fragMinhaColecão);
            transaction.commit();

        } else if (id == R.id.nav_coleções) { // Entrar fragment Galeria de Coleções
            ColecoesFragment colecFragment = new ColecoesFragment();
            setTitle(R.string.title_galeria_colec);
            transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.FrameLayoutContainer, colecFragment);
            transaction.commit();

        } else if (id == R.id.nav_mConta) { // Entrar fragment Minha Conta
            setTitle(R.string.title_edit_conta);
            ContaFragment contaFragment = new ContaFragment();
            contaFragment.setHasOptionsMenu(true); // Método para que a fragment possa reescrever o onCreateOptionsMenu
            transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.FrameLayoutContainer, contaFragment);
            transaction.commit();

        } else if (id == R.id.nav_contato) {

        } else if (id == R.id.nav_sobre) {

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
    public boolean onOptionsItemSelected(MenuItem item) { // Ações para a action bar ficam aqui
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


    // GETTER PARA O FLOATING_ACTION_BUTTON
    public FloatingActionButton getFloatingActionButton() {
        return fab;
    }


    private Context getContext() { // RETORNAR CONTEXTO DA TELA
        return this;
    }
}
