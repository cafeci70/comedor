package com.ejemplo.comedor.fragmento_alumno;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.ejemplo.comedor.ApiResponseHelper;
import com.ejemplo.comedor.R;
import com.ejemplo.comedor.Url;
import com.ejemplo.comedor.Variables_globales;
import com.ejemplo.comedor.VolleySingleton;

import net.glxn.qrgen.android.QRCode;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

/**
 * Shared base fragment for displaying purchased meal tickets (lunch or dinner)
 * along with a QR code containing the ticket and student identifiers.
 */
public abstract class BaseReporteTicketFragment extends Fragment {

    private final Url ip = new Url();
    private TextView rNumeroTicket;
    private TextView rNombreAlumno;
    private TextView rMenu;
    private TextView rFecha;
    private ImageView imgQr;

    protected abstract int getLayoutResId();

    protected abstract String getReporteEndpointPath();

    protected abstract String getEntradaField();

    protected abstract String getPostreField();

    protected abstract String getPlatoPrincipalField();

    protected abstract String getBebidaField();

    protected abstract String getTipoComidaLabel();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(getLayoutResId(), container, false);

        rNumeroTicket = v.findViewById(R.id.r_numero_ticket);
        rNombreAlumno = v.findViewById(R.id.r_nombre_alumno);
        rFecha = v.findViewById(R.id.r_fecha_ticket);
        rMenu = v.findViewById(R.id.r_menu);
        imgQr = v.findViewById(R.id.img_qr);

        if (Variables_globales.hasAlumnoSession()) {
            cargarReporte(ip.getIp() + getReporteEndpointPath() + "?id_alumno="
                    + Uri.encode(Variables_globales.a_id));
        }

        return v;
    }

    private void cargarReporte(String url) {
        if (!isAdded() || getContext() == null) {
            return;
        }
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if (!isAdded() || getContext() == null) {
                    return;
                }
                if (response == null || response.length() == 0) {
                    Toast.makeText(getContext(), "No se encontró ticket para hoy", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    JSONObject jsonObject = response.getJSONObject(0);
                    String compId = jsonObject.getString("comp_id");
                    String compQr = jsonObject.optString("comp_qr", "");
                    String compFecha = jsonObject.getString("comp_fecha");
                    String nombreAlumno = jsonObject.getString("alum_nombres");

                    rNumeroTicket.setText(compId);
                    rNombreAlumno.setText(nombreAlumno);
                    rFecha.setText(compFecha);

                    String entrada = jsonObject.getString(getEntradaField());
                    String postre = jsonObject.getString(getPostreField());
                    String plato = jsonObject.getString(getPlatoPrincipalField());
                    String bebida = jsonObject.getString(getBebidaField());

                    rMenu.setText("Entrada: " + entrada + "\n"
                            + "Postre: " + postre + "\n"
                            + getTipoComidaLabel() + ": " + plato + "\n"
                            + "Bebida: " + bebida);

                    String qrPayload = "TICKET:" + compId
                            + "|ALUMNO:" + Variables_globales.a_id
                            + "|CODIGO:" + Variables_globales.a_codigo
                            + "|TIPO:" + getTipoComidaLabel().toUpperCase(Locale.ROOT)
                            + "|FECHA:" + compFecha;
                    if (!compQr.isEmpty() && !"####".equals(compQr)) {
                        qrPayload += "|TOKEN:" + compQr;
                    }

                    Bitmap bitmap = QRCode.from(qrPayload).withSize(500, 500).bitmap();
                    imgQr.setImageBitmap(bitmap);
                } catch (JSONException e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (!isAdded() || getContext() == null) {
                    return;
                }
                if (ApiResponseHelper.handleUnauthorized(BaseReporteTicketFragment.this, error)) {
                    return;
                }
                Toast.makeText(getContext(), "ERROR EN LA BUSQUEDA", Toast.LENGTH_SHORT).show();
            }
        });
        VolleySingleton.getInstance(getContext()).addToRequestQueue(jsonArrayRequest);
    }
}
