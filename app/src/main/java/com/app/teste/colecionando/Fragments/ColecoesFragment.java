package com.app.teste.colecionando.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;

import com.app.teste.colecionando.Activitys.MainActivity;
import com.app.teste.colecionando.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ColecoesFragment extends Fragment {


    public ColecoesFragment() {
        // Required empty public constructor
    }

    private RecyclerView recyclerColecoes; // Instância do RecyclerView
    private Context context;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_colecoes, container, false);

        FloatingActionButton floatingActionButton = ((MainActivity) getActivity()).getFloatingActionButton();
        if (floatingActionButton != null) {
            floatingActionButton.hide();
        }

        /*
        // CONFIGURANDO O RECYCLER VIEW - LISTA COM COLEÇÕES
        recyclerColecoes = view.findViewById(R.id.recyclerMinhaColeção); // Pegando referência do recycler view do layout xml
        recyclerColecoes.setLayoutManager(new LinearLayoutManager(context)); // Colocando o gerenciador de layout
        recyclerColecoes.setHasFixedSize(true);
        */
        return view;
    }

    // GARANTINDO QUE IRÁ PASSAR UM CONTEXTO VÁLIDO
    @Override
    public void onAttach(Context context) {
        this.context = context;
        super.onAttach(context);
    }

}
