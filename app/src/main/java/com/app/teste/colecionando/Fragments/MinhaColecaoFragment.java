package com.app.teste.colecionando.Fragments;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.app.teste.colecionando.Activitys.CadastroColecaoActivity;
import com.app.teste.colecionando.Activitys.MainActivity;
import com.app.teste.colecionando.Adapter.AdapterRecyclerView;
import com.app.teste.colecionando.Ajuda.UsuárioFirebase;
import com.app.teste.colecionando.ConfiguraçãoFirebase.ConfigFirebase;
import com.app.teste.colecionando.Modelos.Colecionável;
import com.app.teste.colecionando.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MinhaColecaoFragment extends Fragment {

    public MinhaColecaoFragment() {
        // Required empty public constructor
    }

    private Context context;
    private RecyclerView recyclerMinhaColeção;
    private List<Colecionável> coleção = new ArrayList<>();
    private DatabaseReference coleçãoUsuarioRef;
    private BaseQuickAdapter adapter;
    private Activity fragActivity;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_minha_colecao, container, false);
        fragActivity = this.getActivity();
        coleçãoUsuarioRef = ConfigFirebase.getFirebaseDatabase() // Recuperando todos os colecionável do usuário através do UID
                .child("minha_coleção").child(UsuárioFirebase.getIdentificadorUsuario());

        // CONFIGURANDO FLOATING ACTION BUTTON
        FloatingActionButton floatingActionButton = ((MainActivity) getActivity()).getFloatingActionButton();
        if (floatingActionButton != null) {
            floatingActionButton.show();
        }
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // Click do fab
                startActivity(new Intent(fragActivity.getApplicationContext(), CadastroColecaoActivity.class));
            }
        });

        // CONFIGURANDO RECYCLER VIEW
        inicializarRecyclerView();
        inicializarAdapter();
        inicializarData();
        //

        return view;
    }


    public void inicializarRecyclerView(){ // Setando layout do recycler view, colocando um layout_manager
        recyclerMinhaColeção = view.findViewById(R.id.recyclerMinhaColeção);
        recyclerMinhaColeção.setLayoutManager(new LinearLayoutManager(context));
        recyclerMinhaColeção.setHasFixedSize(true);
    }


    public void inicializarAdapter(){ // Criando um adapter para o recycler view e setando ele no mesmo
        adapter = new AdapterRecyclerView(coleção);
        int mFirstPageItemCount = 4;
        adapter.setNotDoAnimationCount(mFirstPageItemCount);
        adapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT);
    }

    public void inicializarData(){

        coleçãoUsuarioRef.addValueEventListener(new ValueEventListener() { // Recuperando dados da minha_coleção passando para lista
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                coleção.clear();
                for(DataSnapshot data : dataSnapshot.getChildren()){
                    coleção.add(data.getValue(Colecionável.class));
                }
                Collections.reverse(coleção);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void onAttach(Context context) {
        this.context = context;
        super.onAttach(context);
    }

}
