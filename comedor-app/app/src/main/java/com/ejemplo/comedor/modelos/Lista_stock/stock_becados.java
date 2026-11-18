package com.ejemplo.comedor.modelos.Lista_stock;

import androidx.annotation.NonNull;

@SuppressWarnings("unused")
public class stock_becados {

    private String stoc_becados;

    public stock_becados() {
    }

    public stock_becados(String stoc_becados) {
        this.stoc_becados = stoc_becados;
    }

    public String getStoc_becados() {
        return stoc_becados;
    }

    public void setStoc_becados(String stoc_becados) {
        this.stoc_becados = stoc_becados;
    }

    @NonNull
    @Override
    public String toString() {
        return stoc_becados;
    }
}
