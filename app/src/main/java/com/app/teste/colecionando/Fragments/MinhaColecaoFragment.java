package com.app.teste.colecionando.Fragments;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.app.teste.colecionando.Adapter.AdapterColecionaveis;
import com.app.teste.colecionando.Ajuda.UsuárioFirebase;
import com.app.teste.colecionando.ConfiguraçãoFirebase.ConfigFirebase;
import com.app.teste.colecionando.Modelos.Colecionável;
import com.app.teste.colecionando.R;
import com.google.firebase.database.DatabaseReference;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

import java.util.ArrayList;
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
    private int posiçãoAnterior;
    private boolean restaurado;
    private Colecionável colecRestaurado;
    private AdapterColecionaveis adapter;
    private Activity fragActivity;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_minha_colecao, container, false);

        setHasOptionsMenu(true);
        fragActivity = this.getActivity();
        posiçãoAnterior = 0;
        colecRestaurado = new Colecionável();
        coleçãoUsuarioRef = ConfigFirebase.getDatabase() // Recuperando todos os colecionável do usuário através do UID
                .child("minha_coleção").child(UsuárioFirebase.getIdentificadorUsuario());

        // CONFIGURANDO ABAS
        FragmentPagerItemAdapter pagerAdapter = new FragmentPagerItemAdapter(
                this.getChildFragmentManager(), FragmentPagerItems.with(context)
                .add(R.string.titulo_pager_mColeção, MColecaoPagerFragment.class)
                .add(R.string.titulo_pager_mClassif, MClassifPagerFragment.class)
                .create()
        );

        ViewPager viewPager = view.findViewById(R.id.viewPager);
        SmartTabLayout viewPagerTab = view.findViewById(R.id.viewPagerTab);

        viewPager.setAdapter(pagerAdapter); // setando o adapter do view_pager
        viewPagerTab.setViewPager(viewPager); // setando view_pager do pager_tab


        // CONFIGURANDO FLOATING ACTION BUTTON
        /*FloatingActionButton floatingActionButton = ((MainActivity) getActivity()).getFloatingActionButton();
        if (floatingActionButton != null) {
            floatingActionButton.show();
        }
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // Click do fab
                startActivity(new Intent(fragActivity.getApplicationContext(), CadastroColecaoActivity.class));
            }
        });*/

        return view;
    }

    /*@Override
    public void onPrepareOptionsMenu(Menu menu) {

        MenuItem item = menu.findItem(R.id.action_search);
        if(item!=null){
            item.setVisible(true);
        }

        super.onPrepareOptionsMenu(menu);
    }*/

    public void onAttach(Context context) {
        this.context = context;
        super.onAttach(context);
    }

}
