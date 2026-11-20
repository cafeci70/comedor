package com.ejemplo.comedor.fragmento_secretaria;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.ejemplo.comedor.ApiResponseHelper;
import com.ejemplo.comedor.R;
import com.ejemplo.comedor.Url;
import com.ejemplo.comedor.Variables_globales;
import com.ejemplo.comedor.VolleySingleton;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class fragmento_secretaria_stock_editar extends Fragment {

    EditText text_periodo, text_becados;
    Button btn_subir, btn_cancelar;

    Url ip = new Url();

    public fragmento_secretaria_stock_editar() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_fragmento_secretaria_stock_editar, container, false);

        text_periodo = v.findViewById(R.id.text_perido_stock);
        text_becados = v.findViewById(R.id.text_stock);

        btn_subir = v.findViewById(R.id.btn_guardar);
        btn_cancelar = v.findViewById(R.id.btn_cancelar);

        //enviamos los datos al layout
        text_periodo.setText(Variables_globales.periodo);
        text_becados.setText(Variables_globales.becados);

        btn_subir.setOnClickListener(view -> {
            if (text_periodo.getText().toString().isEmpty() || text_becados.getText().toString().isEmpty()) {
                Toast.makeText(getContext(), "Hay campos vacios!!", Toast.LENGTH_SHORT).show();
            } else {
                ejecutar_stock_editar(ip.getIp() + "/comedor-api/Secretaria/acciones/accion_stock_editar.php");
            }
        });

        btn_cancelar.setOnClickListener(view -> cargar_fragmento(new fragmento_secretaria_reporte_stock()));

        return v;
    }

    private void ejecutar_stock_editar(String url) {
        if (!isAdded() || getContext() == null) {
            return;
        }
        if (Variables_globales.id_tabla_stock == null) {
            Toast.makeText(getContext(), "No se seleccionó ningún registro", Toast.LENGTH_SHORT).show();
            return;
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, response -> {
            if (!isAdded() || getContext() == null) {
                return;
            }
            String msg = ApiResponseHelper.extractMessage(response, "OPERACION EXITOSA");
            Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
            if (ApiResponseHelper.isSuccessResponse(response)) {
                cargar_fragmento(new fragmento_secretaria_reporte_stock());
            }

        }, error -> {
            if (!isAdded() || getContext() == null) {
                return;
            }
            if (ApiResponseHelper.handleUnauthorized(this, error)) {
                return;
            }
            Toast.makeText(getContext(), ApiResponseHelper.extractErrorMessage(error, "No se pudo actualizar el stock"), Toast.LENGTH_SHORT).show();
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> parametros = new HashMap<>();
                //PARAMETROS PARA ENVIAR POR EL METODO POST
                parametros.put("id_stock", Variables_globales.id_tabla_stock);
                parametros.put("periodo", text_periodo.getText().toString());
                parametros.put("becados", text_becados.getText().toString());
                return parametros;
            }
        };
        VolleySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);
    }

    public void cargar_fragmento(Fragment fragmentento) {
        if (!isAdded() || getActivity() == null) {
            return;
        }
        FragmentManager manager = getActivity().getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.contenedor_fragmento, fragmentento).commit();
    }
}
