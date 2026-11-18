package com.ejemplo.comedor.modelos.Lista_menu;

import androidx.annotation.NonNull;

@SuppressWarnings("unused")
public class menu_alm_bebida {

    private String menu_alm_bebida;

    public menu_alm_bebida() {
    }

    public menu_alm_bebida(String menu_alm_bebida) {
        this.menu_alm_bebida = menu_alm_bebida;
    }

    public String getMenu_alm_bebida() {
        return menu_alm_bebida;
    }

    public void setMenu_alm_bebida(String menu_alm_bebida) {
        this.menu_alm_bebida = menu_alm_bebida;
    }

    @NonNull
    @Override
    public String toString() {
        return menu_alm_bebida;
    }
}
