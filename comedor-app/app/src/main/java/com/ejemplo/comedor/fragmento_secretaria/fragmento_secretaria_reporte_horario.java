package com.ejemplo.comedor.fragmento_secretaria;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.ejemplo.comedor.ApiResponseHelper;
import com.ejemplo.comedor.R;
import com.ejemplo.comedor.Url;
import com.ejemplo.comedor.Variables_globales;
import com.ejemplo.comedor.VolleySingleton;
import com.ejemplo.comedor.modelos.MenuHorarioAdapter;
import com.ejemplo.comedor.modelos.MenuHorarioItem;

import org.json.JSONException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Displays the menu table for Secretary with edit and confirmed delete actions.
 */
public class fragmento_secretaria_reporte_horario extends Fragment {

    private ListView listaMenus;
    private final Url ip = new Url();

    public fragmento_secretaria_reporte_horario() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_fragmento_secretaria_reporte_horario, container, false);
        listaMenus = v.findViewById(R.id.lista_menus);

        if (listaMenus != null) {
            Ejecutar_Reporte_menu(ip.getIp() + "/comedor-api/Secretaria/reportes/reporte_horario.php");
        }

        return v;
    }

    public void cargar_fragmento(Fragment fragmentento) {
        if (!isAdded() || getActivity() == null) {
            return;
        }
        FragmentManager manager = getActivity().getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.contenedor_fragmento, fragmentento).commit();
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
                listaMenus.setAdapter(new MenuHorarioAdapter(getContext(), items, new MenuHorarioAdapter.OnMenuActionListener() {
                    @Override
                    public void onEdit(MenuHorarioItem item) {
                        Variables_globales.id_tabla_menu = item.menuId;
                        Variables_globales.menu_fecha = item.fecha;
                        Variables_globales.menu_desayuno = item.desayuno;
                        Variables_globales.menu_alm_entrada = item.almEntrada;
                        Variables_globales.menu_alm_postre = item.almPostre;
                        Variables_globales.menu_almmuerzo = item.almuerzo;
                        Variables_globales.menu_alm_bebida = item.almBebida;
                        Variables_globales.menu_cen_entrada = item.cenEntrada;
                        Variables_globales.menu_cen_postre = item.cenPostre;
                        Variables_globales.menu_cena = item.cena;
                        Variables_globales.menu_cen_bebida = item.cenBebida;
                        cargar_fragmento(new fragmento_secretaria_horario_editar());
                    }

                    @Override
                    public void onDelete(MenuHorarioItem item) {
                        confirmarEliminarMenu(item);
                    }
                }));
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

    private void confirmarEliminarMenu(MenuHorarioItem item) {
        if (!isAdded() || getContext() == null) {
            return;
        }
        new AlertDialog.Builder(getContext())
                .setTitle("Confirmar eliminación")
                .setMessage("¿Deseas eliminar el menú del " + item.fecha + "?")
                .setPositiveButton("Eliminar", (dialog, which) -> {
                    Variables_globales.id_tabla_menu = item.menuId;
                    Ejecutar_menu_eliminar(ip.getIp() + "/comedor-api/Secretaria/acciones/accion_menu_eliminar.php");
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void Ejecutar_menu_eliminar(String url) {
        if (!isAdded() || getContext() == null) {
            return;
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, response -> {
            if (!isAdded() || getContext() == null) {
                return;
            }
            String msg = ApiResponseHelper.extractMessage(response, "OPERACION EXITOSA");
            Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
            if (ApiResponseHelper.isSuccessResponse(response)) {
                Ejecutar_Reporte_menu(ip.getIp() + "/comedor-api/Secretaria/reportes/reporte_horario.php");
            }

        }, error -> {
            if (!isAdded() || getContext() == null) {
                return;
            }
            if (ApiResponseHelper.handleUnauthorized(this, error)) {
                return;
            }
            Toast.makeText(getContext(), ApiResponseHelper.extractErrorMessage(error, "No se pudo eliminar el menú"), Toast.LENGTH_SHORT).show();
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> parametros = new HashMap<>();
                parametros.put("id_menu", Variables_globales.id_tabla_menu);
                return parametros;
            }
        };
        VolleySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);
    }
}
