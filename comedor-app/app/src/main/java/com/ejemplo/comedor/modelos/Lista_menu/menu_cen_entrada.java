package com.ejemplo.comedor.modelos.Lista_menu;

import androidx.annotation.NonNull;

@SuppressWarnings("unused")
public class menu_cen_entrada {

    private String menu_cen_entrada;

    public menu_cen_entrada() {
    }

    public menu_cen_entrada(String menu_cen_entrada) {
        this.menu_cen_entrada = menu_cen_entrada;
    }

    public String getMenu_cen_entrada() {
        return menu_cen_entrada;
    }

    public void setMenu_cen_entrada(String menu_cen_entrada) {
        this.menu_cen_entrada = menu_cen_entrada;
    }

    @NonNull
    @Override
    public String toString() {
        return menu_cen_entrada;
    }
}
