package com.ejemplo.comedor.fragmento_secretaria;

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
import com.ejemplo.comedor.ApiResponseHelper;
import com.ejemplo.comedor.R;
import com.ejemplo.comedor.Url;
import com.ejemplo.comedor.Variables_globales;
import com.ejemplo.comedor.VolleySingleton;
import com.ejemplo.comedor.modelos.Lista_stock.stock_becados;
import com.ejemplo.comedor.modelos.Lista_stock.stock_periodo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class fragmento_secretaria_reporte_stock extends Fragment {
    ListView lista_stock_periodo_, lista_stock_becados_;
    ListView lista_editar, lista_eliminar;

    String[] editar, eliminar;
    String[] id_tabla_stock;

    Object[] array_stock_periodo, array_stock_becado;

    Url ip = new Url();

    public fragmento_secretaria_reporte_stock() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_fragmento_secretaria_reporte_stock, container, false);
        lista_stock_periodo_ = v.findViewById(R.id.lista_stock_periodo);
        lista_stock_becados_ = v.findViewById(R.id.lista_stock_becados);

        lista_editar = v.findViewById(R.id.lista_editar);
        lista_eliminar = v.findViewById(R.id.lista_eliminar);

        Ejecutar_Reporte_stock(ip.getIp() + "/comedor-api/Secretaria/reportes/reporte_stock.php");

        lista_editar.setOnItemClickListener((parent, view, position, id) -> {
            if (id_tabla_stock == null || position < 0 || position >= id_tabla_stock.length
                    || array_stock_periodo == null || array_stock_becado == null) {
                return;
            }

            Variables_globales.id_tabla_stock = id_tabla_stock[position];
            Variables_globales.periodo = array_stock_periodo[position].toString();
            Variables_globales.becados = array_stock_becado[position].toString();

            //redireccionamos en el fragmento stock editar
            cargar_fragmento(new fragmento_secretaria_stock_editar());
        });

        lista_eliminar.setOnItemClickListener((parent, view, position, id) -> {
            if (id_tabla_stock == null || position < 0 || position >= id_tabla_stock.length) {
                return;
            }

            Variables_globales.id_tabla_stock = id_tabla_stock[position];
            ejecutar_stock_eliminar(ip.getIp() + "/comedor-api/Secretaria/acciones/accion_stock_eliminar.php");
        });

        return v;
    }

    public void cargar_fragmento(Fragment fragmentento) {
        if (!isAdded() || getActivity() == null) {
            return;
        }
        FragmentManager manager = getActivity().getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.contenedor_fragmento, fragmentento).commit();
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
            Toast.makeText(getContext(), ApiResponseHelper.extractErrorMessage(error, "No se pudo eliminar el stock"), Toast.LENGTH_SHORT).show();
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> parametros = new HashMap<>();
                //PARAMETROS PARA ENVIAR POR EL METODO POST
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
            ArrayList<stock_periodo> lista_stock_periodo = new ArrayList<>();
            ArrayList<stock_becados> lista_stock_becados = new ArrayList<>();

            //instanciamos los array
            editar = new String[response.length()];
            eliminar = new String[response.length()];
            id_tabla_stock = new String[response.length()];
            try {
                for (int i = 0; i < response.length(); i++) {
                    stock_periodo s1 = new stock_periodo();
                    stock_becados s2 = new stock_becados();

                    JSONObject jsonObject = response.getJSONObject(i);
                    s1.setStock_periodo(jsonObject.getString("stoc_periodo"));
                    s2.setStoc_becados(jsonObject.getString("stoc_becados"));

                    //guardarmos datos en el array
                    editar[i] = "EDITAR";
                    eliminar[i] = "ELIMINAR";
                    id_tabla_stock[i] = jsonObject.getString("stoc_id");

                    //agregamos datos al arraylist
                    lista_stock_periodo.add(s1);
                    lista_stock_becados.add(s2);
                }

                lista_stock_periodo_.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, lista_stock_periodo));
                lista_stock_becados_.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, lista_stock_becados));

                lista_editar.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, editar));
                lista_eliminar.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, eliminar));

                //convertimos en object array los arraylist(lista_stock_periodo,lista_stock_becados)
                array_stock_periodo = lista_stock_periodo.toArray();
                array_stock_becado = lista_stock_becados.toArray();

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
