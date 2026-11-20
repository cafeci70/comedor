package com.ejemplo.comedor;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.android.volley.toolbox.JsonArrayRequest;
import com.ejemplo.comedor.fragmento_alumno.fragmento_alumno_comprar;
import com.ejemplo.comedor.fragmento_alumno.fragmento_alumno_comprar_cena;
import com.ejemplo.comedor.fragmento_alumno.fragmento_alumno_inicio;
import com.ejemplo.comedor.fragmento_alumno.fragmento_alumno_menu;
import com.ejemplo.comedor.fragmento_alumno.fragmento_alumno_perfil;
import com.ejemplo.comedor.fragmento_alumno.fragmento_alumno_reglamento;
import com.ejemplo.comedor.fragmento_general.fragmento_horario;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;
import org.json.JSONObject;

public class menu_alumno extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String POLL_SALDO_TAG = "POLL_SALDO";
    int contador = 0;
    String nuevo_saldo;
    Handler handler = new Handler(Looper.getMainLooper());
    Url ip = new Url();
    boolean isPolling = false;
    Runnable runnable = () -> {
        if (!isPolling || isFinishing() || !Variables_globales.hasAlumnoSession()) {
            return;
        }
        metodo_timer();
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!Variables_globales.hasAlumnoSession()) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        }
        setContentView(R.layout.activity_menu_alumno);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        if (headerView != null) {
            TextView tvCorreo = headerView.findViewById(R.id.text_correo_alumno);
            if (tvCorreo != null && Variables_globales.a_correo != null) {
                tvCorreo.setText(Variables_globales.a_correo);
            }
        }

        if (savedInstanceState == null) {
            cargar_fragmento_para_inicio(new fragmento_alumno_inicio());
            navigationView.getMenu().getItem(0).setChecked(true);
        }

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else if (contador == 0) {
                    Toast.makeText(menu_alumno.this, "Presione nuevamente para salir", Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onResume() {
        super.onResume();
        if (Variables_globales.hasAlumnoSession()) {
            startSaldoPolling();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopSaldoPolling();
    }

    @Override
    protected void onDestroy() {
        stopSaldoPolling();
        super.onDestroy();
    }

    private void startSaldoPolling() {
        if (isPolling) {
            return;
        }
        isPolling = true;
        handler.removeCallbacks(runnable);
        handler.post(runnable);
    }

    private void stopSaldoPolling() {
        isPolling = false;
        handler.removeCallbacks(runnable);
        VolleySingleton.getInstance(this).getRequestQueue().cancelAll(POLL_SALDO_TAG);
    }

    public void metodo_timer() {
        reporte_saldo(ip.getIp() + "/comedor-api/Alumno/reportes/reporte_saldo.php?id_alumno=" + Uri.encode(Variables_globales.a_id));
        if (isPolling) {
            handler.postDelayed(runnable, 5000);
        }
    }

    public void reporte_saldo(String url) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, response -> {
            if (isFinishing() || !Variables_globales.hasAlumnoSession()) {
                return;
            }
            for (int i = 0; i < response.length(); i++) {
                try {
                    JSONObject jsonObject = response.getJSONObject(i);
                    nuevo_saldo = jsonObject.getString("alum_saldo");
                    if (nuevo_saldo.trim().isEmpty()) {
                        continue;
                    }

                    int saldoNuevoInt = Integer.parseInt(nuevo_saldo.trim());
                    if (Variables_globales.a_saldo != null && !Variables_globales.a_saldo.trim().isEmpty()) {
                        int saldoActualInt = Integer.parseInt(Variables_globales.a_saldo.trim());
                        if (saldoNuevoInt > saldoActualInt) {
                            int saldo_recibido = saldoNuevoInt - saldoActualInt;
                            Toast.makeText(getApplicationContext(), "Saldo recibido de S./" + saldo_recibido, Toast.LENGTH_LONG).show();
                        }
                    }
                    Variables_globales.a_saldo = String.valueOf(saldoNuevoInt);

                } catch (JSONException | NumberFormatException e) {
                    // Ignore malformed saldo entries during background polling
                }
            }
        }, error -> {
            if (ApiResponseHelper.handleUnauthorized(this, error)) {
                stopSaldoPolling();
            }
        });
        jsonArrayRequest.setTag(POLL_SALDO_TAG);
        VolleySingleton.getInstance(this).addToRequestQueue(jsonArrayRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_alumno, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            stopSaldoPolling();
            SessionLogoutHelper.logout(this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            cargar_fragmento(new fragmento_alumno_inicio());
        } else if (id == R.id.nav_gallery) {
            cargar_fragmento(new fragmento_alumno_perfil());
        } else if (id == R.id.nav_slideshow) {
            cargar_fragmento(new fragmento_alumno_menu());
        } else if (id == R.id.nav_tools) {
            cargar_fragmento(new fragmento_alumno_comprar());
        } else if (id == R.id.nav_tools2) {
            cargar_fragmento(new fragmento_alumno_comprar_cena());
        } else if (id == R.id.nav_share) {
            cargar_fragmento(new fragmento_alumno_reglamento());
        } else if (id == R.id.nav_send) {
            cargar_fragmento(new fragmento_horario());
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void cargar_fragmento(Fragment fragmentento) {
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.contenedor_fragmento, fragmentento).commit();
    }

    public void cargar_fragmento_para_inicio(Fragment fragmentento) {
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.contenedor_fragmento, fragmentento).commit();
    }
}
