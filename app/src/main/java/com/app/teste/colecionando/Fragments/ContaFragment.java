package com.app.teste.colecionando.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.app.teste.colecionando.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContaFragment extends Fragment {


    public ContaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_conta, container, false);
    }

    // ARRUMANDO O MENU PARA O FRAGMENT 'MINHA CONTA' //
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        MenuItem item = menu.findItem(R.id.action_search); // deixando o item 'pesquisar' fique invisivel nesse fragment
        item.setVisible(false);
        super.onCreateOptionsMenu(menu, inflater);
    }

}
