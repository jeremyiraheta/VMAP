package com.utec.vmap.ui.login;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.utec.vmap.R;


public class profile extends Fragment {

    public profile() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView carnet =view.findViewById(R.id.carnet);
        TextView carrera = view.findViewById(R.id.carrera);
        TextView nombres = view.findViewById(R.id.nombres);
        SharedPreferences pref = getActivity().getSharedPreferences(getActivity().getPackageName(), Context.MODE_PRIVATE);
        String _carnet = pref.getString("carnet","");
        String _nombres = pref.getString("nombres","");
        String _apellidos = pref.getString("apellidos","");
        String _carrera = pref.getString("facultad","");
        carnet.setText(Html.fromHtml("<b>Carnet: </b> " + _carnet));
        nombres.setText(Html.fromHtml("<b>Nombres: </b>" + _nombres + " " + _apellidos));
        carrera.setText(Html.fromHtml("<b>Facultad: </b>" + _carrera));
    }
}