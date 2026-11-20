package com.ejemplo.comedor.fragmento_secretaria;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
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
public class fragmento_secretaria_horario_editar extends Fragment {

    EditText desayuno, almuerzo_en, almuerzo_post, almuerzo, almuerzo_be, cena_en, cena_post, cena, cena_be;
    Button btn_registrar, btn_cancelar;
    CalendarView calendarView;
    String fecha;
    Url ip = new Url();

    public fragmento_secretaria_horario_editar() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_fragmento_secretaria_horario_editar, container, false);

        btn_registrar = v.findViewById(R.id.btn_registrar);
        btn_cancelar = v.findViewById(R.id.btn_cancelar);

        calendarView = v.findViewById(R.id.calendario);

        desayuno = v.findViewById(R.id.text_desayuno);
        almuerzo_en = v.findViewById(R.id.text_al_entrada);
        almuerzo_post = v.findViewById(R.id.text_al_postre);
        almuerzo = v.findViewById(R.id.text_al);
        almuerzo_be = v.findViewById(R.id.text_al_bebida);
        cena_en = v.findViewById(R.id.text_ce_entrada);
        cena_post = v.findViewById(R.id.text_ce_postre);
        cena = v.findViewById(R.id.text_ce);
        cena_be = v.findViewById(R.id.text_ce_bebida);

        fecha = Variables_globales.menu_fecha;
        desayuno.setText(Variables_globales.menu_desayuno);
        almuerzo_en.setText(Variables_globales.menu_alm_entrada);
        almuerzo_post.setText(Variables_globales.menu_alm_postre);
        almuerzo.setText(Variables_globales.menu_almmuerzo);
        almuerzo_be.setText(Variables_globales.menu_alm_bebida);
        cena_en.setText(Variables_globales.menu_cen_entrada);
        cena_post.setText(Variables_globales.menu_cen_postre);
        cena.setText(Variables_globales.menu_cena);
        cena_be.setText(Variables_globales.menu_cen_bebida);

        btn_registrar.setOnClickListener(view -> {
            if (desayuno.getText().toString().isEmpty() || almuerzo_en.getText().toString().isEmpty() || almuerzo_post.getText().toString().isEmpty()
                    || almuerzo.getText().toString().isEmpty() || almuerzo_be.getText().toString().isEmpty() || cena_en.getText().toString().isEmpty()
                    || cena_post.getText().toString().isEmpty() || cena.getText().toString().isEmpty() || cena_be.getText().toString().isEmpty()) {

                Toast.makeText(getContext(), "Hay campos vacios!!", Toast.LENGTH_SHORT).show();
            } else {
                ejecutar_horario_editar(ip.getIp() + "/comedor-api/Secretaria/acciones/accion_menu_editar.php");
            }
        });

        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) ->
                fecha = Variables_globales.formatIsoDate(year, month, dayOfMonth));

        btn_cancelar.setOnClickListener(view -> cargar_fragmento(new fragmento_secretaria_reporte_horario()));

        return v;
    }

    private void ejecutar_horario_editar(String url) {
        if (!isAdded() || getContext() == null) {
            return;
        }
        if (Variables_globales.id_tabla_menu == null) {
            Toast.makeText(getContext(), "No se seleccionó ningún menú", Toast.LENGTH_SHORT).show();
            return;
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, response -> {
            if (!isAdded() || getContext() == null) {
                return;
            }
            String msg = ApiResponseHelper.extractMessage(response, "OPERACION EXITOSA");
            Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
            if (ApiResponseHelper.isSuccessResponse(response)) {
                cargar_fragmento(new fragmento_secretaria_reporte_horario());
            }

        }, error -> {
            if (!isAdded() || getContext() == null) {
                return;
            }
            Toast.makeText(getContext(), ApiResponseHelper.extractErrorMessage(error, "No se pudo actualizar el menú"), Toast.LENGTH_SHORT).show();
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> parametros = new HashMap<>();
                //PARAMETROS PARA ENVIAR POR EL METODO POST
                parametros.put("id_menu", Variables_globales.id_tabla_menu);
                parametros.put("fecha", fecha != null ? fecha : "");
                parametros.put("desayuno", desayuno.getText().toString());
                parametros.put("almuerzo_entrada", almuerzo_en.getText().toString());
                parametros.put("almuerzo_postre", almuerzo_post.getText().toString());
                parametros.put("almuerzo", almuerzo.getText().toString());
                parametros.put("almuerzo_bebida", almuerzo_be.getText().toString());
                parametros.put("cena_entrada", cena_en.getText().toString());
                parametros.put("cena_postre", cena_post.getText().toString());
                parametros.put("cena", cena.getText().toString());
                parametros.put("cena_bebida", cena_be.getText().toString());

                return parametros;
            }
        };
        VolleySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);
    }

    @SuppressWarnings({"DiscouragedApi", "SourceLockedOrientationActivity"})
    public void cargar_fragmento(Fragment fragmentento) {
        if (!isAdded() || getActivity() == null) {
            return;
        }
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        FragmentManager manager = getActivity().getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.contenedor_fragmento, fragmentento).commit();
    }
}
