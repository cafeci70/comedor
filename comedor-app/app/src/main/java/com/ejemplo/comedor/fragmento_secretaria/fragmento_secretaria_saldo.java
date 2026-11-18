package com.ejemplo.comedor.fragmento_secretaria;


import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.ejemplo.comedor.R;
import com.ejemplo.comedor.Url;
import com.ejemplo.comedor.Variables_globales;
import com.ejemplo.comedor.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class fragmento_secretaria_saldo extends Fragment {
    EditText edi_saldo,edi_alum_codigo;
    TextView nombre_alumno;
    Button btn_recargar;
    Url ip = new Url();

    public fragmento_secretaria_saldo() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v= inflater.inflate(R.layout.fragment_fragmento_secretaria_saldo, container, false);
        edi_alum_codigo = v.findViewById(R.id.alum_codigo);
        edi_saldo = v.findViewById(R.id.alum_saldo);
        btn_recargar=v.findViewById(R.id.btn_recargar);

        nombre_alumno=v.findViewById(R.id.nombre_alumno);

        btn_recargar.setOnClickListener(view -> {
            String codigoTexto = edi_alum_codigo.getText().toString().trim();
            String saldoTexto = edi_saldo.getText().toString().trim();
            if (saldoTexto.isEmpty() || codigoTexto.isEmpty()) {
                Toast.makeText(getContext(), "Hay campos vacios!!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (Variables_globales.parseIntOrDefault(saldoTexto, 0) <= 0) {
                Toast.makeText(getContext(), "El monto debe ser mayor a 0", Toast.LENGTH_SHORT).show();
                return;
            }
            comprobar_si_es_becado(ip.getIp() + "/comedor-api/Secretaria/acciones/comprobar_si_es_becado.php?alum_codigo=" + Uri.encode(codigoTexto));
        });


        edi_alum_codigo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s != null && s.length() == 10) {
                    mostrar_nombre_alumno(ip.getIp() + "/comedor-api/Secretaria/reportes/reporte_nombre_alumno.php?alum_codigo="
                            + Uri.encode(s.toString()));
                } else {
                    nombre_alumno.setText("--");
                }
            }
        });


        return  v;
    }

    private  void mostrar_nombre_alumno(String url){
        if (!isAdded() || getContext() == null) {
            return;
        }

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, response -> {
            if (!isAdded() || getContext() == null) {
                return;
            }
            if (response == null || response.length() == 0) {
                nombre_alumno.setText(R.string.no_existe);
                return;
            }
            for (int i = 0; i < response.length(); i++) {
                try {
                    JSONObject jsonObject = response.getJSONObject(i);
                    String nombre_alumno_ = (jsonObject.getString("alum_nombres"));

                    nombre_alumno.setText(nombre_alumno_);

                } catch (JSONException e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, error -> {
            if (!isAdded() || getContext() == null) {
                return;
            }
            Toast.makeText(getContext(), R.string.no_existe, Toast.LENGTH_SHORT).show();
        });
        VolleySingleton.getInstance(getContext()).addToRequestQueue(jsonArrayRequest);


    }

    private  void comprobar_si_es_becado(String url){
        if (!isAdded() || getContext() == null) {
            return;
        }

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, response -> {
            if (!isAdded() || getContext() == null) {
                return;
            }
            if (response == null || response.length() == 0) {
                Toast.makeText(getContext(), "ALUMNO NO ENCONTRADO", Toast.LENGTH_SHORT).show();
                return;
            }
            for (int i = 0; i < response.length(); i++) {
                try {
                    JSONObject jsonObject = response.getJSONObject(i);
                    String becado = (jsonObject.getString("alum_beca"));

                    if ("si".equals(becado)) {
                        Toast.makeText(getContext(), "EL ALUMNO ES BECADO", Toast.LENGTH_SHORT).show();
                    } else {
                        ejecutarRecargar(ip.getIp() + "/comedor-api/Secretaria/registros/generar_recarga.php");
                    }

                } catch (JSONException e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, error -> {
            if (!isAdded() || getContext() == null) {
                return;
            }
            Toast.makeText(getContext(), "ALUMNO NO ENCONTRADO", Toast.LENGTH_SHORT).show();
        });
        VolleySingleton.getInstance(getContext()).addToRequestQueue(jsonArrayRequest);


    }

    private void ejecutarRecargar(String url){
        if (!isAdded() || getContext() == null) {
            return;
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, response -> {
            if (!isAdded() || getContext() == null) {
                return;
            }
            Toast.makeText(getContext(), "OPERACION EXITOSA" + response, Toast.LENGTH_SHORT).show();
            //LIMPIAMOS LOS CAMPOS DESPUES DE RECARGAR
            edi_alum_codigo.setText("");
            edi_saldo.setText("");
            nombre_alumno.setText("--");
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
                parametros.put("alum_codigo", edi_alum_codigo.getText().toString().trim());
                parametros.put("saldo", edi_saldo.getText().toString().trim());
                return parametros;
            }
        };
        VolleySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);

    }

}
