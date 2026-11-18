package com.ejemplo.comedor.fragmento_secretaria;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ejemplo.comedor.R;
import com.ejemplo.comedor.Variables_globales;

/**
 * A simple {@link Fragment} subclass.
 */
public class fragmento_secretaria_perfil extends Fragment {

    TextView s_nombre,s_apellido,s_direccion,s_celular,s_correo;

    public fragmento_secretaria_perfil() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =inflater.inflate(R.layout.fragment_fragmento_secretaria_perfil, container, false);

        s_nombre= v.findViewById(R.id.s_nombre);
        s_apellido= v.findViewById(R.id.s_apellido);
        s_direccion= v.findViewById(R.id.s_direccion);
        s_celular= v.findViewById(R.id.s_celular);
        s_correo= v.findViewById(R.id.s_correo);


        if (Variables_globales.s_nombre != null) {
            s_nombre.setText(Variables_globales.s_nombre);
        }
        if (Variables_globales.s_apellido != null) {
            s_apellido.setText(Variables_globales.s_apellido);
        }
        if (Variables_globales.s_direccion != null) {
            s_direccion.setText(Variables_globales.s_direccion);
        }
        if (Variables_globales.s_celular != null) {
            s_celular.setText(Variables_globales.s_celular);
        }
        if (Variables_globales.s_correo != null) {
            s_correo.setText(Variables_globales.s_correo);
        }



        return v;
    }

}
