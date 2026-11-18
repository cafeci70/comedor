package com.ejemplo.comedor.modelos.Lista_menu;

import androidx.annotation.NonNull;

@SuppressWarnings("unused")
public class menu_cen_bebida {

    private String menu_cen_bebida;

    public menu_cen_bebida() {
    }

    public menu_cen_bebida(String menu_cen_bebida) {
        this.menu_cen_bebida = menu_cen_bebida;
    }

    public String getMenu_cen_bebida() {
        return menu_cen_bebida;
    }

    public void setMenu_cen_bebida(String menu_cen_bebida) {
        this.menu_cen_bebida = menu_cen_bebida;
    }

    @NonNull
    @Override
    public String toString() {
        return menu_cen_bebida;
    }
}
