package com.app.teste.colecionando.Ajuda;

import com.app.teste.colecionando.Modelos.Colecionável;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ColecionaveisData {

    private List<Colecionável> mList = new ArrayList<>();

    public ColecionaveisData(List<Colecionável> mList) {
        this.mList = mList;
    }
    public ColecionaveisData() {

    }

    public List<Colecionável> getTdsColecionaveis() {
        return mList;
    }

    public void setmList(List<Colecionável> mList) {
        this.mList = mList;
    }

    public List<String> getKeysUnicasCategoria() {
        List<String> categorias = new ArrayList<>();
        for (Colecionável colecionável : mList) {
            if (!categorias.contains(colecionável.getCategoria())) {
                categorias.add(colecionável.getCategoria());
            }
        }
        Collections.sort(categorias);
        return categorias;
    }

    public List<String> getKeysUnicasEtiqueta() {
        List<String> etiquetas = new ArrayList<>();
        for (Colecionável colecionável : mList) {
            if(!colecionável.getEtiquetaCustomizada().isEmpty()){
                if (!etiquetas.contains(colecionável.getEtiquetaCustomizada())) {
                    etiquetas.add(colecionável.getEtiquetaCustomizada());
                }
            }
        }
        Collections.sort(etiquetas);
        return etiquetas;
    }

    public List<Colecionável> getColecFiltradosCategoria (String categoria, List<Colecionável> mList) {
        List<Colecionável> tempList = new ArrayList<>();
        for (Colecionável colec : mList) {
            if (colec.getCategoria().equals(categoria)) {
                tempList.add(colec);
            }

        }
        return tempList;
    }

    public List<Colecionável> getColecFiltradosEtiqueta (String etiqueta, List<Colecionável> mList) {
        List<Colecionável> tempList = new ArrayList<>();
        for (Colecionável colec : mList) {
            if(!colec.getEtiquetaCustomizada().isEmpty()){
                if (colec.getEtiquetaCustomizada().equals(etiqueta)) {
                    tempList.add(colec);
                }
            }
        }
        return tempList;
    }

}
