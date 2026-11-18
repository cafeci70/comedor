package com.ejemplo.comedor.modelos.Lista_stock;

import androidx.annotation.NonNull;

@SuppressWarnings("unused")
public class stock_periodo {

    private String stock_periodo;

    public stock_periodo() {
    }

    public stock_periodo(String stock_periodo) {
        this.stock_periodo = stock_periodo;
    }

    public String getStock_periodo() {
        return stock_periodo;
    }

    public void setStock_periodo(String stock_periodo) {
        this.stock_periodo = stock_periodo;
    }

    @NonNull
    @Override
    public String toString() {
        return stock_periodo;
    }
}
