package com.ejemplo.comedor.modelos.Lista_menu;

import androidx.annotation.NonNull;

@SuppressWarnings("unused")
public class menu_cen_postre {

    private String menu_cen_postre;

    public menu_cen_postre() {
    }

    public menu_cen_postre(String menu_cen_postre) {
        this.menu_cen_postre = menu_cen_postre;
    }

    public String getMenu_cen_postre() {
        return menu_cen_postre;
    }

    public void setMenu_cen_postre(String menu_cen_postre) {
        this.menu_cen_postre = menu_cen_postre;
    }

    @NonNull
    @Override
    public String toString() {
        return menu_cen_postre;
    }
}
