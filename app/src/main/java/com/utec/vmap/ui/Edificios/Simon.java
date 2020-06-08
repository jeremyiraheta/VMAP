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

public class Simon extends Fragment {

    private MSV gLView;

    private SLoader scene;

    private Handler handler;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler = new Handler(this.getActivity().getMainLooper());
        scene = new SLoader(this.getActivity(), Uri.parse("assets://assets/models/Bolivar.obj"));
        scene.init();
        try {
            gLView = new MSV(this.getActivity(), scene);
            scene.setGLView(gLView);
        } catch (Exception ex) {
            Log.println(Log.ERROR, "", ex.getMessage());
            return;
        }
        Util.setTitle("Simon Bolivar");
        Util.setText("La universidad designo como Simon Bolivar a su primer edificio en memoria del paladin latinoamericano, libertador de pueblos oprimidos en la epoca colonial tardia<br>" +
                "<b>Dependencias</b><br>" +
                "-Decanato de la Facultad de Derecho<br>" +
                "-Biblioteca especializada de Derecho<br>" +
                "-Laboratorio 14<br>" +
                "-Laboratorio de Data Center<br>" +
                "-Centro de copias TecnyCopias<br>" +
                "-Sala de deportes<br>" +
                "-Auditorio Dr. Rufino Garay<br>" +
                "-Laboratorio 12<br>" +
                "-Aulas: 201 a 208<br>" +
                "-Sala de jurados Dr. Jose Enrique Burgos<br>" +
                "-Sala de Audiencia de Familia<br>" +
                "-Aulas: 301 a 309<br>" +
                "-Aulas: 401 a 407<br>" +
                "-Camara Gesell<br>" +
                "-Aulas: 501 a 507<br>");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return gLView;
    }
}