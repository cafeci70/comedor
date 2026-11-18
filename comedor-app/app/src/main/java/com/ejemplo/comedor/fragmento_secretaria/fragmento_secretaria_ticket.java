package com.ejemplo.comedor.fragmento_secretaria;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.ejemplo.comedor.R;
import com.ejemplo.comedor.Url;
import com.ejemplo.comedor.VolleySingleton;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class fragmento_secretaria_ticket extends Fragment {

    EditText text_periodo, text_stock;
    Button btn_guardar;
    Url ip = new Url();

    public fragmento_secretaria_ticket() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_fragmento_secretaria_ticket, container, false);

        text_periodo = v.findViewById(R.id.text_perido_stock);
        text_stock = v.findViewById(R.id.text_stock);

        btn_guardar = v.findViewById(R.id.btn_guardar);

        btn_guardar.setOnClickListener(view -> {
            if (text_periodo.getText().toString().isEmpty() || text_stock.getText().toString().isEmpty()) {
                Toast.makeText(getContext(), "Hay campos vacios!!", Toast.LENGTH_SHORT).show();
            } else {
                Ejecutar_stock_becados(ip.getIp() + "/comedor-api/Secretaria/registros/stock_becados.php");
            }
        });

        return v;
    }

    private void Ejecutar_stock_becados(String url) {
        if (!isAdded() || getContext() == null) {
            return;
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, response -> {
            if (!isAdded() || getContext() == null) {
                return;
            }
            Toast.makeText(getContext(), "OPERACION EXITOSA", Toast.LENGTH_SHORT).show();
            //LIMPIAMOS LOS CAMPOS DESPUES DE RECARGAR
            text_periodo.setText("");
            text_stock.setText("");
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
                parametros.put("periodo", text_periodo.getText().toString());
                parametros.put("becados", text_stock.getText().toString());

                return parametros;
            }
        };
        VolleySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);
    }
}
