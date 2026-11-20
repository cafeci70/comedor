package com.ejemplo.comedor;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    int contador = 0;
    EditText codigo, clave;

    Button btn;
    Url ip = new Url();

    String codi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        codigo = findViewById(R.id.text_codigo);
        clave = findViewById(R.id.text_clave);
        btn = findViewById(R.id.btn_ingresar);


        btn.setOnClickListener(v -> {
            codi = codigo.getText().toString().trim();
            String clav = clave.getText().toString().trim();

            if (codi.isEmpty() || clav.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Ingrese código y clave", Toast.LENGTH_SHORT).show();
                return;
            }

            Variables_globales.clearSession();
            VolleySingleton.getInstance(this).clearCookies();
            btn.setEnabled(false);

            if (codi.startsWith("000")) {
                ejecutarLogin(
                        ip.getIp() + "/comedor-api/iniciar_sesion_alumno.php",
                        codi,
                        clav,
                        true);
            } else {
                ejecutarLogin(
                        ip.getIp() + "/comedor-api/iniciar_sesion_secretaria.php",
                        codi,
                        clav,
                        false);
            }
        });

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (contador == 0) {
                    Toast.makeText(MainActivity.this, "Presione nuevamente para salir", Toast.LENGTH_SHORT).show();
                    contador++;
                    new CountDownTimer(3000, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                        }

                        @Override
                        public void onFinish() {
                            contador = 0;
                        }
                    }.start();
                } else {
                    setEnabled(false);
                    getOnBackPressedDispatcher().onBackPressed();
                }
            }
        });

    }

    private void ejecutarLogin(String url, String codigoUsuario, String claveUsuario, boolean esAlumno) {
        StringRequest request = new StringRequest(Request.Method.POST, url, rawResponse -> {
            btn.setEnabled(true);
            try {
                JSONArray response = new JSONArray(rawResponse);
                if (response.length() == 0) {
                    Toast.makeText(getApplicationContext(), "Datos inválidos", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (esAlumno) {
                    manejarRespuestaAlumno(response);
                } else {
                    manejarRespuestaSecretaria(response, claveUsuario);
                }
            } catch (JSONException e) {
                Variables_globales.clearSession();
                Toast.makeText(getApplicationContext(), "Error al leer los datos de sesión", Toast.LENGTH_SHORT).show();
            }
        }, error -> {
            btn.setEnabled(true);
            String msg = ApiResponseHelper.extractErrorMessage(error, "Datos inválidos o error de conexión");
            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("codigo", codigoUsuario);
                params.put("clave", claveUsuario);
                return params;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    private void manejarRespuestaSecretaria(JSONArray response, String claveUsuario) throws JSONException {
        for (int i = 0; i < response.length(); i++) {
            JSONObject jsonObject = response.getJSONObject(i);
            Variables_globales.s_nombre = jsonObject.getString("usu_nombre");
            Variables_globales.s_apellido = jsonObject.getString("usu_apellido");
            Variables_globales.s_direccion = jsonObject.getString("usu_direccion");
            Variables_globales.s_celular = jsonObject.getString("usu_celular");
            Variables_globales.s_correo = jsonObject.getString("usu_correo");
        }

        if (!Variables_globales.hasSecretariaSession()) {
            Toast.makeText(getApplicationContext(), "Datos inválidos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Credenciales temporales para abrir la sesión de los reportes web (WebView).
        Variables_globales.s_codigo = codi;
        Variables_globales.s_clave = claveUsuario;

        Intent intent = new Intent(getApplicationContext(), menu_secretaria.class);
        startActivity(intent);
        finish();
    }

    private void manejarRespuestaAlumno(JSONArray response) throws JSONException {
        for (int i = 0; i < response.length(); i++) {
            JSONObject jsonObject = response.getJSONObject(i);
            Variables_globales.a_id = jsonObject.getString("alum_id");
            Variables_globales.a_nombre = jsonObject.getString("alum_nombres");
            Variables_globales.a_apellido_p = jsonObject.getString("alum_apellido_pa");
            Variables_globales.a_apellido_m = jsonObject.getString("alum_apellido_ma");
            Variables_globales.a_correo = jsonObject.getString("alum_correo");
            Variables_globales.a_celular = jsonObject.getString("alum_celular");
            Variables_globales.a_saldo = jsonObject.getString("alum_saldo");
            Variables_globales.a_beca = jsonObject.getString("alum_beca");
            Variables_globales.a_codigo = jsonObject.getString("alum_codigo");
        }

        if (!Variables_globales.hasAlumnoSession()) {
            Toast.makeText(getApplicationContext(), "Datos inválidos", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(getApplicationContext(), menu_alumno.class);
        startActivity(intent);
        finish();
    }

}
