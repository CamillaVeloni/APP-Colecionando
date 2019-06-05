package com.app.teste.colecionando.Fragments;


import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.teste.colecionando.Ajuda.AjudaNetwork;
import com.app.teste.colecionando.Ajuda.UsuárioFirebase;
import com.app.teste.colecionando.ConfiguraçãoFirebase.ConfigFirebase;
import com.app.teste.colecionando.Modelos.Colecionável;
import com.app.teste.colecionando.R;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class MClassifPagerFragment extends Fragment {


    public MClassifPagerFragment() {
        // Required empty public constructor
    }

    private Context context;
    private TextView total_colecion, total_favoritos, total_coleção;
    private DatabaseReference coleçãoUsuarioRef;
    private FirebaseUser usuario;
    private ImageView imgPerfil;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mclassif_pager, container, false);

        final int totalCategoria = 17;

        total_coleção = view.findViewById(R.id.totalColeção_pager);
        total_colecion = view.findViewById(R.id.totalMeusColecion_pager);
        total_favoritos = view.findViewById(R.id.totalFavoritos_pager);

        imgPerfil = view.findViewById(R.id.imgColecionador_pager);
        usuario = UsuárioFirebase.getUsuarioAtual();
        Uri url = usuario.getPhotoUrl(); // pegando url da foto perfil no firebase
        if (url != null) { // Verificar se tem uma foto de perfil no firebase
            Glide.with(MClassifPagerFragment.this).load(url).into(imgPerfil); // Carregar img do firebase p/ imgview
        }else{
            imgPerfil.setImageResource(R.drawable.perfil_padrao); // img padrão do perfil
        }

        coleçãoUsuarioRef = ConfigFirebase.getDatabase() // Recuperando todos os colecionável do usuário através do UID
                .child("minha_coleção").child(UsuárioFirebase.getIdentificadorUsuario());

        if(AjudaNetwork.conectadoNet(context)){
            coleçãoUsuarioRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    int coleção = 0;
                    int fav = 0;
                    int col = 0;

                    for(DataSnapshot data : dataSnapshot.getChildren()){
                        Colecionável tempColec = data.getValue(Colecionável.class);
                        if(tempColec != null){
                            coleção += 1;
                            if(tempColec.isBoolAdquirido()){ // Colecionavel adquirido (meus_colecionaveis)
                                col += 1;
                            }else{ // Colecionaveis ainda não adquirido (meus_favoritos)
                                fav += 1;
                            }
                        }
                    }
                    total_coleção.setText(String.valueOf(coleção));
                    total_colecion.setText(String.valueOf(col));
                    total_favoritos.setText(String.valueOf(fav));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        return view;
    }

    public void onAttach(Context context) {
        this.context = context;
        super.onAttach(context);
    }

}
