package com.ejemplo.comedor.modelos;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a daily meal menu row returned by reporte_horario.php.
 */
public class MenuHorarioItem {
    public final String menuId;
    public final String fecha;
    public final String desayuno;
    public final String almEntrada;
    public final String almPostre;
    public final String almuerzo;
    public final String almBebida;
    public final String cenEntrada;
    public final String cenPostre;
    public final String cena;
    public final String cenBebida;

    public MenuHorarioItem(String menuId, String fecha, String desayuno,
                           String almEntrada, String almPostre, String almuerzo, String almBebida,
                           String cenEntrada, String cenPostre, String cena, String cenBebida) {
        this.menuId = menuId;
        this.fecha = fecha;
        this.desayuno = desayuno;
        this.almEntrada = almEntrada;
        this.almPostre = almPostre;
        this.almuerzo = almuerzo;
        this.almBebida = almBebida;
        this.cenEntrada = cenEntrada;
        this.cenPostre = cenPostre;
        this.cena = cena;
        this.cenBebida = cenBebida;
    }

    public static List<MenuHorarioItem> fromJsonArray(JSONArray response) throws JSONException {
        List<MenuHorarioItem> items = new ArrayList<>();
        if (response == null) {
            return items;
        }
        for (int i = 0; i < response.length(); i++) {
            JSONObject obj = response.getJSONObject(i);
            items.add(new MenuHorarioItem(
                    obj.optString("menu_id", ""),
                    obj.getString("menu_fecha"),
                    obj.getString("menu_desayuno"),
                    obj.getString("menu_alm_entrada"),
                    obj.getString("menu_alm_postre"),
                    obj.getString("menu_almuerzo"),
                    obj.getString("menu_alm_bebida"),
                    obj.getString("menu_cen_entrada"),
                    obj.getString("menu_cen_postre"),
                    obj.getString("menu_cena"),
                    obj.getString("menu_cen_bebida")
            ));
        }
        return items;
    }
}
