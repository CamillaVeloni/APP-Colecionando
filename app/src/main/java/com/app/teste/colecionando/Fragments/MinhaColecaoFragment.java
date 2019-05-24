package com.app.teste.colecionando.Fragments;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.teste.colecionando.Activitys.CadastroColecaoActivity;
import com.app.teste.colecionando.Activitys.ColecionavelActivity;
import com.app.teste.colecionando.Activitys.MainActivity;
import com.app.teste.colecionando.Adapter.AdapterRecyclerView;
import com.app.teste.colecionando.Ajuda.UsuárioFirebase;
import com.app.teste.colecionando.ConfiguraçãoFirebase.ConfigFirebase;
import com.app.teste.colecionando.Modelos.Colecionável;
import com.app.teste.colecionando.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.callback.ItemDragAndSwipeCallback;
import com.chad.library.adapter.base.listener.OnItemSwipeListener;
import com.crowdfire.cfalertdialog.CFAlertDialog;
import com.forms.sti.progresslitieigb.ProgressLoadingJIGB;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.pd.chocobar.ChocoBar;

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
    private int posiçãoAnterior;
    private boolean restaurado;
    private Colecionável colecRestaurado;
    private AdapterRecyclerView adapter;
    private Activity fragActivity;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_minha_colecao, container, false);

        fragActivity = this.getActivity();
        posiçãoAnterior = 0;
        colecRestaurado = new Colecionável();
        coleçãoUsuarioRef = ConfigFirebase.getFirebaseDatabase() // Recuperando todos os colecionável do usuário através do UID
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

    public void onAttach(Context context) {
        this.context = context;
        super.onAttach(context);
    }

}
