package com.ejemplo.comedor.fragmento_alumno;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.VideoView;
import android.widget.ViewFlipper;

import androidx.fragment.app.Fragment;

import com.ejemplo.comedor.R;

/**
 * Fragmento de inicio del alumno.
 *
 * El video es opcional: si se coloca un archivo en res/raw/introduccion.mp4,
 * se reproduce automáticamente. Si no existe, la pantalla funciona sin video.
 */
public class fragmento_alumno_inicio extends Fragment {

    ViewFlipper v_flipper;
    VideoView videoView;

    public fragmento_alumno_inicio() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_fragmento_alumno_inicio, container, false);
        v_flipper = v.findViewById(R.id.v_flipper);

        v_flipper.setInAnimation(getContext(), android.R.anim.slide_in_left);
        v_flipper.setOutAnimation(getContext(), android.R.anim.slide_out_right);
        v_flipper.setAutoStart(true);
        v_flipper.setFlipInterval(4000);
        v_flipper.startFlipping();

        videoView = v.findViewById(R.id.videoView);
        if (getContext() != null) {
            int videoId = getResources().getIdentifier("introduccion", "raw", getContext().getPackageName());
            if (videoId != 0) {
                String videoPath = "android.resource://" + getContext().getPackageName() + "/" + videoId;
                videoView.setVideoURI(Uri.parse(videoPath));

                MediaController mediaController = new MediaController(getContext());
                videoView.setMediaController(mediaController);
                mediaController.setAnchorView(videoView);
            } else {
                // No hay video incluido: se oculta para no dejar un recuadro vacío.
                videoView.setVisibility(View.GONE);
            }
        }

        return v;
    }
}
