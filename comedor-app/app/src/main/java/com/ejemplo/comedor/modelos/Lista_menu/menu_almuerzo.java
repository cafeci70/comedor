package com.ejemplo.comedor.modelos.Lista_menu;

import androidx.annotation.NonNull;

@SuppressWarnings("unused")
public class menu_almuerzo {

    private String menu_almuerzo;

    public menu_almuerzo() {
    }

    public menu_almuerzo(String menu_almuerzo) {
        this.menu_almuerzo = menu_almuerzo;
    }

    public String getMenu_almuerzo() {
        return menu_almuerzo;
    }

    public void setMenu_almuerzo(String menu_almuerzo) {
        this.menu_almuerzo = menu_almuerzo;
    }

    @NonNull
    @Override
    public String toString() {
        return menu_almuerzo;
    }
}
