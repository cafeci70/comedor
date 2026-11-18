package com.ejemplo.comedor.fragmento_general;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ejemplo.comedor.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class fragmento_horario extends Fragment {


    public fragmento_horario() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fragmento_horario, container, false);
    }

}
