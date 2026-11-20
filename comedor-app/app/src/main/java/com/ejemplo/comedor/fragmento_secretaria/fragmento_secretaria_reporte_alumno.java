package com.ejemplo.comedor.fragmento_secretaria;

import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.JsonArrayRequest;
import com.ejemplo.comedor.ApiResponseHelper;
import com.ejemplo.comedor.R;
import com.ejemplo.comedor.Url;
import com.ejemplo.comedor.Variables_globales;
import com.ejemplo.comedor.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 */
public class fragmento_secretaria_reporte_alumno extends Fragment {
    EditText buscar;
    ImageButton btn_buscar;
    Button btn_editar_saldo;
    TextView codigo, nombre, saldo, becado;
    Url ip = new Url();

    public fragmento_secretaria_reporte_alumno() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_fragmento_secretaria_reporte_alumno, container, false);
        buscar = v.findViewById(R.id.codigo_alumno);
        codigo = v.findViewById(R.id.text_codigo_alumno);
        nombre = v.findViewById(R.id.text_nombre_alumno);
        saldo = v.findViewById(R.id.text_saldo_alumno);
        becado = v.findViewById(R.id.text_becado_alumno);

        btn_buscar = v.findViewById(R.id.btn_buscar);
        btn_editar_saldo = v.findViewById(R.id.btn_editar_saldo);

        btn_buscar.setOnClickListener(view -> {
            String codigoBuscar = buscar.getText().toString().trim();
            if (codigoBuscar.isEmpty()) {
                Toast.makeText(getContext(), "Ingrese el código del alumno", Toast.LENGTH_SHORT).show();
                return;
            }
            Ejecutar_Reporte_alumno(ip.getIp() + "/comedor-api/Secretaria/reportes/reporte_alumno.php?alum_codigo=" + Uri.encode(codigoBuscar));
        });

        btn_editar_saldo.setOnClickListener(view -> {
            Variables_globales.alumn_codigo = codigo.getText().toString();
            Variables_globales.alumn_saldo = saldo.getText().toString();
            cargar_fragmento(new fragmento_secretaria_alumno_saldo_editar());
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

    public void Ejecutar_Reporte_alumno(String url) {
        if (!isAdded() || getContext() == null) {
            return;
        }

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, response -> {
            if (!isAdded() || getContext() == null) {
                return;
            }
            if (response == null || response.length() == 0) {
                btn_editar_saldo.setVisibility(View.INVISIBLE);
                Toast.makeText(getContext(), "ALUMNO NO ENCONTRADO", Toast.LENGTH_SHORT).show();
                return;
            }
            for (int i = 0; i < response.length(); i++) {
                try {
                    JSONObject jsonObject = response.getJSONObject(i);
                    nombre.setText(jsonObject.getString("alum_nombres"));
                    codigo.setText(jsonObject.getString("alum_codigo"));
                    saldo.setText(jsonObject.getString("alum_saldo"));
                    becado.setText(jsonObject.getString("alum_beca"));
                    buscar.setText("");

                } catch (JSONException e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            if ("no".equals(becado.getText().toString())) {
                btn_editar_saldo.setVisibility(View.VISIBLE);
            } else {
                btn_editar_saldo.setVisibility(View.INVISIBLE);
            }

        }, error -> {
            if (!isAdded() || getContext() == null) {
                return;
            }
            if (ApiResponseHelper.handleUnauthorized(this, error)) {
                return;
            }
            btn_editar_saldo.setVisibility(View.INVISIBLE);
            Toast.makeText(getContext(), "ERROR EN LA BUSQUEDA", Toast.LENGTH_SHORT).show();
        });
        VolleySingleton.getInstance(getContext()).addToRequestQueue(jsonArrayRequest);
    }
}
