package com.ejemplo.comedor.modelos.Lista_menu;

import androidx.annotation.NonNull;

@SuppressWarnings("unused")
public class menu_fecha {

    private String menu_fecha;

    public menu_fecha() {
    }

    public menu_fecha(String menu_fecha) {
        this.menu_fecha = menu_fecha;
    }

    public String getMenu_fecha() {
        return menu_fecha;
    }

    public void setMenu_fecha(String menu_fecha) {
        this.menu_fecha = menu_fecha;
    }

    @NonNull
    @Override
    public String toString() {
        return menu_fecha;
    }
}
