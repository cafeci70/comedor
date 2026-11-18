package com.ejemplo.comedor.modelos.Lista_menu;

import androidx.annotation.NonNull;

@SuppressWarnings("unused")
public class menu_desayuno {

    private String menu_desayuno;

    public menu_desayuno() {
    }

    public menu_desayuno(String menu_desayuno) {
        this.menu_desayuno = menu_desayuno;
    }

    public String getMenu_desayuno() {
        return menu_desayuno;
    }

    public void setMenu_desayuno(String menu_desayuno) {
        this.menu_desayuno = menu_desayuno;
    }

    @NonNull
    @Override
    public String toString() {
        return menu_desayuno;
    }
}
