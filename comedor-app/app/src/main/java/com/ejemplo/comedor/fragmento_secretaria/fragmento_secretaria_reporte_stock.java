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
import com.ejemplo.comedor.modelos.StockAdapter;
import com.ejemplo.comedor.modelos.StockItem;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Displays scholarship stock periods with edit and confirmed delete actions.
 */
public class fragmento_secretaria_reporte_stock extends Fragment {

    private ListView listaStock;
    private final Url ip = new Url();

    public fragmento_secretaria_reporte_stock() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_fragmento_secretaria_reporte_stock, container, false);
        listaStock = v.findViewById(R.id.lista_stock);

        if (listaStock != null) {
            Ejecutar_Reporte_stock(ip.getIp() + "/comedor-api/Secretaria/reportes/reporte_stock.php");
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

    private void confirmarEliminarStock(StockItem item) {
        if (!isAdded() || getContext() == null) {
            return;
        }
        new AlertDialog.Builder(getContext())
                .setTitle("Confirmar eliminación")
                .setMessage("¿Deseas eliminar el stock del periodo " + item.periodo + "?")
                .setPositiveButton("Eliminar", (dialog, which) -> {
                    Variables_globales.id_tabla_stock = item.stocId;
                    ejecutar_stock_eliminar(ip.getIp() + "/comedor-api/Secretaria/acciones/accion_stock_eliminar.php");
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void ejecutar_stock_eliminar(String url) {
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
                Ejecutar_Reporte_stock(ip.getIp() + "/comedor-api/Secretaria/reportes/reporte_stock.php");
            }

        }, error -> {
            if (!isAdded() || getContext() == null) {
                return;
            }
            if (ApiResponseHelper.handleUnauthorized(this, error)) {
                return;
            }
            Toast.makeText(getContext(), ApiResponseHelper.extractErrorMessage(error, "No se pudo eliminar el stock"), Toast.LENGTH_SHORT).show();
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> parametros = new HashMap<>();
                parametros.put("id_stock", Variables_globales.id_tabla_stock);
                return parametros;
            }
        };
        VolleySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);
    }

    public void Ejecutar_Reporte_stock(String url) {
        if (!isAdded() || getContext() == null) {
            return;
        }

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, response -> {
            if (!isAdded() || getContext() == null) {
                return;
            }
            List<StockItem> items = new ArrayList<>();
            try {
                for (int i = 0; i < response.length(); i++) {
                    JSONObject obj = response.getJSONObject(i);
                    items.add(new StockItem(
                            obj.getString("stoc_id"),
                            obj.getString("stoc_periodo"),
                            obj.getString("stoc_becados")
                    ));
                }
                listaStock.setAdapter(new StockAdapter(getContext(), items, new StockAdapter.OnStockActionListener() {
                    @Override
                    public void onEdit(StockItem item) {
                        Variables_globales.id_tabla_stock = item.stocId;
                        Variables_globales.periodo = item.periodo;
                        Variables_globales.becados = item.becados;
                        cargar_fragmento(new fragmento_secretaria_stock_editar());
                    }

                    @Override
                    public void onDelete(StockItem item) {
                        confirmarEliminarStock(item);
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
}
