package com.utec.vmap.ui.Edificios;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.utec.vmap.Util;

public class Fundadores extends Fragment {

    private MSV gLView;

    private SLoader scene;

    private Handler handler;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler = new Handler(this.getActivity().getMainLooper());
        scene = new SLoader(this.getActivity(), Uri.parse("assets://assets/models/Fundadores.obj"));
        scene.init();
        try {
            gLView = new MSV(this.getActivity(), scene);
            scene.setGLView(gLView);
        } catch (Exception ex) {
            Log.println(Log.ERROR, "", ex.getMessage());
            return;
        }
        Util.setTitle("Los Fundadores");
        Util.setText("Nominado asi como un reconocimiento a los empresarios y academicos visionarios que emprendieron la fundacion de la UTEC como un suenyo hecho realidad, para la educacion superior en el pais:<br>" +
                "<b>Dependencias</b><br>" +
                "-Presidencia<br>" +
                "-Asistencia de Presidencia<br>" +
                "-Vicepresidencia<br>" +
                "-Asistencia Vicepresidencia<br>" +
                "-Salon Ignacio Ellacuria<br>" +
                "-Direccion adjunta de Informatica<br>" +
                "-Secretaria General<br>" +
                "-Asesoria Juridica<br>" +
                "-Vicerectoria de Desarrollo Educativo<br>" +
                "-Rectoria<br>" +
                "-Sala de sesiones<br>");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return gLView;
    }
}