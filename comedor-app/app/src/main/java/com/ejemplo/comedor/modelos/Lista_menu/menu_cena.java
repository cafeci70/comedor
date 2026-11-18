package com.ejemplo.comedor.modelos.Lista_menu;

import androidx.annotation.NonNull;

@SuppressWarnings("unused")
public class menu_cena {

    private String menu_cena;

    public menu_cena() {
    }

    public menu_cena(String menu_cena) {
        this.menu_cena = menu_cena;
    }

    public String getMenu_cena() {
        return menu_cena;
    }

    public void setMenu_cena(String menu_cena) {
        this.menu_cena = menu_cena;
    }

    @NonNull
    @Override
    public String toString() {
        return menu_cena;
    }
}
