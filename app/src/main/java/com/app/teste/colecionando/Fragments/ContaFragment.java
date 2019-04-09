package com.app.teste.colecionando.Fragments;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.teste.colecionando.Activitys.MainActivity;
import com.app.teste.colecionando.Ajuda.UsuárioFirebase;
import com.app.teste.colecionando.ConfiguraçãoFirebase.ConfigFirebase;
import com.app.teste.colecionando.R;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ContaFragment extends Fragment {


    public ContaFragment() {
        // Required empty public constructor
    }

    private Context context; //Declara um atributo para guardar o context.
    private static final int REQUEST_CODE_READ_EXTERNAL_STORAGE = 0; // Request_code para permissão de uso do armaz. externo
    private static final int REQUEST_CODE_IMAGE_GALLERY = 1; // Request_code para seleção de imagem na galeria
    private StorageReference storageReference;
    private CircleImageView imgPerfil;
    private EditText txtPerfilNome;
    private TextView txtPerfilEmail;
    private ImageView imgSalvarNomeUsuario;
    private Activity fragActivity;
    private NavigationView navigationView;
    private View headerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_conta, container, false);

        fragActivity = this.getActivity();
        storageReference = ConfigFirebase.getFirebaseStorage();

        imgPerfil = view.findViewById(R.id.img_perfil);
        txtPerfilEmail = view.findViewById(R.id.textView_email);
        txtPerfilNome = view.findViewById(R.id.editText_nomePerfil);
        imgSalvarNomeUsuario = view.findViewById(R.id.imgSalvarNome);

        navigationView = fragActivity.findViewById(R.id.nav_view);
        headerView = navigationView.getHeaderView(0);

        FloatingActionButton floatingActionButton = ((MainActivity) getActivity()).getFloatingActionButton();
        if (floatingActionButton != null) {
            floatingActionButton.hide();
        }

        // RECUPERANDO DADOS DO USUÁRIO DE DENTRO DO FIREBASE PARA SETAR NA IMAGE VIEW, NO EDIT TEXT E TEXT VIEW
        FirebaseUser usuario = UsuárioFirebase.getUsuarioAtual();

        // Atualizando e-mail e nome do perfil
        txtPerfilNome.setText(usuario.getDisplayName());
        txtPerfilEmail.setText(usuario.getEmail());

        // Atualizando foto perfil
        Uri url = usuario.getPhotoUrl(); // pegando url da foto perfil no firebase
        if (url != null) { // Verificar se tem uma foto de perfil no firebase
            Glide.with(ContaFragment.this).load(url).into(imgPerfil); // Carregar img do firebase p/ imgview
        }else{
            imgPerfil.setImageResource(R.drawable.imagem_perfil); // img padrão do perfil
        }

        // MODIFICANDO O DISPLAY NAME DO USUÁRIO //
        imgSalvarNomeUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!txtPerfilNome.getText().toString().isEmpty()){
                    try {
                        UsuárioFirebase.atualizarNomeUsuario(txtPerfilNome.getText().toString());

                        TextView nome_nav = headerView.findViewById(R.id.txtNome_nav_header);
                        nome_nav.setText(txtPerfilNome.getText().toString());

                        Toast.makeText(fragActivity,"Nome do perfil atualizado com sucesso", Toast.LENGTH_SHORT).show();
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }else{
                    Toast.makeText(getContext(), "Coloque um nome para o perfil", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // IMPORTANDO FOTO DO ARMAZENAMENTO E DE PERFIL //
        imgPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 23) { // Verificar se o usuário está utilizando alguma versão superior à do Marshmallow (api-23)
                    // O método checkSelfPermission recupera a permissão e verificando através do packageManager se a permissão foi concedida
                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_GRANTED) {
                        // Usando intent p/ passar pro android o que é necessário fazer
                        Intent intent = new Intent(Intent.ACTION_PICK,
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI); // O ACTION_PICK permite escolher uma foto e o lugar padrão armaz. no cel
                        if (intent.resolveActivity(context.getPackageManager()) != null){ // Verificar se realmente existe a galeria de fotos
                            startActivityForResult(intent, REQUEST_CODE_IMAGE_GALLERY);
                        }

                    } else {
                        // Solicitando permissão se não foi concedida ainda
                        requestPermissions(
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                REQUEST_CODE_READ_EXTERNAL_STORAGE
                        );
                    }
                }
            }
        });

        return view;
    }


    // RETORNANDO A DATA (IMAGEM NA GALERIA) //
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE_IMAGE_GALLERY && resultCode == Activity.RESULT_OK){

            try {
                // Recuperando a img selecionada
                Uri imgSelecionada = data.getData(); // caminho da img
                Bitmap img = MediaStore.Images.Media.getBitmap(
                        context.getContentResolver(), imgSelecionada); // getContentResolver é um objeto responsavel por recuperar

                if(img != null) {
                    imgPerfil.setImageBitmap(img);
                    CircleImageView foto_nav = headerView.findViewById(R.id.image_nav_header);
                    foto_nav.setImageBitmap(img);

                    UsuárioFirebase.atualizarFotoUsuario(imgSelecionada);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }


    // MÉTODO PARA ATUALIZAR FOTO DE PERFIL DO USUÁRIO DENTRO DO FIREBASE //
    /*public void atualizarPerfilFotoUsuario(Uri url){
        UsuárioFirebase.atualizarFotoUsuario(url);
    }*/


    // ARRUMANDO O MENU PARA O FRAGMENT 'MINHA CONTA' //
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        MenuItem item = menu.findItem(R.id.action_search); // deixando o item 'pesquisar' fique invisivel nesse fragment
        item.setVisible(false);
        super.onCreateOptionsMenu(menu, inflater);
    }


    // TRATAMENTO QUANDO O USUÁRIO NEGAR A PERMISSÃO //
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE_READ_EXTERNAL_STORAGE) { // verificar se os request codes são os mesmos
            if (grantResults[0] == PackageManager.PERMISSION_DENIED){    // foi concedida permissão. O grantResults fica o resultado de cada permissão
                alertaPermissao();
            }
        }
    }


    // ALERTA QUANDO NÃO FOR ACEITADO A PERMISSÃO
    public void alertaPermissao(){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Permissão Negada");
        builder.setCancelable(false);
        builder.setMessage("Para utilizar o recurso é necessário aceitar a permissão.");
        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // nd
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();

    }


    // GARANTINDO QUE IRÁ PASSAR UM CONTEXTO VÁLIDO
    @Override
    public void onAttach(Context context) {
        this.context = context;
        super.onAttach(context);
    }
}
