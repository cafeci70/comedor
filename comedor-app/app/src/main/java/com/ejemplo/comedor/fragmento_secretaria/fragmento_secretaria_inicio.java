package com.ejemplo.comedor.fragmento_secretaria;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.webkit.CookieManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
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
public class fragmento_secretaria_inicio extends Fragment {

    Url ip = new Url();
    WebView reporte;

    public fragmento_secretaria_inicio() {
        // Required empty public constructor
    }


    @Override
    @SuppressLint("SetJavaScriptEnabled")
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_fragmento_secretaria_inicio, container, false);

        reporte = v.findViewById(R.id.reportes);

        reporte.getSettings().setJavaScriptEnabled(true);
        reporte.setWebViewClient(new WebViewClient());

        if (Variables_globales.s_reportes_cookie == null
                || Variables_globales.s_reportes_cookie.isEmpty()) {
            abrirSesionReportes();
        } else {
            cargarReporte();
        }

        return v;
    }

    /**
     * Abre la sesión PHP de los reportes con las credenciales ya usadas en el login,
     * para que el WebView no muestre el formulario de acceso.
     */
    private void abrirSesionReportes() {
        if (getContext() == null || !Variables_globales.hasSecretariaSession()
                || Variables_globales.s_codigo == null || Variables_globales.s_clave == null) {
            cargarReporte();
            return;
        }

        String url = ip.getIp() + "/comedor-api/reportes/session.php";
        final String codigo = Variables_globales.s_codigo;
        final String clave = Variables_globales.s_clave;

        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    try {
                        JSONObject json = new JSONObject(response);
                        if (json.optBoolean("ok", false)) {
                            String nombre = json.getString("cookie_name");
                            String valor = json.getString("cookie_value");
                            String path = json.optString("cookie_path", "/");
                            Variables_globales.s_reportes_cookie =
                                    nombre + "=" + valor + "; Path=" + path;
                        }
                    } catch (JSONException ignored) {
                        // Sin cookie: el reporte mostrará su formulario de acceso.
                    }

                    Variables_globales.s_clave = null;
                    if (isAdded()) {
                        cargarReporte();
                    }
                },
                error -> {
                    Variables_globales.s_clave = null;
                    if (isAdded()) {
                        cargarReporte();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("codigo", codigo);
                params.put("clave", clave);
                return params;
            }
        };

        VolleySingleton.getInstance(getContext()).addToRequestQueue(request);
    }

    private void cargarReporte() {
        String cookie = Variables_globales.s_reportes_cookie;
        if (cookie != null && !cookie.isEmpty()) {
            CookieManager.getInstance().setAcceptCookie(true);
            CookieManager.getInstance().setCookie(ip.getIp(), cookie);
        }
        reporte.loadUrl(ip.getIp() + "/comedor-api/reportes/reporte_almuerzo_cena.php");
    }
}
