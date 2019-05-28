package com.app.teste.colecionando.Activitys;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.app.teste.colecionando.ConfiguraçãoFirebase.ConfigFirebase;
import com.app.teste.colecionando.Modelos.Colecionável;
import com.app.teste.colecionando.R;
import com.blackcat.currencyedittext.CurrencyEditText;
import com.bumptech.glide.Glide;
import com.forms.sti.progresslitieigb.ProgressLoadingJIGB;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.pd.chocobar.ChocoBar;

import java.io.ByteArrayOutputStream;
import java.text.CollationElementIterator;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CadastroColecaoActivity extends AppCompatActivity
            implements View.OnClickListener{

    private StorageReference storage;
    private Colecionável colecionável;
    public static final int RESULT_CODE = 1;
    private Colecionável colecEdit;
    private Button btnAdd;
    private ImageButton btnDel01, btnDel02, btnDel03;
    private EditText txtNome, txtDesc, txtComp, txtEtiq;
    private ImageView img1, img2, img3;
    private CurrencyEditText txtValor;
    private Spinner spinnerCategorias;
    private CheckBox checkAdq;
    private Switch switchPublico;
    private String imgUrl1, imgUrl2, imgUrl3;
    private List<String> listaCaminhosFotos = new ArrayList<>(); // lista de url dentro do aparelho do usuário
    private List<String> listaFotosFirebase = new ArrayList<>(); // lista de url recuperadas do firebase
    private final static int REQUEST_CODE_READ_EXTERNAL_STORAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_colecao);


        Toolbar toolbar = findViewById(R.id.toolbar_mColec);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // colocando botão voltar para activity principal
        storage = ConfigFirebase.getStorage();

        // VERIFICAÇÃO DE PERMISSÃO - ARMAZENAMENTO EXTERNO //
        if (Build.VERSION.SDK_INT >= 23) { // Verificar se o usuário está utilizando alguma versão superior à do Marshmallow (api-23)
            // O método checkSelfPermission recupera a permissão e verificando através do packageManager se a permissão foi concedida
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                // Solicitando permissão se não foi concedida ainda
                requestPermissions(
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_CODE_READ_EXTERNAL_STORAGE
                );
            }
        }

        // INICIALIZANDO COMPONENTES
        img1 = findViewById(R.id.cadastroColec_img1);
        img2 = findViewById(R.id.cadastroColec_img2);
        img3 = findViewById(R.id.cadastroColec_img3);

        txtNome = findViewById(R.id.cadastroColec_nome);
        txtDesc = findViewById(R.id.cadastroColec_desc);
        txtComp = findViewById(R.id.cadastroColec_ondeComp);
        txtEtiq = findViewById(R.id.cadastroColec_etiq);
        txtValor = findViewById(R.id.cadastroColec_valor);
        spinnerCategorias = findViewById(R.id.cadastroColec_spCateg);
        checkAdq = findViewById(R.id.cadastroColec_checkAd);
        switchPublico = findViewById(R.id.switchPublico);
        btnAdd = findViewById(R.id.cadastroColec_btnAdd);
        btnDel01 = findViewById(R.id.cadastroColec_btnDel01);
        btnDel02 = findViewById(R.id.cadastroColec_btnDel02);
        btnDel03 = findViewById(R.id.cadastroColec_btnDel03);

        imgUrl1 = "";
        imgUrl2 = "";
        imgUrl3 = "";

        // CONFIGURAÇÃO DO SPINNER //
        String[] categorias = getResources()
                .getStringArray(R.array.minhaColec_tdcategorias); // Recuperando array de categorias colocadas no strings.xml

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, categorias);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerCategorias.setAdapter(adapter); // Adicionando um adaptador para o spinner

        checkAdq.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    txtComp.setEnabled(true);
                    switchPublico.setVisibility(View.VISIBLE);
                }else{
                    txtComp.setEnabled(false);
                    txtComp.setText(null);
                    switchPublico.setVisibility(View.GONE);
                    switchPublico.setChecked(false);
                }
            }
        });

        // SETANDO INSTÂNCIA E AJEITANDO ACTIVITY PARA 'EDITAR'

        if(getIntent().getBooleanExtra("boolEditar", false)){
            setTitle(getResources().getString(R.string.title_editar_colec));
            btnAdd.setText(getResources().getString(R.string.minhaColec_editar));

            setandoInformaçõesEdit();
        }

        // SETANDO CLICK LISTENERS DENTRO DA CLASSE
        img1.setOnClickListener(this); // imageview 1
        img2.setOnClickListener(this); // imageview 2
        img3.setOnClickListener(this); // imageview 3
        btnAdd.setOnClickListener(this); // btn add colecionável
        btnDel01.setOnClickListener(this); // imgbtn delete 1
        btnDel02.setOnClickListener(this); // imgbtn delete 2
        btnDel03.setOnClickListener(this); // imgbtn delete 3

        // CONFIGURANDO 'VALOR' PARA REAL
        Locale local = new Locale("pt", "BR");
        txtValor.setLocale(local);

    }

    // CLICK LISTENERS //
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cadastroColec_img1: // Se clicou na primeira imagem
                escolherFoto(1);
                break;
            case R.id.cadastroColec_img2:
                escolherFoto(2);
                break;
            case R.id.cadastroColec_img3:
                escolherFoto(3);
                break;
            case R.id.cadastroColec_btnDel01:
                img1.setImageResource(R.drawable.imgpadrao_colec);
                imgUrl1 = "";
                break;
            case R.id.cadastroColec_btnDel02:
                img2.setImageResource(R.drawable.imgpadrao_colec);
                imgUrl2 = "";
                break;
            case R.id.cadastroColec_btnDel03:
                img3.setImageResource(R.drawable.imgpadrao_colec);
                imgUrl3 = "";
                break;
            case R.id.cadastroColec_btnAdd: // Se clicou no btn adicionar
                verificarCampos();
                break;
        }
    }
    public void escolherFoto(int rqCode){ // click das imagens
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if(intent.resolveActivity(getContext().getPackageManager()) != null){
            startActivityForResult(intent, rqCode);
        }
    }
    public void verificarCampos(){ // VALIDAÇÃO DOS CAMPOS E DA IMAGENS

        if(!imgUrl1.equals("") || !imgUrl2.equals("") || !imgUrl3.equals("")){ // Verificando se foi selecionada pelo menos 1 foto

            if(!txtNome.getText().toString().isEmpty() && !txtDesc.getText().toString().isEmpty() &&
                    txtValor.getRawValue() != 0){ // Verificando se os campos NOME, DESCRIÇÃO, VALOR foram preenchidos direito
                Boolean booleanaChecked = false; // booleana de verificação do campo 'local aonde o usuário comprou o colecionável
                // false == se foi selecionado o checkBox mas não tem nada dentro do campo 'local'
                // true == se foi selecionado o checkBox e possui algo dentro OU se não foi selecionado o checkBox

                if(checkAdq.isChecked() && txtComp.getText().toString().isEmpty()){ // Checando se o checkBox foi selecionado & o campo tá nulo
                    chocoBarPadrao("Porfavor, preencha todos os campos obrigatórios.");
                }else if(checkAdq.isChecked() && !txtComp.getText().toString().isEmpty() ||
                        !checkAdq.isChecked()){ // Checando se foi selecionado & tem algo no campo OU se não foi selecionado
                    booleanaChecked = true;
                }

                if(booleanaChecked){ // Verificação dos campos acaba aqui -> passando para método de salvar os dados no firebase

                    Colecionável colecionávelAdd = new Colecionável(txtNome.getText().toString(), txtDesc.getText().toString(),
                            txtValor.getText().toString(), spinnerCategorias.getSelectedItem().toString(),
                            checkAdq.isChecked()); // passando nome, descrição, valor, categoria, checkAdquirido

                    if(getIntent().getBooleanExtra("boolEditar", false)){
                        colecionávelAdd.setIdColecionavel(colecEdit.getIdColecionavel());
                    }

                    if(!txtEtiq.getText().toString().isEmpty()){ // adicionando etiqueta
                        colecionávelAdd.setEtiquetaCustomizada(txtEtiq.getText().toString());

                    }if(checkAdq.isChecked()){ // adicionando local compra
                        colecionávelAdd.setLocalCompra(txtComp.getText().toString());
                        if(switchPublico.isChecked()){ // verificando se colecionável é publico ou particular
                            colecionávelAdd.setBoolPublico(true);
                        }
                    }
                    if(!imgUrl1.equals("")){
                        listaCaminhosFotos.add(imgUrl1);
                    }if(!imgUrl2.equals("")){
                        listaCaminhosFotos.add(imgUrl2);
                    }if(!imgUrl3.equals("")){
                        listaCaminhosFotos.add(imgUrl3);
                    }
                    colecionável = colecionávelAdd;
                    salvarDadosFirebase();
                }

            }else{
                chocoBarPadrao("Porfavor, preencha todos os campos obrigatórios.");
            }

        }else{
            chocoBarPadrao("Selecione pelo menos uma foto.");
        }

    }
    public void salvarDadosFirebase(){ // MÉTODO PRINCIPAL NO SALVAMENTO DOS DADOS NO FIREBASE

        ProgressLoadingJIGB.startLoadingJIGB(getContext(),R.raw.trail_loading, // Travando tela para 'Carregar' - progress loading
                "",0,600,600);

        for(int i = 0; i < listaCaminhosFotos.size(); i++){
            String urlImg = listaCaminhosFotos.get(i);
            int qntdFotos = listaCaminhosFotos.size();
            salvarFotosFirebase(urlImg, qntdFotos, i); // PRIMEIRO É SALVO AS FOTOS NO STORAGE
        }
    }

    private void salvarFotosFirebase(String imgStringUrl, final int qntdFotos, int fotoAtual){

        StorageReference imagemColecionavel = storage.child("imagens")
                .child("colecionaveis")
                .child(colecionável.getIdColecionavel()) // id do colecionável
                .child("imagem" + fotoAtual); // imagem + index da lista

        // Upload da img transformando a string_url em uri
        UploadTask task = imagemColecionavel.putFile(Uri.parse(imgStringUrl));
        task.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // Maneira para pegar a url do arquivo guardado no Storage do firebase
                taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() { // O método getDownloadUrl vai retornar um obj Uri
                    @Override
                    public void onSuccess(Uri url) { // O objt Uri vai ser passado por parametro no método onSuccess
                        String urlConvertida = url.toString(); // url da imagem dentro do firebase -> Vai ser usada para colocar no database
                        listaFotosFirebase.add(urlConvertida);
                        if(qntdFotos == listaFotosFirebase.size()){
                            colecionável.setImagens(listaFotosFirebase); // setando o url das imagens de dentro do firebase
                            colecionável.salvarColecionável(); // SALVANDO COLECIONÁVEL ATRAVÉS DO MODELO DE CLASSE 'COLECIONÁVEL'
                            ProgressLoadingJIGB.finishLoadingJIGB(getContext()); // Retirando o progress loading

                            if(getIntent().getBooleanExtra("boolEditar", false)){
                                Intent intent = new Intent();
                                intent.putExtra("colecAtualizado", colecionável);
                                setResult(RESULT_CODE, intent);
                            }
                            finish();
                        }
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                chocoBarPadrao("Erro ao dar upload na imagem.");
                ProgressLoadingJIGB.finishLoadingJIGB(getContext()); // Retirando o progress loading
                Log.d("ERRO IMG", "Falha ao fazer upload: " + e.getMessage());
            }
        });
    }

    // SETANDO INFORMAÇÕES PARA EDIT
    private void setandoInformaçõesEdit(){
        colecEdit = (Colecionável) getIntent().getSerializableExtra("colecEditar");

        txtNome.setText(colecEdit.getNome());
        txtDesc.setText(colecEdit.getDescrição());

        // Transformando string em long
        String valor = colecEdit.getValor().substring(2);
        valor = valor.replace(".", ""); // Retirando o(s) ponto(s) do valor
        valor = valor.replace(",", ""); // Retirando a virgula do valor
        txtValor.setValue(Long.parseLong(valor));

        // Passando pelos valores do spinner para procurar o que foi selecionado
        for (int i = 0; i < spinnerCategorias.getCount(); i++) {
            if (spinnerCategorias.getItemAtPosition(i).toString().equals(colecEdit.getCategoria())) {
                spinnerCategorias.setSelection(i);
            }
        }
        if(colecEdit.getEtiquetaCustomizada() != null){
            txtEtiq.setText(colecEdit.getEtiquetaCustomizada());
        }
        if(colecEdit.isBoolAdquirido()){
            checkAdq.setChecked(true);
            txtComp.setText(colecEdit.getLocalCompra());
            if(colecEdit.isBoolPublico()){
                switchPublico.setChecked(true);
            }
        }

        /*String urlAtual = colecEdit.getImagens().get(0); // url em string
        Uri url = Uri.parse(urlAtual); // url em uri

        Glide.with(CadastroColecaoActivity.this).load(url).into(img1);
        listaFotosFirebase.add(urlAtual);
        try{
            if(colecEdit.getImagens().get(1) != null){
                urlAtual = colecEdit.getImagens().get(1);
                url = Uri.parse(urlAtual);
                Glide.with(CadastroColecaoActivity.this).load(url).into(img2);
                listaFotosFirebase.add(urlAtual);
            }
            if(colecEdit.getImagens().get(2) != null){
                urlAtual = colecEdit.getImagens().get(2);
                url = Uri.parse(urlAtual);
                Glide.with(CadastroColecaoActivity.this).load(url).into(img3);
                listaFotosFirebase.add(urlAtual);
            }
        }catch (Exception e){
            e.printStackTrace();
        }*/

    }

    private void chocoBarPadrao(String text){
        ChocoBar.builder().setActivity(CadastroColecaoActivity.this)
                .setText(text)
                .setDuration(ChocoBar.LENGTH_SHORT)
                .build()
                .show();
    }


    // RETORNANDO IMAGEM SELECIONADA
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == Activity.RESULT_OK){

            try{
                // Recuperando a img selecionada
                Uri imgUrl = data.getData();
                String localImg = imgUrl.toString(); // pegando o caminho da img
                Bitmap img = MediaStore.Images.Media.getBitmap(
                        getContext().getContentResolver(), imgUrl);// getContentResolver é um objeto responsavel por recuperar
                // os conteúdos do app android

                if(img != null){
                    int num = 0;

                    // Mostrando foto em umas das 3 imgViews
                    if(requestCode == 1){
                        Glide.with(CadastroColecaoActivity.this).load(imgUrl).into(img1);
                        imgUrl1 = localImg;
                    }else if(requestCode == 2){
                        Glide.with(CadastroColecaoActivity.this).load(imgUrl).into(img2);
                        imgUrl2 = localImg;
                    }else if(requestCode == 3){
                        Glide.with(CadastroColecaoActivity.this).load(imgUrl).into(img3);
                        imgUrl3 = localImg;
                    }
                }

            }catch (Exception e){
                e.printStackTrace();
            }

        }
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
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Permissão Negada");
        builder.setCancelable(false);
        builder.setMessage("Para utilizar o recurso é necessário aceitar a permissão.");
        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    private Context getContext() { // RETORNAR CONTEXTO DA TELA
        return this;
    }

}
