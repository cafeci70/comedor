package com.ejemplo.comedor.fragmento_alumno;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.ejemplo.comedor.R;
import com.ejemplo.comedor.Variables_globales;

/**
 * A simple {@link Fragment} subclass.
 */
public class fragmento_alumno_perfil extends Fragment {

    TextView a_nombre, a_apellido_p, a_apellido_m, a_correo, a_celular, a_saldo, a_beca;


    public fragmento_alumno_perfil() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_fragmento_alumno_perfil, container, false);

        a_nombre = v.findViewById(R.id.a_nombre);
        a_apellido_p = v.findViewById(R.id.a_apellido_p);
        a_apellido_m = v.findViewById(R.id.a_apellido_m);
        a_correo = v.findViewById(R.id.a_correo);
        a_celular = v.findViewById(R.id.a_celular);
        a_saldo = v.findViewById(R.id.a_saldo);
        a_beca = v.findViewById(R.id.a_beca);

        if (Variables_globales.a_nombre != null) {
            a_nombre.setText(Variables_globales.a_nombre);
        }
        if (Variables_globales.a_apellido_p != null) {
            a_apellido_p.setText(Variables_globales.a_apellido_p);
        }
        if (Variables_globales.a_apellido_m != null) {
            a_apellido_m.setText(Variables_globales.a_apellido_m);
        }
        if (Variables_globales.a_correo != null) {
            a_correo.setText(Variables_globales.a_correo);
        }
        if (Variables_globales.a_celular != null) {
            a_celular.setText(Variables_globales.a_celular);
        }
        if (Variables_globales.a_saldo != null) {
            a_saldo.setText(Variables_globales.a_saldo);
        }
        if (Variables_globales.a_beca != null) {
            a_beca.setText(Variables_globales.a_beca);
        }

        return v;
    }

}
