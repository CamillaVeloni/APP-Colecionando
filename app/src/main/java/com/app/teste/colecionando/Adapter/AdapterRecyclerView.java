package com.app.teste.colecionando.Adapter;

import android.net.Uri;
import android.widget.ImageView;

import com.app.teste.colecionando.Modelos.Colecionável;
import com.app.teste.colecionando.R;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;


public class AdapterRecyclerView extends BaseItemDraggableAdapter<Colecionável, BaseViewHolder> {


    public AdapterRecyclerView(List<Colecionável> data){
        super(R.layout.layout_item_colecionavel, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Colecionável item) {
        helper.setText(R.id.txtNome, item.getNome());
        helper.setText(R.id.txtDescrição, item.getDescrição());
        helper.setText(R.id.txtData, item.getData());


        // Pegar a primeira imagem da lista
        List<String> urlFotos = item.getImagens();
        String urlString = urlFotos.get(0);
        Uri url = Uri.parse(urlString);

        Glide
                .with(this.mContext)
                .load(url)
                .into((ImageView) helper.getView(R.id.imgColecionavel));

    }


}

