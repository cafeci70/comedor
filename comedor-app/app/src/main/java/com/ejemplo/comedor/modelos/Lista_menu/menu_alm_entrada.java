package com.ejemplo.comedor.modelos.Lista_menu;

import androidx.annotation.NonNull;

@SuppressWarnings("unused")
public class menu_alm_entrada {

    private String menu_alm_entrada;

    public menu_alm_entrada() {
    }

    public menu_alm_entrada(String menu_alm_entrada) {
        this.menu_alm_entrada = menu_alm_entrada;
    }

    public String getMenu_alm_entrada() {
        return menu_alm_entrada;
    }

    public void setMenu_alm_entrada(String menu_alm_entrada) {
        this.menu_alm_entrada = menu_alm_entrada;
    }

    @NonNull
    @Override
    public String toString() {
        return menu_alm_entrada;
    }
}
