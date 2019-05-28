package com.app.teste.colecionando.Adapter;

import com.app.teste.colecionando.Ajuda.MuseuItem;
import com.app.teste.colecionando.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

public class MuseuVirtualAdapter extends BaseQuickAdapter<MuseuItem, BaseViewHolder> {
    public MuseuVirtualAdapter(int layoutResId, List data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MuseuItem item) {
        helper.setText(R.id.texto, item.getTitulo());
        helper.setImageResource(R.id.imagem_categoria, item.getImagem());
    }
}
