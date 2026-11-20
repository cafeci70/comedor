package com.ejemplo.comedor.fragmento_alumno;

import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.ejemplo.comedor.ApiResponseHelper;
import com.ejemplo.comedor.R;
import com.ejemplo.comedor.Url;
import com.ejemplo.comedor.Variables_globales;
import com.ejemplo.comedor.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Shared base fragment for purchasing daily meal tickets (lunch or dinner),
 * handling menu availability, stock verification, balance/scholarship checks,
 * and post-purchase redirection.
 */
public abstract class BaseCompraTicketFragment extends Fragment {

    private Button btnComprar;
    private TextView tvStock;
    private final Url ip = new Url();
    private int stockDisponible = 0;

    protected abstract int getLayoutResId();

    protected abstract int getBuyButtonId();

    protected abstract int getStockTextViewId();

    protected abstract String getCompraCheckField();

    protected abstract String getStockBecadoField();

    protected abstract String getStockRegularField();

    protected abstract String getRegistrarCompraPath();

    protected abstract Fragment createReportFragment();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(getLayoutResId(), container, false);
        btnComprar = v.findViewById(getBuyButtonId());
        tvStock = v.findViewById(getStockTextViewId());

        // Keep button disabled until menu and stock availability are confirmed.
        btnComprar.setEnabled(false);

        if (!Variables_globales.hasAlumnoSession()) {
            btnComprar.setText(R.string.sesion_invalida);
            return v;
        }

        comprobarCompra(false);
        comprobarSiHayMenu();

        btnComprar.setOnClickListener(view -> ejecutarCompra());

        return v;
    }

    private void setNoVentaState() {
        stockDisponible = 0;
        if (tvStock != null) {
            tvStock.setText("0");
        }
        if (btnComprar != null) {
            btnComprar.setEnabled(false);
            btnComprar.setText(R.string.no_hay_venta);
        }
    }

    private void reporteStock() {
        if (!isAdded() || getContext() == null) {
            return;
        }
        String url = ip.getIp() + "/comedor-api/Alumno/reportes/reporte_stock.php";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, response -> {
            if (!isAdded() || getContext() == null) {
                return;
            }
            if (response == null || response.length() == 0) {
                setNoVentaState();
                return;
            }
            try {
                JSONObject jsonObject = response.getJSONObject(0);
                String stockKey = "si".equals(Variables_globales.a_beca)
                        ? getStockBecadoField()
                        : getStockRegularField();
                String stockText = jsonObject.getString(stockKey);
                stockDisponible = Variables_globales.parseIntOrDefault(stockText, 0);
                if (tvStock != null) {
                    tvStock.setText(String.valueOf(stockDisponible));
                }
                if (stockDisponible <= 0) {
                    btnComprar.setEnabled(false);
                    btnComprar.setText(R.string.sin_stock);
                } else {
                    btnComprar.setEnabled(true);
                    btnComprar.setText(R.string.btn_comprar);
                }
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
            setNoVentaState();
        });
        VolleySingleton.getInstance(getContext()).addToRequestQueue(jsonArrayRequest);
    }

    private void comprobarSiHayMenu() {
        if (!isAdded() || getContext() == null) {
            return;
        }
        String url = ip.getIp() + "/comedor-api/Alumno/acciones/comprobar_si_hay_menu.php";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, response -> {
            if (!isAdded() || getContext() == null) {
                return;
            }
            if (response == null || response.length() == 0) {
                setNoVentaState();
                return;
            }
            reporteStock();
        }, error -> {
            if (!isAdded() || getContext() == null) {
                return;
            }
            if (ApiResponseHelper.handleUnauthorized(this, error)) {
                return;
            }
            setNoVentaState();
        });
        VolleySingleton.getInstance(getContext()).addToRequestQueue(jsonArrayRequest);
    }

    private void comprobarCompra(final boolean afterPurchase) {
        if (!isAdded() || getContext() == null || !Variables_globales.hasAlumnoSession()) {
            return;
        }
        String url = ip.getIp() + "/comedor-api/Alumno/acciones/comprobar_si_compro.php?alum_id="
                + Uri.encode(Variables_globales.a_id);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, response -> {
            if (!isAdded() || getContext() == null) {
                return;
            }
            boolean comprado = false;
            if (response != null) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        String estado = jsonObject.getString(getCompraCheckField());
                        if ("si".equals(estado)) {
                            comprado = true;
                            break;
                        }
                    } catch (JSONException e) {
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            if (comprado) {
                if (afterPurchase) {
                    Toast.makeText(getContext(), "OPERACION EXITOSA", Toast.LENGTH_SHORT).show();
                    refrescarSaldo();
                }
                cargarFragmento(createReportFragment());
            } else if (afterPurchase) {
                Toast.makeText(getContext(), "No se pudo registrar la compra", Toast.LENGTH_SHORT).show();
                reporteStock();
            }
        }, error -> {
            if (!isAdded() || getContext() == null) {
                return;
            }
            if (ApiResponseHelper.handleUnauthorized(this, error)) {
                return;
            }
            if (afterPurchase) {
                Toast.makeText(getContext(), "No se pudo verificar la compra", Toast.LENGTH_SHORT).show();
                reporteStock();
            }
        });
        VolleySingleton.getInstance(getContext()).addToRequestQueue(jsonArrayRequest);
    }

    private void ejecutarCompra() {
        if (!isAdded() || getContext() == null || !Variables_globales.hasAlumnoSession()) {
            return;
        }
        if (stockDisponible <= 0) {
            Toast.makeText(getContext(), "NO HAY STOCK DISPONIBLE", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean esBecado = "si".equals(Variables_globales.a_beca);
        int saldoActual = Variables_globales.parseIntOrDefault(Variables_globales.a_saldo, 0);
        if (saldoActual <= 0 && !esBecado) {
            Toast.makeText(getContext(), "USTED NO TIENE SALDO :(", Toast.LENGTH_SHORT).show();
            return;
        }

        btnComprar.setEnabled(false);
        String url = ip.getIp() + getRegistrarCompraPath();
        StringRequest stringRequest = crearCompraRequest(url);
        VolleySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);
    }

    private StringRequest crearCompraRequest(String url) {
        return new StringRequest(Request.Method.POST, url, response -> {
            if (!isAdded() || getContext() == null) {
                return;
            }
            if (!ApiResponseHelper.isSuccessResponse(response)) {
                String msg = ApiResponseHelper.extractMessage(response, "No se pudo registrar la compra");
                Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                reporteStock();
                return;
            }
            comprobarCompra(true);
        }, error -> {
            if (!isAdded() || getContext() == null) {
                return;
            }
            if (ApiResponseHelper.handleUnauthorized(this, error)) {
                return;
            }
            String msg = ApiResponseHelper.extractErrorMessage(error, "No se pudo procesar la compra");
            Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
            reporteStock();
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> parametros = new HashMap<>();
                parametros.put("alum_codigo", Variables_globales.a_codigo);
                return parametros;
            }
        };
    }

    private void refrescarSaldo() {
        if (!isAdded() || getContext() == null || !Variables_globales.hasAlumnoSession()) {
            return;
        }
        String url = ip.getIp() + "/comedor-api/Alumno/reportes/reporte_saldo.php?id_alumno="
                + Uri.encode(Variables_globales.a_id);
        JsonArrayRequest request = new JsonArrayRequest(url, response -> {
            if (response != null && response.length() > 0) {
                try {
                    Variables_globales.a_saldo = response.getJSONObject(0).getString("alum_saldo");
                } catch (JSONException ignored) {
                }
            }
        }, error -> {
        });
        VolleySingleton.getInstance(getContext()).addToRequestQueue(request);
    }

    protected void cargarFragmento(Fragment fragment) {
        if (!isAdded() || getActivity() == null) {
            return;
        }
        FragmentManager manager = getActivity().getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.contenedor_fragmento, fragment).commit();
    }
}

