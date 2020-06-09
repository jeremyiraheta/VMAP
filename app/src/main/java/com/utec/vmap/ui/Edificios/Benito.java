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

public class Benito extends Fragment {

    private MSV gLView;

    private SLoader scene;

    private Handler handler;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler = new Handler(this.getActivity().getMainLooper());
        scene = new SLoader(this.getActivity(), Uri.parse("assets://assets/models/Benito.obj"));
        scene.init();
        try {
            gLView = new MSV(this.getActivity(), scene);
            scene.setGLView(gLView);
        } catch (Exception ex) {
            Log.println(Log.ERROR, "", ex.getMessage());
            return;
        }
        Util.setTitle("Benito Juarez");
        Util.setText("La universidad ha denominado Benito Juarez al edificio donde se erige el busto del que fuera brillante jurista y presidente de la Republica de Mexico<BR>" +
                "<b>Dependencias:</b> <br>" +
                "-Laboratorio 3, de Informatica<br>" +
                "-Biblioteca Central<br>" +
                "-Plaza Benito Juarez<br>" +
                "-Laboratorio 10, de Academia Microsoft<br>" +
                "-Coordinacion de Disenyo Grafico<br>" +
                "-Docentes de Disenyo Grafico<br>" +
                "-Aulas: 201 a 203<br>" +
                "-Aulas: 301 a 305<br>" +
                "-Aulas: 401 a 405<br>" +
                "-Aulas: 501 a 506<br>");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return gLView;
    }
}