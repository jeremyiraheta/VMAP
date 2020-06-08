package com.utec.vmap.ui.login;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.utec.vmap.R;
import com.utec.vmap.Util;

import org.json.JSONArray;
import org.json.JSONObject;


public class profile extends Fragment {

    public profile() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if(Util.Load(getActivity(),"carnet").equals(""))
            Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.nav_login);
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
        String _materias ="";
        carnet.setText(Html.fromHtml("<b>Carnet: </b> " + _carnet));
        nombres.setText(Html.fromHtml("<b>Nombres: </b>" + _nombres + " " + _apellidos));
        carrera.setText(Html.fromHtml("<b>Facultad: </b>" + _carrera));
        Util.setTitle("Materias");
        try{
            _materias = pref.getString("materias","No hay informacion!");
            JSONObject parse = new JSONObject(_materias.substring(_materias.indexOf("{"),_materias.lastIndexOf("}")+1));
            JSONArray notas = parse.getJSONArray("notas");
            String table = "";
            for(int i=0; i < notas.length(); i++)
            {
                JSONObject current = notas.getJSONObject(i);
                table += "<h2>Codigo: " + current.getString("mat_codigo") + "</h2>";
                table += "<br><i>Nombre</i>: &#9;" + current.getString("mat_nombre");
                table += "<br><i>Docente</i>: &#9;" + current.getString("nombre_docente");
                table += "<br><i>Correo</i>: &#9;" + current.getString("correo_docente");
                table += "<br><i>Aula</i>: &#9;" + current.getString("Aula");
                table += "<br><i>Dias</i>: &#9;" + current.getString("Dias");
                table += "<br><i>Horas</i>: &#9;" + current.getString("Horas");
                table += "<br><i>Seccion</i>: &#9;" + current.getString("Seccion");

            }
            table += "";
            _materias = table;
        }catch (Exception ex)
        {

        }
        Util.setText(_materias);
    }
}