package com.ejemplo.comedor.fragmento_secretaria;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.ejemplo.comedor.R;
import com.ejemplo.comedor.Url;
import com.ejemplo.comedor.Variables_globales;
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
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class fragmento_secretaria_reporte_horario extends Fragment {

    ListView lista_menu_fecha, lista_menu_desayuno, lista_menu_alm_entrada, lista_menu_alm_postre, lista_menu_almuerzo, lista_menu_alm_bebida,
            lista_menu_cen_entrada, lista_menu_cen_postre, lista_menu_cena, lista_menu_cen_bebida;

    ListView lista_editar, lista_eliminar;

    Url ip = new Url();

    String[] editar, eliminar;
    String[] id_tabla_menu;

    Object[] array_alm_bebida, array_alm_entrada, array_alm_postre, array_almuerzo, array_cen_bebida, array_cen_entrada,
            array_cen_postre, array_cena, array_desayuno, array_fecha;

    public fragmento_secretaria_reporte_horario() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_fragmento_secretaria_reporte_horario, container, false);

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

        lista_editar = v.findViewById(R.id.lista_editar);
        lista_eliminar = v.findViewById(R.id.lista_eliminar);

        if (lista_menu_fecha == null) {
            return v;
        }

        Ejecutar_Reporte_menu(ip.getIp() + "/comedor-api/Secretaria/reportes/reporte_horario.php");

        lista_editar.setOnItemClickListener((parent, view, position, id) -> {
            if (id_tabla_menu == null || position < 0 || position >= id_tabla_menu.length
                    || array_fecha == null || array_desayuno == null || array_alm_entrada == null) {
                return;
            }

            Variables_globales.id_tabla_menu = id_tabla_menu[position];
            Variables_globales.menu_fecha = array_fecha[position].toString();
            Variables_globales.menu_desayuno = array_desayuno[position].toString();
            Variables_globales.menu_alm_entrada = array_alm_entrada[position].toString();
            Variables_globales.menu_alm_postre = array_alm_postre[position].toString();
            Variables_globales.menu_almmuerzo = array_almuerzo[position].toString();
            Variables_globales.menu_alm_bebida = array_alm_bebida[position].toString();
            Variables_globales.menu_cen_entrada = array_cen_entrada[position].toString();
            Variables_globales.menu_cen_postre = array_cen_postre[position].toString();
            Variables_globales.menu_cena = array_cena[position].toString();
            Variables_globales.menu_cen_bebida = array_cen_bebida[position].toString();

            //redireccionamos en el fragmento stock editar
            cargar_fragmento(new fragmento_secretaria_horario_editar());
        });

        lista_eliminar.setOnItemClickListener((parent, view, position, id) -> {
            if (id_tabla_menu == null || position < 0 || position >= id_tabla_menu.length) {
                return;
            }

            Variables_globales.id_tabla_menu = id_tabla_menu[position];
            Ejecutar_menu_eliminar(ip.getIp() + "/comedor-api/Secretaria/acciones/accion_menu_eliminar.php");
        });

        return v;
    }

    @SuppressWarnings({"DiscouragedApi", "SourceLockedOrientationActivity"})
    public void cargar_fragmento(Fragment fragmentento) {
        if (!isAdded() || getActivity() == null) {
            return;
        }
        FragmentManager manager = getActivity().getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.contenedor_fragmento, fragmentento).commit();
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
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

            editar = new String[response.length()];
            eliminar = new String[response.length()];
            id_tabla_menu = new String[response.length()];

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

                    editar[i] = "EDITAR";
                    eliminar[i] = "ELIMINAR";
                    id_tabla_menu[i] = jsonObject.getString("menu_id");
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

                lista_editar.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, editar));
                lista_eliminar.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, eliminar));

                array_fecha = lista_fecha.toArray();
                array_desayuno = lista_desayuno.toArray();
                array_alm_entrada = lista_alm_entrada.toArray();
                array_alm_postre = lista_alm_postre.toArray();
                array_almuerzo = lista_almuerzo.toArray();
                array_alm_bebida = lista_alm_bebida.toArray();
                array_cen_entrada = lista_cen_entrada.toArray();
                array_cen_postre = lista_cen_postre.toArray();
                array_cena = lista_cena.toArray();
                array_cen_bebida = lista_cen_bebida.toArray();

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

    private void Ejecutar_menu_eliminar(String url) {
        if (!isAdded() || getContext() == null) {
            return;
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, response -> {
            if (!isAdded() || getContext() == null) {
                return;
            }
            Toast.makeText(getContext(), "OPERACION EXITOSA", Toast.LENGTH_SHORT).show();
            Ejecutar_Reporte_menu(ip.getIp() + "/comedor-api/Secretaria/reportes/reporte_horario.php");

        }, error -> {
            if (!isAdded() || getContext() == null) {
                return;
            }
            Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> parametros = new HashMap<>();
                //PARAMETROS PARA ENVIAR POR EL METODO POST
                parametros.put("id_menu", Variables_globales.id_tabla_menu);
                return parametros;
            }
        };
        VolleySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);
    }
}
