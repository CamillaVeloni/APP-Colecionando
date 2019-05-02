package com.app.teste.colecionando.Activitys;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.app.teste.colecionando.Modelos.Colecionável;
import com.app.teste.colecionando.R;
import com.bumptech.glide.Glide;
import com.crowdfire.cfalertdialog.CFAlertDialog;
import com.pd.chocobar.ChocoBar;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

public class ColecionavelActivity extends AppCompatActivity {

    private CarouselView carouselView;
    private TextView txtNome, txtDesc, txtCateg;
    private TextView txtValor, txtLocal, txtEtiq;
    private TextView txtLayout_etiq, txtLayout_local;
    private CheckBox checkAdq;
    private Switch switchPublic;
    public static final int REQUEST_CODE = 1;
    private Colecionável colec;
    private boolean boolGaleria;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_colecionavel);

        Toolbar toolbar = findViewById(R.id.toolbar_detColec);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // colocando botão voltar para activity principal

        // Inicializando componentes
        carouselView = findViewById(R.id.carouselView);
        txtNome = findViewById(R.id.colecSelecionado_nome);
        txtDesc = findViewById(R.id.colecSelecionado_desc);
        txtCateg = findViewById(R.id.colecSelecionado_categ);
        txtValor = findViewById(R.id.colecSelecionado_valor);
        txtLocal = findViewById(R.id.colecSelecionado_comp);
        txtEtiq = findViewById(R.id.colecSelecionado_etiq);
        txtLayout_local = findViewById(R.id.colecSelecionado_localLayout);
        txtLayout_etiq = findViewById(R.id.colecSelecionado_etiqLayout);
        checkAdq = findViewById(R.id.colecSelecionado_adq);
        switchPublic = findViewById(R.id.colecSelecionado_public);

        // Recuperando colecionável selecionado pela intent
        Intent intent = getIntent();
        colec = (Colecionável) intent.getSerializableExtra("colecSelecionado");
        boolGaleria = intent.getBooleanExtra("boolGaleria", false);
        setandoInfo();
    }

    private void setandoInfo(){

        // Verificando se foi mesmo recuperado o colecionável e setando os valores dentro dos componentes
        if(colec != null){

            txtNome.setText(colec.getNome());
            txtCateg.setText(colec.getCategoria());
            txtDesc.setText(colec.getDescrição());
            txtValor.setText(colec.getValor());

            if(colec.getEtiquetaCustomizada() != null){
                txtEtiq.setText(colec.getEtiquetaCustomizada());
            }else{
                txtEtiq.setText("N/d");
            }

            checkAdq.setChecked(colec.isBoolAdquirido());
            if(checkAdq.isChecked()){ // SE O CHECKBOX ADQUIRIDO ESTIVER FALSE SETAR VISIBILIDADE DE LOCAL_COMPRA E SWITCH_PUBLIC PARA GONE
                txtLocal.setText(colec.getLocalCompra());
                switchPublic.setChecked(colec.isBoolPublico());
            }else{
                txtLayout_local.setVisibility(View.GONE);
                txtLocal.setVisibility(View.GONE);
                checkAdq.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
                switchPublic.setVisibility(View.GONE);
            }

            ImageListener imgListener = new ImageListener() {
                @Override
                public void setImageForPosition(int position, ImageView imageView) {
                    String urlAtual = colec.getImagens().get(position); // Recuperando url da imagem atual da pagina do carousel_view
                    Uri url = Uri.parse(urlAtual); // Transformando string em uri

                    Glide
                            .with(getContext())
                            .load(url)
                            .fitCenter()
                            .into(imageView);
                }
            };
            carouselView.setPageCount(colec.getImagens().size()); // Colocando quantidade de paginas do carousel_view igual ao da lista de fotos
            carouselView.setImageListener(imgListener);

        }else{
            Log.d("Colecionável", "Tela Colecionável: instância nula");
        }
    }

    // Inflando menu //
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflar o menu
        if(!boolGaleria){
            getMenuInflater().inflate(R.menu.menu_colec, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) { // Ações para a action bar ficam aqui
        //noinspection SimplifiableIfStatement
        switch (item.getItemId()) {
            case R.id.action_editar:
                funçãoEditar();
                break;
            case R.id.action_deletar:
                funçãoDeletar();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void funçãoEditar(){

        Intent intent = new Intent(ColecionavelActivity.this, CadastroColecaoActivity.class);
        intent.putExtra("colecEditar", colec);
        intent.putExtra("boolEditar", true);
        startActivityForResult(intent, REQUEST_CODE);
    }

    private void funçãoDeletar(){

        // Criando o alert dialog com o builder
        CFAlertDialog.Builder builder = new CFAlertDialog.Builder(getContext())
                .setDialogStyle(CFAlertDialog.CFAlertStyle.BOTTOM_SHEET)
                .setTitle("Deletar Colecionável")
                .setCornerRadius(20)
                .setMessage("Deseja deletar o colecionável? Não será possível restaurar o colecionável depois!")
                .setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary))
                .setIcon(R.drawable.ic_delete_24dp)
                .addButton("Deletar", -1, -1, CFAlertDialog.CFAlertActionStyle.NEGATIVE,
                        CFAlertDialog.CFAlertActionAlignment.JUSTIFIED, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                colec.removerColecionável();

                                ChocoBar.builder().setActivity(ColecionavelActivity.this)
                                        .setText("Deletado com sucesso!")
                                        .setDuration(ChocoBar.LENGTH_SHORT)
                                        .build()
                                        .show();
                                dialog.dismiss();
                                finish();
                            }
                        }).addButton("Cancelar", -1, -1, CFAlertDialog.CFAlertActionStyle.DEFAULT,
                        CFAlertDialog.CFAlertActionAlignment.JUSTIFIED, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which){
                                dialog.dismiss();
                            }
                        }).setCancelable(false);

        // Mostrar o alert
        builder.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){

        if (requestCode == REQUEST_CODE) {
            if (resultCode == CadastroColecaoActivity.RESULT_CODE) {
                colec = (Colecionável) data.getSerializableExtra("colecAtualizado");
                setandoInfo();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private Context getContext() { // RETORNAR CONTEXTO DA TELA
        return this;
    }
}
