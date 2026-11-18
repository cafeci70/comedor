package com.ejemplo.comedor.fragmento_alumno;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.toolbox.JsonArrayRequest;
import com.ejemplo.comedor.R;
import com.ejemplo.comedor.Url;
import com.ejemplo.comedor.VolleySingleton;
import com.ejemplo.comedor.modelos.Lista_menu.menu_alm_bebida;
import com.ejemplo.comedor.modelos.Lista_menu.menu_alm_entrada;
import com.ejemplo.comedor.modelos.Lista_menu.menu_alm_postre;
import com.ejemplo.comedor.modelos.Lista_menu.menu_almuerzo;
import com.ejemplo.comedor.modelos.Lista_menu.menu_cen_bebida;
import com.ejemplo.comedor.modelos.Lista_menu.menu_cen_entrada;
import com.ejemplo.comedor.modelos.Lista_menu.menu_cen_postre;
import com.ejemplo.comedor.modelos.Lista_menu.menu_cena;
import com.ejemplo.comedor.modelos.Lista_menu.menu_desayuno;
import com.ejemplo.comedor.modelos.Lista_menu.menu_fecha;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class fragmento_alumno_menu extends Fragment {

    ListView lista_menu_fecha, lista_menu_desayuno, lista_menu_alm_entrada, lista_menu_alm_postre, lista_menu_almuerzo, lista_menu_alm_bebida,
            lista_menu_cen_entrada, lista_menu_cen_postre, lista_menu_cena, lista_menu_cen_bebida;
    Url ip = new Url();

    public fragmento_alumno_menu() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_fragmento_alumno_menu, container, false);

        lista_menu_fecha = v.findViewById(R.id.lista_menu_fecha);
        lista_menu_desayuno = v.findViewById(R.id.lista_menu_desayuno);
        lista_menu_alm_entrada = v.findViewById(R.id.lista_menu_alm_entrada);
        lista_menu_alm_postre = v.findViewById(R.id.lista_menu_alm_postre);
        lista_menu_almuerzo = v.findViewById(R.id.lista_menu_almuerzo);
        lista_menu_alm_bebida = v.findViewById(R.id.lista_menu_alm_bebida);
        lista_menu_cen_entrada = v.findViewById(R.id.lista_menu_cen_entrada);
        lista_menu_cen_postre = v.findViewById(R.id.lista_menu_cen_postre);
        lista_menu_cena = v.findViewById(R.id.lista_menu_cena);
        lista_menu_cen_bebida = v.findViewById(R.id.lista_menu_cen_bebida);

        if (lista_menu_fecha == null) {
            return v;
        }

        Ejecutar_Reporte_menu(ip.getIp() + "/comedor-api/Secretaria/reportes/reporte_horario.php");

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
            ArrayList<menu_fecha> lista_fecha = new ArrayList<>();
            ArrayList<menu_desayuno> lista_desayuno = new ArrayList<>();
            ArrayList<menu_alm_entrada> lista_alm_entrada = new ArrayList<>();
            ArrayList<menu_alm_postre> lista_alm_postre = new ArrayList<>();
            ArrayList<menu_almuerzo> lista_almuerzo = new ArrayList<>();
            ArrayList<menu_alm_bebida> lista_alm_bebida = new ArrayList<>();
            ArrayList<menu_cen_entrada> lista_cen_entrada = new ArrayList<>();
            ArrayList<menu_cen_postre> lista_cen_postre = new ArrayList<>();
            ArrayList<menu_cena> lista_cena = new ArrayList<>();
            ArrayList<menu_cen_bebida> lista_cen_bebida = new ArrayList<>();

            try {
                for (int i = 0; i < response.length(); i++) {
                    menu_fecha m1 = new menu_fecha();
                    menu_desayuno m2 = new menu_desayuno();
                    menu_alm_entrada m3 = new menu_alm_entrada();
                    menu_alm_postre m4 = new menu_alm_postre();
                    menu_almuerzo m5 = new menu_almuerzo();
                    menu_alm_bebida m6 = new menu_alm_bebida();
                    menu_cen_entrada m7 = new menu_cen_entrada();
                    menu_cen_postre m8 = new menu_cen_postre();
                    menu_cena m9 = new menu_cena();
                    menu_cen_bebida m10 = new menu_cen_bebida();

                    JSONObject jsonObject = response.getJSONObject(i);
                    m1.setMenu_fecha(jsonObject.getString("menu_fecha"));
                    m2.setMenu_desayuno(jsonObject.getString("menu_desayuno"));
                    m3.setMenu_alm_entrada(jsonObject.getString("menu_alm_entrada"));
                    m4.setMenu_alm_postre(jsonObject.getString("menu_alm_postre"));
                    m5.setMenu_almuerzo(jsonObject.getString("menu_almuerzo"));
                    m6.setMenu_alm_bebida(jsonObject.getString("menu_alm_bebida"));
                    m7.setMenu_cen_entrada(jsonObject.getString("menu_cen_entrada"));
                    m8.setMenu_cen_postre(jsonObject.getString("menu_cen_postre"));
                    m9.setMenu_cena(jsonObject.getString("menu_cena"));
                    m10.setMenu_cen_bebida(jsonObject.getString("menu_cen_bebida"));

                    lista_fecha.add(m1);
                    lista_desayuno.add(m2);
                    lista_alm_entrada.add(m3);
                    lista_alm_postre.add(m4);
                    lista_almuerzo.add(m5);
                    lista_alm_bebida.add(m6);
                    lista_cen_entrada.add(m7);
                    lista_cen_postre.add(m8);
                    lista_cena.add(m9);
                    lista_cen_bebida.add(m10);
                }

                lista_menu_fecha.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, lista_fecha));
                lista_menu_desayuno.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, lista_desayuno));
                lista_menu_alm_entrada.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, lista_alm_entrada));
                lista_menu_alm_postre.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, lista_alm_postre));
                lista_menu_almuerzo.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, lista_almuerzo));
                lista_menu_alm_bebida.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, lista_alm_bebida));
                lista_menu_cen_entrada.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, lista_cen_entrada));
                lista_menu_cen_postre.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, lista_cen_postre));
                lista_menu_cena.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, lista_cena));
                lista_menu_cen_bebida.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, lista_cen_bebida));

            } catch (JSONException e) {
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }, error -> {
            if (!isAdded() || getContext() == null) {
                return;
            }
            Toast.makeText(getContext(), "ERROR EN LA BUSQUEDA", Toast.LENGTH_SHORT).show();
        });

        VolleySingleton.getInstance(getContext()).addToRequestQueue(jsonArrayRequest);
    }
}
