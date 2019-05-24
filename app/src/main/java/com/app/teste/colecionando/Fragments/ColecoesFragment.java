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
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ColecoesFragment extends Fragment {


    public ColecoesFragment() {
        // Required empty public constructor
    }

    private View view;
    private List<Colecionável> galeria = new ArrayList<>();
    private DatabaseReference galeriaColecRef;
    private AdapterRecyclerView adapter;
    private Activity fragActivity;
    private RecyclerView recyclerColecoes;
    private String filtrarCategoria = "";
    private Button btnCategoria;
    private ProgressLoadingJIGB progressLoading;
    private Context context;
    private String[] categorias;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_colecoes, container, false);
        fragActivity = this.getActivity();
        btnCategoria = view.findViewById(R.id.btnCategoria);
        categorias = getResources()
                .getStringArray(R.array.minhaColec_categorias);

        inicializarRecyclerView();
        inicializarAdapter();


        inicializarData();

        btnCategoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seleçãoCategoria();
            }
        });

        /*FloatingActionButton floatingActionButton = ((MainActivity) getActivity()).getFloatingActionButton();
        if (floatingActionButton != null) {
            floatingActionButton.hide();
        }*/

        return view;
    }

    public void seleçãoCategoria(){

        CFAlertDialog.Builder builder = new CFAlertDialog.Builder(context);
        builder.setDialogStyle(CFAlertDialog.CFAlertStyle.ALERT);

        View viewSpinner = getLayoutInflater() // Converter em objeto View
                .inflate(R.layout.dialog_spinner, null); // inflar o layout xml
        final Spinner sp = viewSpinner.findViewById(R.id.spinnerCategoria);

        ArrayAdapter<String> adapterCateg = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_item, categorias);
        adapterCateg.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(adapterCateg);

        builder.setHeaderView(viewSpinner);

        builder.addButton("Confirmar", -1, -1, CFAlertDialog.CFAlertActionStyle.POSITIVE,
                CFAlertDialog.CFAlertActionAlignment.JUSTIFIED, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        filtrarCategoria = sp.getSelectedItem().toString();
                        recuperarColecioCateg();
                        dialogInterface.dismiss();
                    }
                });
        builder.addButton("Cancelar", -1, -1, CFAlertDialog.CFAlertActionStyle.NEGATIVE,
                CFAlertDialog.CFAlertActionAlignment.JUSTIFIED, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        builder.addButton("Apagar filtro", -1, -1, CFAlertDialog.CFAlertActionStyle.DEFAULT,
                CFAlertDialog.CFAlertActionAlignment.JUSTIFIED, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        inicializarData();
                        dialogInterface.dismiss();
                    }
                });
        builder.show();
    }

    public void recuperarColecioCateg(){

        progressLoading.startLoadingJIGB(context,R.raw.trail_loading, // Travando tela para 'Carregar' - progress loading
                "",0,600,600);

        galeriaColecRef = ConfigFirebase.getFirebaseDatabase() // Recuperando os colecionável públicos
                .child("galeria_coleções")
                .child(filtrarCategoria);

        galeriaColecRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                galeria.clear();
                for(DataSnapshot colecionavel : dataSnapshot.getChildren()){ // Percorre todos os colecionaveis dentro de determinada categoria
                    galeria.add(colecionavel.getValue(Colecionável.class));
                }
                Collections.reverse(galeria);
                adapter.notifyDataSetChanged();
                progressLoading.finishLoadingJIGB(context);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void inicializarRecyclerView(){ // Setando layout do recycler view, colocando um layout_manager
        recyclerColecoes = view.findViewById(R.id.recyclerGaleriaColec);
        recyclerColecoes.setLayoutManager(new LinearLayoutManager(context));
        recyclerColecoes.setHasFixedSize(true);
    }

    public void inicializarAdapter(){ // Criando um adapter para o recycler view e setando ele no mesmo

        adapter = new AdapterRecyclerView(galeria);
        int mFirstPageItemCount = 3;
        adapter.setNotDoAnimationCount(mFirstPageItemCount);
        adapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT); // Animação quando estiver 'descendo' pelo recyclerView

        recyclerColecoes.setAdapter(adapter);

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Log.d("Click", "onItemClick");
                Colecionável colecSelecionado = galeria.get(position);
                Intent intent = new Intent(fragActivity.getApplicationContext(), ColecionavelActivity.class);
                intent.putExtra("colecSelecionado", colecSelecionado);
                intent.putExtra("boolGaleria", true);
                startActivity(intent);

            }
        });
    }

    public void inicializarData(){

        galeriaColecRef = ConfigFirebase.getFirebaseDatabase() // Recuperando os colecionável públicos
                .child("galeria_coleções");

        progressLoading.startLoadingJIGB(context,R.raw.trail_loading, // Travando tela para 'Carregar' - progress loading
                "",0,600,600);

        galeriaColecRef.addValueEventListener(new ValueEventListener() { // Recuperando dados da minha_coleção passando para lista
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                galeria.clear();
                for(DataSnapshot categorias : dataSnapshot.getChildren()){ // Percorre todas as categorias
                    for(DataSnapshot colecionavel : categorias.getChildren()){ // Percorre todos os colecionaveis dentro de determinada categoria
                        galeria.add(colecionavel.getValue(Colecionável.class));
                    }
                }
                Collections.reverse(galeria);
                adapter.notifyDataSetChanged();
                progressLoading.finishLoadingJIGB(context);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    // GARANTINDO QUE IRÁ PASSAR UM CONTEXTO VÁLIDO
    @Override
    public void onAttach(Context context) {
        this.context = context;
        super.onAttach(context);
    }

}
