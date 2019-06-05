package com.app.teste.colecionando.Activitys;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.teste.colecionando.Adapter.AdapterColecionaveis;
import com.app.teste.colecionando.Ajuda.AjudaNetwork;
import com.app.teste.colecionando.Ajuda.ColecionaveisData;
import com.app.teste.colecionando.ConfiguraçãoFirebase.ConfigFirebase;
import com.app.teste.colecionando.Modelos.Colecionável;
import com.app.teste.colecionando.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.forms.sti.progresslitieigb.ProgressLoadingJIGB;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ColecoesListaActivity extends AppCompatActivity {

    private RecyclerView recyclerMuseuLista;
    private List<Colecionável> museuVirtual = new ArrayList<>();
    private DatabaseReference coleçãoMuseuRef;
    private View view_ofList;
    private static int TYPE_ERROR = 0, TYPE_EMPTY = 1;
    private AdapterColecionaveis adapter;
    private String categoriaSelecionada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_colecoes_lista);

        recyclerMuseuLista = findViewById(R.id.recyclerMuseuVirtual);
        Toolbar toolbar = findViewById(R.id.toolbar_museuColec);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // colocando botão voltar para activity principal

        Intent intent = getIntent();
        categoriaSelecionada = intent.getStringExtra("categoriaSelecionada");
        Log.d("Categoria", "Categoria selecionada: " + categoriaSelecionada);


        inicializarRecyclerView();
        inicializarAdapter();
        inicializarData();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        if ( getFragmentManager().getBackStackEntryCount() > 0)
        {
            getFragmentManager().popBackStack();
            return;
        }
        super.onBackPressed();
    }

    public void inicializarRecyclerView(){ // Setando layout do recycler view, colocando um layout_manager
        recyclerMuseuLista = findViewById(R.id.recyclerMuseuVirtual);
        recyclerMuseuLista.setLayoutManager(new LinearLayoutManager(this));
        recyclerMuseuLista.setHasFixedSize(true);
    }

    public void inicializarAdapter(){

        adapter = new AdapterColecionaveis(museuVirtual);
        int mFirstPageItemCount = 4;
        adapter.setNotDoAnimationCount(mFirstPageItemCount);
        adapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT); // Animação quando estiver 'descendo' pelo recyclerView

        recyclerMuseuLista.setAdapter(adapter);

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Log.d("Click", "onItemClick");
                Colecionável colecSelecionado = museuVirtual.get(position);
                Intent intent = new Intent(ColecoesListaActivity.this,
                        ColecionavelActivity.class);
                intent.putExtra("colecSelecionado", colecSelecionado);
                intent.putExtra("boolGaleria", true);
                startActivity(intent);

            }
        });

    }

    public void inicializarData(){

        if(categoriaSelecionada == null){
            Log.d("Data", "Entrou na categoria como null");
            coleçãoMuseuRef = ConfigFirebase.getDatabase() // Recuperando TODOS colecionaveis publicos
                    .child("galeria_coleções");
        }else{
            Log.d("Data", "Entrou na categoria como true");
            coleçãoMuseuRef = ConfigFirebase.getDatabase() // Recuperando colecionaveis de determinada categ
                    .child("galeria_coleções")
                    .child(categoriaSelecionada);
        }

        ProgressLoadingJIGB.startLoadingJIGB(getContext(),
                 R.raw.animation_w500_h500, // Travando tela para 'Carregar' - progress loading
                "",0,600,600);

        if(AjudaNetwork.conectadoNet(getContext())){
            coleçãoMuseuRef.addValueEventListener(new ValueEventListener() { // Recuperando dados do museu virtual e passando para lista
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    museuVirtual.clear();

                    if(categoriaSelecionada == null){ // Percorre todas as categorias
                        Log.d("Data", "Entrou na categoria como null 2");
                        for(DataSnapshot categorias : dataSnapshot.getChildren()){
                            for(DataSnapshot colecionavel : categorias.getChildren()){ // Percorre todos os colecionaveis dentro de determinada categoria
                                museuVirtual.add(colecionavel.getValue(Colecionável.class));
                            }
                        }
                    }else{ // Percorre todos os colecionaveis dentro de determinada categoria
                        Log.d("Data", "Entrou na categoria como true 2");
                        for(DataSnapshot colecionavel : dataSnapshot.getChildren()){
                            museuVirtual.add(colecionavel.getValue(Colecionável.class));
                        }
                    }

                    Collections.reverse(museuVirtual);
                    adapter.notifyDataSetChanged();
                    ProgressLoadingJIGB.finishLoadingJIGB(getContext());

                    if(museuVirtual.size() == 0){
                        ProgressLoadingJIGB.finishLoadingJIGB(ColecoesListaActivity.this);
                        errorEmptyList(TYPE_EMPTY);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    ProgressLoadingJIGB.finishLoadingJIGB(ColecoesListaActivity.this);
                    errorEmptyList(TYPE_ERROR);
                }
            });
        }else{
            ProgressLoadingJIGB.finishLoadingJIGB(ColecoesListaActivity.this);
            errorEmptyList(TYPE_ERROR);
        }
    }

    private void errorEmptyList(int type){ // Método para mostrar uma view no recycler view
        if(type == 0){ // 0 - error_network
            view_ofList =
                    getLayoutInflater().inflate(R.layout.error_view,
                            (ViewGroup) recyclerMuseuLista.getParent(),
                            false);
        }else{ // 1 - empty_list
            view_ofList =
                    getLayoutInflater().inflate(R.layout.empty_view,
                            (ViewGroup) recyclerMuseuLista.getParent(),
                            false);
            TextView txtEmpty = view_ofList.findViewById(R.id.txtEmpty);
            txtEmpty.setText(R.string.view_emptyMuseuList);
        }

        adapter.setEmptyView(view_ofList);
    }

    private Context getContext() { // RETORNAR CONTEXTO DA TELA
        return this;
    }

}
