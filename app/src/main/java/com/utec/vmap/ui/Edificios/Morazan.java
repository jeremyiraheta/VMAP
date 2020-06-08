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

public class Morazan extends Fragment {

    private MSV gLView;

    private SLoader scene;

    private Handler handler;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler = new Handler(this.getActivity().getMainLooper());
        scene = new SLoader(this.getActivity(), Uri.parse("assets://assets/models/Morazan.obj"));
        scene.init();
        try {
            gLView = new MSV(this.getActivity(), scene);
            scene.setGLView(gLView);
        } catch (Exception ex) {
            Log.println(Log.ERROR, "", ex.getMessage());
            return;
        }
        Util.setTitle("Francisco Morazan");
        Util.setText("Edificio nominado en honor del paladin de origen hondurenyo que unio la parcela centroamericana.<br>" +
                "<b>Dependencias</b><br>" +
                "-Decanato de la Facultad de Ciencias Empresariales<br>" +
                "-Direccion de Escuela de Administracion y Finanzas<br>" +
                "-Coordinacion de Administracion y Contabilidad<br>" +
                "-Coordinacion de Mercadeo, Economia y Turismo<br>" +
                "-Auditorio de la Paz<br>" +
                "-Biblioteca especializada de Negocios<br>" +
                "-Laboratorio 4 de Cisco<br>" +
                "-Sala de docentes y Coordinacion<br>" +
                "-Laboratorio 8 de Redes<br>" +
                "-Laboratorios 1 y 2 <br>" +
                "-Laboratorio de Tecnologias avanzadas<br>" +
                "-Aulas: 201 a 207<br>" +
                "-Aulas: 301 a 309<br>" +
                "-Aulas: 401 a 407<br>" +
                "-Aulas: 501 a 507<br>");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return gLView;
    }
}