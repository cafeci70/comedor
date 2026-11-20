package com.ejemplo.comedor.fragmento_alumno;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.toolbox.JsonArrayRequest;
import com.ejemplo.comedor.ApiResponseHelper;
import com.ejemplo.comedor.R;
import com.ejemplo.comedor.Url;
import com.ejemplo.comedor.VolleySingleton;
import com.ejemplo.comedor.modelos.MenuHorarioAdapter;
import com.ejemplo.comedor.modelos.MenuHorarioItem;

import org.json.JSONException;

import java.util.List;

/**
 * Displays the weekly/daily menu table for students.
 */
public class fragmento_alumno_menu extends Fragment {

    private ListView listaMenus;
    private final Url ip = new Url();

    public fragmento_alumno_menu() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_fragmento_alumno_menu, container, false);
        listaMenus = v.findViewById(R.id.lista_menus);

        if (listaMenus != null) {
            Ejecutar_Reporte_menu(ip.getIp() + "/comedor-api/Secretaria/reportes/reporte_horario.php");
        }

        return v;
    }

    public void Ejecutar_Reporte_menu(String url) {
        if (!isAdded() || getContext() == null) {
            return;
        }

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, response -> {
            if (!isAdded() || getContext() == null) {
                return;
            }
            try {
                List<MenuHorarioItem> items = MenuHorarioItem.fromJsonArray(response);
                listaMenus.setAdapter(new MenuHorarioAdapter(getContext(), items, null));
            } catch (JSONException e) {
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }, error -> {
            if (!isAdded() || getContext() == null) {
                return;
            }
            if (ApiResponseHelper.handleUnauthorized(this, error)) {
                return;
            }
            Toast.makeText(getContext(), "ERROR EN LA BUSQUEDA", Toast.LENGTH_SHORT).show();
        });

        VolleySingleton.getInstance(getContext()).addToRequestQueue(jsonArrayRequest);
    }
}
