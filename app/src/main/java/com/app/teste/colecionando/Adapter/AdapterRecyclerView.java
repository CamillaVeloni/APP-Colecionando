package com.app.teste.colecionando.Adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.teste.colecionando.Modelos.Colecionável;
import com.app.teste.colecionando.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Random;


public class AdapterRecyclerView extends BaseQuickAdapter<Colecionável, BaseViewHolder> {

    public AdapterRecyclerView(List<Colecionável> data){
        super(R.layout.layout_item_colecionavel, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Colecionável item) {
        helper.setText(R.id.txtNome, item.getNome());
        helper.setText(R.id.txtDescrição, item.getDescrição());
        String dataEtiq = "";
        if(item.getEtiquetaCustomizada() != null){
            dataEtiq = "#" + item.getEtiquetaCustomizada() + " - ";
        }
        dataEtiq += item.getData();
        helper.setText(R.id.txtData, dataEtiq);

        // Pegar a primeira imagem da lista -- editar depois para pegar uma foto aleatoriamente (??)
        List<String> urlFotos = item.getImagens();
        String url = urlFotos.get(0);
        Picasso.get().load(url).into((ImageView) helper.getView(R.id.imgColecionavel));


    }

}
