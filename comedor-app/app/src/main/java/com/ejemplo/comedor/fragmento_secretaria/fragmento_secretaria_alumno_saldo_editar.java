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
import com.ejemplo.comedor.R;
import com.ejemplo.comedor.Url;
import com.ejemplo.comedor.Variables_globales;
import com.ejemplo.comedor.VolleySingleton;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class fragmento_secretaria_alumno_saldo_editar extends Fragment {

    EditText alumno_codigo, alumno_saldo;
    Button btn_recargar, btn_cancelar;

    Url ip = new Url();

    public fragmento_secretaria_alumno_saldo_editar() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_fragmento_secretaria_alumno_saldo_editar, container, false);

        alumno_codigo = v.findViewById(R.id.alum_codigo);
        alumno_saldo = v.findViewById(R.id.alum_saldo);

        btn_recargar = v.findViewById(R.id.btn_recargar);
        btn_cancelar = v.findViewById(R.id.btn_cancelar);

        alumno_codigo.setText(Variables_globales.alumn_codigo);
        alumno_saldo.setText(Variables_globales.alumn_saldo);

        btn_cancelar.setOnClickListener(view -> cargar_fragmento(new fragmento_secretaria_reporte_alumno()));

        btn_recargar.setOnClickListener(view -> {
            if (alumno_saldo.getText().toString().trim().isEmpty()) {
                Toast.makeText(getContext(), "Ingrese el saldo", Toast.LENGTH_SHORT).show();
                return;
            }
            ejecutar_saldo_editar(ip.getIp() + "/comedor-api/Secretaria/acciones/accion_alumno_saldo_editar.php");
        });

        return v;
    }

    private void ejecutar_saldo_editar(String url) {
        if (!isAdded() || getContext() == null) {
            return;
        }
        if (Variables_globales.alumn_codigo == null) {
            Toast.makeText(getContext(), "No se seleccionó ningún alumno", Toast.LENGTH_SHORT).show();
            return;
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, response -> {
            if (!isAdded() || getContext() == null) {
                return;
            }
            Toast.makeText(getContext(), "OPERACION EXITOSA", Toast.LENGTH_SHORT).show();
            cargar_fragmento(new fragmento_secretaria_reporte_alumno());

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
                parametros.put("alum_codigo", Variables_globales.alumn_codigo);
                parametros.put("saldo", alumno_saldo.getText().toString().trim());

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
