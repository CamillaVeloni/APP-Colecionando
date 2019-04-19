package com.app.teste.colecionando.Fragments;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.app.teste.colecionando.Activitys.CadastroColecaoActivity;
import com.app.teste.colecionando.Activitys.MainActivity;
import com.app.teste.colecionando.Adapter.AdapterRecyclerView;
import com.app.teste.colecionando.Ajuda.UsuárioFirebase;
import com.app.teste.colecionando.ConfiguraçãoFirebase.ConfigFirebase;
import com.app.teste.colecionando.Modelos.Colecionável;
import com.app.teste.colecionando.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.callback.ItemDragAndSwipeCallback;
import com.chad.library.adapter.base.listener.OnItemSwipeListener;
import com.crowdfire.cfalertdialog.CFAlertDialog;
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

        // CONFIGURANDO FLOATING ACTION BUTTON
        FloatingActionButton floatingActionButton = ((MainActivity) getActivity()).getFloatingActionButton();
        if (floatingActionButton != null) {
            floatingActionButton.show();
        }
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // Click do fab
                startActivity(new Intent(fragActivity.getApplicationContext(), CadastroColecaoActivity.class));
            }
        });

        // CONFIGURANDO RECYCLER VIEW
        inicializarRecyclerView();
        inicializarAdapter();
        inicializarData();
        //

        return view;
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
                        .setTextColor(ContextCompat.getColor(context, R.color.colorPrimary))
                        .setIcon(R.drawable.ic_delete_24dp)
                        .addButton("Deletar", -1, -1, CFAlertDialog.CFAlertActionStyle.NEGATIVE,
                                CFAlertDialog.CFAlertActionAlignment.JUSTIFIED, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                colecRestaurado.removerColecionável();

                                Toast.makeText(fragActivity, "Deletado com sucesso!", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        }).addButton("Cancelar", -1, -1, CFAlertDialog.CFAlertActionStyle.DEFAULT,
                                CFAlertDialog.CFAlertActionAlignment.JUSTIFIED, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which){
                                coleção.add(posiçãoAnterior, colecRestaurado);
                                adapter.notifyDataSetChanged();

                                Toast.makeText(fragActivity, "Colecionável restaurado com sucesso!", Toast.LENGTH_SHORT).show();
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
                        485, 20, 20, paintRound);
                canvas.drawText("Deletar", 80, 280, paintText);
            }

        };

        adapter = new AdapterRecyclerView(coleção);
        int mFirstPageItemCount = 4;
        adapter.setNotDoAnimationCount(mFirstPageItemCount);
        adapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT); // Animação quando estiver 'descendo' pelo recyclerView

        ItemDragAndSwipeCallback ItemSwipeCallback = new ItemDragAndSwipeCallback(adapter);
        ItemTouchHelper ItemTouchHelper = new ItemTouchHelper(ItemSwipeCallback);
        ItemSwipeCallback.setSwipeMoveFlags(ItemTouchHelper.START); // Colocando para o 'swipe' ser da direita para esquerda
        ItemTouchHelper.attachToRecyclerView(recyclerMinhaColeção); // anexar o itemTouch ao recyclerView

        adapter.enableSwipeItem(); // Permitindo o 'swipe'
        adapter.setOnItemSwipeListener(onItemSwipeListener); // setando um listener para o item

        recyclerMinhaColeção.setAdapter(adapter);
    }

    public void inicializarData(){

        coleçãoUsuarioRef.addValueEventListener(new ValueEventListener() { // Recuperando dados da minha_coleção passando para lista
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                coleção.clear();
                for(DataSnapshot data : dataSnapshot.getChildren()){
                    coleção.add(data.getValue(Colecionável.class));
                }
                Collections.reverse(coleção);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void onAttach(Context context) {
        this.context = context;
        super.onAttach(context);
    }

}
