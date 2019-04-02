package com.app.teste.colecionando.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_colecoes, container, false);

        /*recyclerColecoes = (RecyclerView) view.findViewById(R.id.recyclerColecoes); // Pegando referência do recycler view do layout xml

        // CONFIGURAÇÃO DO RECYCLER VIEW
        recyclerColecoes.setLayoutManager(new LinearLayoutManager(getActivity().getBaseContext())); // Colocando o gerenciador de layout
        recyclerColecoes.setHasFixedSize(true);
        */
        return view;
    }

}
