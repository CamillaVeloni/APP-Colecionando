package com.app.teste.colecionando.Fragments;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
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


    private Activity fragActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_minha_colecao, container, false);

        fragActivity = this.getActivity();

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


        return view;
    }

}
