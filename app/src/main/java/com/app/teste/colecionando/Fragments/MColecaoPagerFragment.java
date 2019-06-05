package com.app.teste.colecionando.Fragments;


import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.helper.ItemTouchHelper;
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

import com.app.teste.colecionando.Activitys.CadastroColecaoActivity;
import com.app.teste.colecionando.Activitys.ColecionavelActivity;
import com.app.teste.colecionando.Adapter.AdapterColecionaveis;
import com.app.teste.colecionando.Ajuda.AjudaNetwork;
import com.app.teste.colecionando.Ajuda.ColecionaveisData;
import com.app.teste.colecionando.Ajuda.UsuárioFirebase;
import com.app.teste.colecionando.ConfiguraçãoFirebase.ConfigFirebase;
import com.app.teste.colecionando.Modelos.Colecionável;
import com.app.teste.colecionando.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.callback.ItemDragAndSwipeCallback;
import com.chad.library.adapter.base.listener.OnItemSwipeListener;
import com.crowdfire.cfalertdialog.CFAlertDialog;
import com.forms.sti.progresslitieigb.ProgressLoadingJIGB;
import com.github.clans.fab.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.pd.chocobar.ChocoBar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MColecaoPagerFragment extends Fragment {


    public MColecaoPagerFragment() {
        // Required empty public constructor
    }

    private Context context;
    private RecyclerView recyclerMinhaColeção;
    private List<Colecionável> coleção = new ArrayList<>();
    private DatabaseReference coleçãoUsuarioRef;
    private int posiçãoAnterior;
    ColecionaveisData mData = new ColecionaveisData();
    private View view_ofList;
    private static int TYPE_ERROR = 0, TYPE_EMPTY = 1;
    private boolean restaurado;
    private Colecionável colecRestaurado;
    private AdapterColecionaveis adapter;
    private Activity fragActivity;
    private View view;
    private android.support.v7.widget.SearchView searchView = null;
    private android.support.v7.widget.SearchView.OnQueryTextListener queryTextListener;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_mcolecao_pager, container, false);

        fragActivity = this.getActivity();

        posiçãoAnterior = 0;
        colecRestaurado = new Colecionável();
        coleçãoUsuarioRef = ConfigFirebase.getFirebaseDatabase() // Recuperando todos os colecionável do usuário através do UID
                .child("minha_coleção").child(UsuárioFirebase.getIdentificadorUsuario());

        // CONFIGURANDO RECYCLER VIEW, ADAPTER E DATA DA MINHA_COLEÇÃO
        inicializarRecyclerView();
        inicializarAdapter();
        inicializarData();

        // CONFIGURANDO FAB ADICIONAR
        FloatingActionButton fab = view.findViewById(R.id.fabAdd);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // Click do fab
                startActivity(new Intent(fragActivity.getApplicationContext(), CadastroColecaoActivity.class));
            }
        });

        FloatingActionButton fabFiltro = view.findViewById(R.id.fabFiltro);

        fabFiltro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selecionandoFabFiltro();
            }
        });

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    private void selecionandoFabFiltro(){

        CFAlertDialog.Builder builder = new CFAlertDialog.Builder(context);
        builder.setDialogStyle(CFAlertDialog.CFAlertStyle.ALERT);
        builder.setTitle("Selecione o tipo de filtro ou apague o filtro já configurado:");
        builder.setItems(new String[]{"Categoria", "Etiqueta personalizada",
                        "Obtido ou no favoritos", "Apagar filtro"},
                new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int index) {

                if(index == 0){ // categ
                    selecionandoFiltro(index);
                    dialogInterface.dismiss();
                }else if(index == 1){ // etiq
                    selecionandoFiltro(index);
                    dialogInterface.dismiss();
                }else if(index == 2){
                    selecionandoFiltro(index);
                    dialogInterface.dismiss();
                }else{ // apagar filtro
                    inicializarData();
                    dialogInterface.dismiss();
                }

            }
        });
        builder.show();

    }

    private void selecionandoFiltro(final int index){

        CFAlertDialog.Builder builder = new CFAlertDialog.Builder(context);
        builder.setDialogStyle(CFAlertDialog.CFAlertStyle.ALERT);

        View viewSpinner = getLayoutInflater() // Converter em objeto View
                .inflate(R.layout.dialog_spinner, null); // inflar o layout xml
        final Spinner sp = viewSpinner.findViewById(R.id.spinnerFiltro);
        List<String> listaFiltros;

        // 0 - categoria, 1 - etiqueta
        if(index == 0){
            listaFiltros = mData.getKeysUnicasCategoria();
        }else if (index == 1){
            listaFiltros = mData.getKeysUnicasEtiqueta();
        }else{
            listaFiltros = new ArrayList<>();
            listaFiltros.add("Obtidos");
            listaFiltros.add("Favorito");
        }

        ArrayAdapter<String> adapterCateg = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_item, listaFiltros);

        adapterCateg.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(adapterCateg);

        builder.setHeaderView(viewSpinner);

        builder.addButton("Confirmar", -1, -1, CFAlertDialog.CFAlertActionStyle.POSITIVE,
                CFAlertDialog.CFAlertActionAlignment.JUSTIFIED, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        if(AjudaNetwork.conectadoNet(context)){
                            String stringFiltro = sp.getSelectedItem().toString();
                            List<Colecionável> listaFiltrada = mData.getTdsColecionaveis();

                            if(index == 0){
                                listaFiltrada = mData.getColecFiltradosCategoria(stringFiltro,
                                        listaFiltrada);
                            }else if(index == 1){
                                listaFiltrada = mData.getColecFiltradosEtiqueta(stringFiltro,
                                        listaFiltrada);
                            }else{
                                boolean boolObtFav = false;
                                if(stringFiltro.equals("Obtidos")){
                                    boolObtFav = true;
                                }
                                listaFiltrada = mData.getColecFiltroAdquiridos(boolObtFav, listaFiltrada);
                            }

                            coleção.clear();
                            coleção.addAll(listaFiltrada);
                            adapter.notifyDataSetChanged();

                            if(coleção.size() == 0){
                                errorEmptyList(TYPE_EMPTY);
                            }
                        }else{
                            errorEmptyList(TYPE_ERROR);
                        }

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
        builder.show();

    }

    public void inicializarRecyclerView(){ // Setando layout do recycler view, colocando um layout_manager
        recyclerMinhaColeção = view.findViewById(R.id.recyclerMinhaColeção);
        recyclerMinhaColeção.setLayoutManager(new LinearLayoutManager(context));
        recyclerMinhaColeção.setHasFixedSize(true);
    }

    public void inicializarAdapter(){ // Criando um adapter para o recycler view e setando ele no mesmo


        OnItemSwipeListener onItemSwipeListener = new OnItemSwipeListener() {
            @Override
            public void onItemSwipeStart(RecyclerView.ViewHolder viewHolder, int pos) { // Armazenando Colecionavel e posição dele se
                posiçãoAnterior = pos;                                                  // for preciso restaurar
                colecRestaurado = coleção.get(posiçãoAnterior);
                restaurado = false;
            }

            @Override
            public void clearView(RecyclerView.ViewHolder viewHolder, int pos) { }
            @Override
            public void onItemSwiped(RecyclerView.ViewHolder viewHolder, int pos) { // Quando o item é deletado da lista

                // Criando o alert dialog com o builder
                CFAlertDialog.Builder builder = new CFAlertDialog.Builder(context)
                        .setDialogStyle(CFAlertDialog.CFAlertStyle.BOTTOM_SHEET)
                        .setTitle("Deletar Colecionável")
                        .setCornerRadius(20)
                        .setMessage("Deseja deletar o colecionável? Não será possível restaurar o colecionável depois!")
                        .setTextColor(ContextCompat.getColor(context, R.color.colorBlue))
                        .setIcon(R.drawable.ic_icons8_excluir)
                        .addButton("Deletar", -1, -1, CFAlertDialog.CFAlertActionStyle.NEGATIVE,
                                CFAlertDialog.CFAlertActionAlignment.JUSTIFIED, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        colecRestaurado.removerColecionável();

                                        chocoBarPadrao("Deletado com sucesso");
                                        dialog.dismiss();
                                    }
                                }).addButton("Cancelar", -1, -1, CFAlertDialog.CFAlertActionStyle.DEFAULT,
                                CFAlertDialog.CFAlertActionAlignment.JUSTIFIED, new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which){
                                        coleção.add(posiçãoAnterior, colecRestaurado);
                                        adapter.notifyDataSetChanged();

                                        chocoBarPadrao("Colecionável restaurado com sucesso!");
                                        dialog.dismiss();
                                    }
                                }).setCancelable(false);

                // Mostrar o alert
                builder.show();
            }

            @Override
            public void onItemSwipeMoving(Canvas canvas, RecyclerView.ViewHolder viewHolder,
                                          float dX, float dY, boolean isCurrentlyActive) {

                Paint paintRound = new Paint();
                Paint paintText = new Paint();

                paintRound.setColor(ContextCompat.getColor(context, R.color.red_colorPrimary));
                paintText.setColor(Color.WHITE);
                paintText.setTextSize(80);

                canvas.drawRoundRect(0, +10, 400,
                        488, 20, 20, paintRound);
                canvas.drawText("Deletar", 80, 280, paintText);
            }

        };

        adapter = new AdapterColecionaveis(coleção);
        int mFirstPageItemCount = 3;
        adapter.setNotDoAnimationCount(mFirstPageItemCount);
        adapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT); // Animação quando estiver 'descendo' pelo recyclerView

        ItemDragAndSwipeCallback ItemSwipeCallback = new ItemDragAndSwipeCallback(adapter);
        ItemTouchHelper ItemTouchHelper = new ItemTouchHelper(ItemSwipeCallback);
        ItemSwipeCallback.setSwipeMoveFlags(ItemTouchHelper.START); // Colocando para o 'swipe' ser da direita para esquerda
        ItemTouchHelper.attachToRecyclerView(recyclerMinhaColeção); // anexar o itemTouch ao recyclerView

        adapter.enableSwipeItem(); // Permitindo o 'swipe'
        adapter.setOnItemSwipeListener(onItemSwipeListener); // setando um listener para o item

        recyclerMinhaColeção.setAdapter(adapter);

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Log.d("Click", "onItemClick");
                Colecionável colecSelecionado = coleção.get(position);
                Intent intent = new Intent(fragActivity.getApplicationContext(), ColecionavelActivity.class);
                intent.putExtra("colecSelecionado", colecSelecionado);
                startActivity(intent);


            }
        });
    }

    public void inicializarData(){

        ProgressLoadingJIGB.startLoadingJIGB(context,R.raw.animation_w500_h500, // Travando tela para 'Carregar' - progress loading
                "",0,600,600);

        if(AjudaNetwork.conectadoNet(context)){
            coleçãoUsuarioRef.addValueEventListener(new ValueEventListener() { // Recuperando dados da minha_coleção passando para lista
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    mData.getTdsColecionaveis().clear();
                    coleção.clear();
                    List<Colecionável> temp = new ArrayList<>();
                    for(DataSnapshot data : dataSnapshot.getChildren()){
                        temp.add(data.getValue(Colecionável.class));
                    }
                    Collections.reverse(temp);
                    mData.setmList(temp);
                    coleção.addAll(mData.getTdsColecionaveis());
                    adapter.notifyDataSetChanged();
                    ProgressLoadingJIGB.finishLoadingJIGB(context); // Retirando o progress loading

                    if(coleção.size() == 0){
                        errorEmptyList(TYPE_EMPTY);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    ProgressLoadingJIGB.finishLoadingJIGB(context); // Retirando o progress loading
                    errorEmptyList(TYPE_ERROR);
                }
            });
        }else{
            ProgressLoadingJIGB.finishLoadingJIGB(context); // Retirando o progress loading
            errorEmptyList(TYPE_ERROR);

        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);

        if (searchItem != null) {
            searchItem.setVisible(true);
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager
                    .getSearchableInfo(getActivity().getComponentName()));

            queryTextListener = new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextChange(String novoTexto) {
                    Log.i("onQuery", "TextChange: " + novoTexto);

                    if(novoTexto != null && !novoTexto.isEmpty()){
                        pesquisarColecionavelNome(novoTexto);
                    } else if (novoTexto.isEmpty()){
                        coleção.clear();
                        coleção.addAll(mData.getTdsColecionaveis());
                        adapter.notifyDataSetChanged();
                    }
                    return true;
                }
                @Override
                public boolean onQueryTextSubmit(String query) {
                    Log.i("onQuery", "Submit: " + query);
                    return true;
                }
            };
            searchView.setOnQueryTextListener(queryTextListener);

        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void pesquisarColecionavelNome(String pesquisa){
        Log.d("OnQuery", "Pesquisa passou por textChange: " + pesquisa);
        List<Colecionável> listaFiltrada = mData.getTdsColecionaveis();
        listaFiltrada = mData.getColecFiltradosNome(pesquisa, listaFiltrada);

        coleção.clear();
        coleção.addAll(listaFiltrada);
        adapter.notifyDataSetChanged();

    }

    private void chocoBarPadrao(String text){
        ChocoBar.builder().setActivity(fragActivity)
                .setText(text)
                .setDuration(ChocoBar.LENGTH_SHORT)
                .build()
                .show();
    }

    private void errorEmptyList(int type){ // Método para mostrar uma view no recycler view
        if(type == 0){ // 0 - error_network
            view_ofList =
                    getLayoutInflater().inflate(R.layout.error_view,
                            (ViewGroup) recyclerMinhaColeção.getParent(),
                            false);
        }else{ // 1 - empty_list
            view_ofList =
                    getLayoutInflater().inflate(R.layout.empty_view,
                            (ViewGroup) recyclerMinhaColeção.getParent(),
                            false);
        }

        adapter.setEmptyView(view_ofList);
    }

    public void onAttach(Context context) {
        this.context = context;
        super.onAttach(context);
    }

}
