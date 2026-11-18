package com.ejemplo.comedor.modelos.Lista_menu;

import androidx.annotation.NonNull;

@SuppressWarnings("unused")
public class menu_alm_postre {

    private String menu_alm_postre;

    public menu_alm_postre() {
    }

    public menu_alm_postre(String menu_alm_postre) {
        this.menu_alm_postre = menu_alm_postre;
    }

    public String getMenu_alm_postre() {
        return menu_alm_postre;
    }

    public void setMenu_alm_postre(String menu_alm_postre) {
        this.menu_alm_postre = menu_alm_postre;
    }

    @NonNull
    @Override
    public String toString() {
        return menu_alm_postre;
    }
}
