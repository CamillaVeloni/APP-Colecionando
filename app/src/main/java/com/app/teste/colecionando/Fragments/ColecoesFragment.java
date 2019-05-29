package com.app.teste.colecionando.Fragments;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.app.teste.colecionando.Activitys.ColecoesListaActivity;
import com.app.teste.colecionando.Adapter.MuseuVirtualAdapter;
import com.app.teste.colecionando.Ajuda.MuseuItem;
import com.app.teste.colecionando.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.crowdfire.cfalertdialog.CFAlertDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ColecoesFragment extends Fragment {


    public ColecoesFragment() {
        // Required empty public constructor
    }

    private View view;
    private BaseQuickAdapter adapter;
    private Activity fragActivity;
    private RecyclerView recyclerCategorias;
    private List<MuseuItem> categData;
    private int[] imagens = {R.mipmap.imagem_categoria01, R.mipmap.imagem_categoria02,
            R.mipmap.imagem_categoria03, R.mipmap.imagem_categoria04, R.mipmap.imagem_categoria05,
            R.mipmap.imagem_categoria06, R.mipmap.imagem_categoria07, R.mipmap.imagem_categoria08};
    private Context context;
    private String[] gridCategorias;
    private String[] todasCategorias;
    private String categoriaSelecionada = "";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_colecoes, container, false);

        //setHasOptionsMenu(true);
        fragActivity = this.getActivity();
        gridCategorias = getResources()
                .getStringArray(R.array.minhaColec_categorias);

        todasCategorias = getResources()
                .getStringArray(R.array.minhaColec_tdcategorias);
        inicializarRecyclerView();
        inicializandoData();
        inicializarAdapter();

        return view;
    }

    public void inicializarRecyclerView(){ // Setando layout do recycler view, colocando um layout_manager
        recyclerCategorias = view.findViewById(R.id.recyclerGaleriaColec);
        recyclerCategorias.setLayoutManager(new GridLayoutManager(context, 2));
        recyclerCategorias.setHasFixedSize(true);
    }

    public void inicializarAdapter(){ // Criando um adapter para o recycler view e setando ele no mesmo

        adapter = new MuseuVirtualAdapter(R.layout.layout_item_museu, categData);
        adapter.openLoadAnimation();

        View header = getLayoutInflater().inflate(R.layout.header_view_museu,
                (ViewGroup) recyclerCategorias.getParent(),
                false);
        View footer = getLayoutInflater().inflate(R.layout.footer_view_museu,
                (ViewGroup) recyclerCategorias.getParent(),
                false);

        TextView txtMaisFiltros = footer.findViewById(R.id.museuVirtual_maisFiltros);

        txtMaisFiltros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selecionandoMaisCategorias();
            }
        });

        adapter.addHeaderView(header);
        adapter.addFooterView(footer);

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                categoriaSelecionada = gridCategorias[position];
                selecionandoCategoria();
            }
        });

        /*View top = getLayoutInflater().inflate(R.layout.top_view, (ViewGroup) mRecyclerView.getParent(),
                false);

        adapter.addHeaderView(top);

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(HomeActivity.this, ACTIVITY[position]);
                startActivity(intent);
            }
        });*/

        recyclerCategorias.setAdapter(adapter);
    }

    private void inicializandoData() {
        categData = new ArrayList<>();

        for (int i = 0; i < gridCategorias.length; i++) {

            MuseuItem item = new MuseuItem();
            item.setTitulo(gridCategorias[i]);
            item.setImagem(imagens[i]);
            categData.add(item);
        }
    }

    private void selecionandoMaisCategorias(){

        CFAlertDialog.Builder builder = new CFAlertDialog.Builder(context);
        builder.setDialogStyle(CFAlertDialog.CFAlertStyle.ALERT);

        View viewSpinner = getLayoutInflater() // Converter em objeto View
                .inflate(R.layout.dialog_spinner, null); // inflar o layout xml
        final Spinner sp = viewSpinner.findViewById(R.id.spinnerFiltro);
        TextView txtFiltroSelec = viewSpinner.findViewById(R.id.txtFiltroSelecionado);

        ArrayAdapter<String> adapterCateg = new ArrayAdapter<>(context,
                android.R.layout.simple_spinner_item, todasCategorias);

        adapterCateg.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(adapterCateg);

        builder.setHeaderView(viewSpinner);

        builder.addButton("Confirmar", -1, -1,
                CFAlertDialog.CFAlertActionStyle.POSITIVE,
                CFAlertDialog.CFAlertActionAlignment.JUSTIFIED, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        categoriaSelecionada = sp.getSelectedItem().toString();
                        selecionandoCategoria();
                        dialogInterface.dismiss();
                    }
                });
        builder.addButton("Cancelar", -1, -1,
                CFAlertDialog.CFAlertActionStyle.NEGATIVE,
                CFAlertDialog.CFAlertActionAlignment.JUSTIFIED, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        builder.addButton("Todos colecionáveis", -1, -1,
                CFAlertDialog.CFAlertActionStyle.DEFAULT,
                CFAlertDialog.CFAlertActionAlignment.JUSTIFIED, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        categoriaSelecionada = null;
                        selecionandoCategoria();
                        dialogInterface.dismiss();
                    }
                }
        );
        builder.show();

    }

    private void selecionandoCategoria(){

        Intent intent = new Intent(fragActivity.getApplicationContext(),
                ColecoesListaActivity.class);
        intent.putExtra("categoriaSelecionada", categoriaSelecionada);
        startActivity(intent);
    }

    /*@Override
    public void onPrepareOptionsMenu(Menu menu) {

        MenuItem item = menu.findItem(R.id.action_search);
        if(item!=null){
            item.setVisible(false);
        }

        super.onPrepareOptionsMenu(menu);
    }*/

    // GARANTINDO QUE IRÁ PASSAR UM CONTEXTO VÁLIDO
    @Override
    public void onAttach(Context context) {
        this.context = context;
        super.onAttach(context);
    }

}
