package com.app.teste.colecionando.Fragments;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.teste.colecionando.Activitys.CadastroColecaoActivity;
import com.app.teste.colecionando.Activitys.MainActivity;
import com.app.teste.colecionando.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MinhaColecaoFragment extends Fragment {

    public MinhaColecaoFragment() {
        // Required empty public constructor
    }

    private Context context;
    private RecyclerView recyclerMinhaColeção;
    private Activity fragActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_minha_colecao, container, false);

        fragActivity = this.getActivity();
        recyclerMinhaColeção = view.findViewById(R.id.recyclerMinhaColeção);


        FloatingActionButton floatingActionButton = ((MainActivity) getActivity()).getFloatingActionButton();
        if (floatingActionButton != null) {
            floatingActionButton.show();
        }
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(fragActivity.getApplicationContext(), CadastroColecaoActivity.class));
            }
        });

        // Configuração do recycler View
        recyclerMinhaColeção.setLayoutManager(new LinearLayoutManager(context));
        recyclerMinhaColeção.setHasFixedSize(true);

        //recyclerMinhaColeção.setAdapter();
        return view;
    }

    public void onAttach(Context context) {
        this.context = context;
        super.onAttach(context);
    }

}
