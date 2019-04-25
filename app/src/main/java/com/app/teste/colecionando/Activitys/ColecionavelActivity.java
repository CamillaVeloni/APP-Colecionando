package com.app.teste.colecionando.Activitys;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.app.teste.colecionando.Modelos.Colecionável;
import com.app.teste.colecionando.R;
import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

public class ColecionavelActivity extends AppCompatActivity {

    private CarouselView carouselView;
    private TextView txtNome, txtDesc, txtCateg;
    private TextView txtValor, txtLocal, txtEtiq;
    private TextView txtLayout_etiq, txtLayout_local;
    private CheckBox checkAdq;
    private Switch switchPublic;
    private Colecionável c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_colecionavel);

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
        c = (Colecionável) intent.getSerializableExtra("colecSelecionado");

        // Verificando se foi mesmo recuperado o colecionável e setando os valores dentro dos componentes
        if(c != null){

            txtNome.setText(c.getNome());
            txtCateg.setText(c.getCategoria());
            txtDesc.setText(c.getDescrição());
            txtValor.setText(c.getValor());

            if(c.getEtiquetaCustomizada() != null){
                txtEtiq.setText(c.getEtiquetaCustomizada());
            }else{
                txtEtiq.setText("N/d");
            }

            checkAdq.setChecked(c.isBoolAdquirido());
            if(checkAdq.isChecked()){ // SE O CHECKBOX ADQUIRIDO ESTIVER FALSE SETAR VISIBILIDADE DE LOCAL_COMPRA E SWITCH_PUBLIC PARA GONE
                txtLocal.setText(c.getLocalCompra());
                switchPublic.setChecked(c.isBoolPublico());
            }else{
                txtLayout_local.setVisibility(View.GONE);
                txtLocal.setVisibility(View.GONE);
                checkAdq.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
                switchPublic.setVisibility(View.GONE);
            }

            ImageListener imgListener = new ImageListener() {
                @Override
                public void setImageForPosition(int position, ImageView imageView) {
                    String urlAtual = c.getImagens().get(position); // Recuperando url da imagem atual da pagina do carousel_view
                    Uri url = Uri.parse(urlAtual); // Transformando string em uri

                    Glide
                            .with(getContext())
                            .load(url)
                            .placeholder(R.raw.trail_loading)
                            .fitCenter()
                            .into(imageView);
                }
            };
            carouselView.setPageCount(c.getImagens().size()); // Colocando quantidade de paginas do carousel_view igual ao da lista de fotos
            carouselView.setImageListener(imgListener);

        }else{
            Log.d("Colecionável", "Tela Colecionável: instância nula");
        }

    }

    private Context getContext() { // RETORNAR CONTEXTO DA TELA
        return this;
    }
}
