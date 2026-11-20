package com.ejemplo.comedor;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
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

import com.ejemplo.comedor.fragmento_general.fragmento_horario;
import com.ejemplo.comedor.fragmento_secretaria.fragmento_secretaria_ayuda;
import com.ejemplo.comedor.fragmento_secretaria.fragmento_secretaria_horario;
import com.ejemplo.comedor.fragmento_secretaria.fragmento_secretaria_inicio;
import com.ejemplo.comedor.fragmento_secretaria.fragmento_secretaria_perfil;
import com.ejemplo.comedor.fragmento_secretaria.fragmento_secretaria_reporte_alumno;
import com.ejemplo.comedor.fragmento_secretaria.fragmento_secretaria_reporte_horario;
import com.ejemplo.comedor.fragmento_secretaria.fragmento_secretaria_reporte_stock;
import com.ejemplo.comedor.fragmento_secretaria.fragmento_secretaria_saldo;
import com.ejemplo.comedor.fragmento_secretaria.fragmento_secretaria_ticket;
import com.google.android.material.navigation.NavigationView;

public class menu_secretaria extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    int contador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!Variables_globales.hasSecretariaSession()) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        }
        setContentView(R.layout.activity_menu_secretaria);
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
            TextView tvCorreo = headerView.findViewById(R.id.text_correo_secretaria);
            if (tvCorreo != null && Variables_globales.s_correo != null) {
                tvCorreo.setText(Variables_globales.s_correo);
            }
        }

        if (savedInstanceState == null) {
            cargar_fragmento_para_inicio(new fragmento_secretaria_inicio());
            navigationView.getMenu().getItem(0).setChecked(true);
        }

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else if (contador == 0) {
                    Toast.makeText(menu_secretaria.this, "Presione nuevamente para salir", Toast.LENGTH_SHORT).show();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_secretaria, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            SessionLogoutHelper.logout(this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            cargar_fragmento(new fragmento_secretaria_inicio());
        } else if (id == R.id.nav_gallery) {
            cargar_fragmento(new fragmento_secretaria_perfil());
        } else if (id == R.id.nav_slideshow) {
            cargar_fragmento(new fragmento_secretaria_saldo());
        } else if (id == R.id.nav_tools) {
            cargar_fragmento(new fragmento_secretaria_horario());
        } else if (id == R.id.nav_ticket) {
            cargar_fragmento(new fragmento_secretaria_ticket());
        } else if (id == R.id.report_alumno) {
            cargar_fragmento(new fragmento_secretaria_reporte_alumno());
        } else if (id == R.id.report_stock) {
            cargar_fragmento(new fragmento_secretaria_reporte_stock());
        } else if (id == R.id.report_horario) {
            cargar_fragmento(new fragmento_secretaria_reporte_horario());
        } else if (id == R.id.nav_share) {
            cargar_fragmento(new fragmento_secretaria_ayuda());
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
