package com.utec.vmap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.util.AttributeSet;
import android.util.JsonReader;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.utec.vmap.api.ApiCallback;
import com.utec.vmap.api.RestfulApi;
import com.utec.vmap.ui.home.Informacion;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import android.view.Menu;
import android.widget.EditText;
import android.widget.Toast;

import org.andresoviedo.util.android.AndroidURLStreamHandlerFactory;
import org.json.JSONObject;

import java.net.URL;
import java.util.HashMap;
import java.util.zip.Inflater;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private RestfulApi api;
    private Activity act;
    public static final String HOST = "http://13.82.193.57:8081";
    public static final String API_SENDESTUDIANTE = HOST +"/sendestudiante";
    public static final String API_LOCACIONES = HOST +"/locaciones";
    public static final String API_PINTERES = HOST + "/pinteres";

    static {
        System.setProperty("java.protocol.handler.pkgs", "org.andresoviedo.util.android");
        URL.setURLStreamHandlerFactory(new AndroidURLStreamHandlerFactory());
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = new RestfulApi(getApplicationContext());
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_login, R.id.nav_fundadores, R.id.nav_profile,R.id.nav_benito,R.id.nav_morazan,R.id.nav_simon)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        act=this;
        new Thread(new Runnable() {
            @Override
            public void run() {
                Util.SyncLocations(api,API_LOCACIONES);
            }
        }).start();
        if(!Util.Load(this,"carnet").equals("")) {
            navController.getGraph().setStartDestination(R.id.nav_profile);
            navigationView.getMenu().findItem(R.id.nav_login).setTitle("Perfil");
            new Thread((new Runnable() {
                @Override
                public void run() {
                    Util.SyncPInteres(api, API_PINTERES, act);
                }
            })).start();
        }else{
            Util.setTitle("Informacion");
            Util.setText("Si sincroniza sus datos apareceran lugares de interes de manera automatica");
        }
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void onLogin(View view) {
        EditText user = view.getRootView().findViewById(R.id.username);
        EditText pass = view.getRootView().findViewById(R.id.password);
        if(user.getText().toString().trim().equals("") || pass.getText().toString().trim().equals(""))
        {
            Toast.makeText(view.getRootView().getContext(),"Ingrese todos los datos!", Toast.LENGTH_LONG).show();
            return;
        }
        api.get("http://consultas.utec.edu.sv/servicios_movil/ServiciosAlumnos.asmx/Login?carnet="+ user.getText().toString().trim().replace("-","") +"&password=" + pass.getText().toString().trim(), new ApiCallback() {
            @Override
            public void OnSuccess(String obj) {
                JSONObject json = parseJson(obj);
                String carnet,carrera,nombres;
                try{
                    carnet = json.getString("carnet");
                    carrera = json.getString("carrera");
                    nombres = json.getString("nombres");
                }catch (Exception ex)
                {
                    Toast.makeText(view.getRootView().getContext(),"Ocurrio un error!\n" + ex.getMessage(), Toast.LENGTH_LONG).show();
                    return;
                }
                if(!carnet.equals("0"))
                {
                    String a[] = nombres.split(" ");
                    String apellido = "";
                    try{
                        apellido = a[a.length-2] + " " + a[a.length-1];
                    }catch (Exception ex)
                    {
                    }
                    Util.Save(act,"carnet", carnet.replace("-",""));
                    Util.Save(act,"facultad", carrera);
                    Util.Save(act,"nombres", nombres.replace(apellido,""));
                    Util.Save(act,"apellidos",apellido);
                    api.post(API_SENDESTUDIANTE, Util.getAllValues(act,new String[]{"carnet", "facultad", "nombres", "apellidos"}), new ApiCallback() {
                        @Override
                        public void OnSuccess(String obj) {
                            if(obj.contains("2"))
                                Log.i("SendEstudiante: ", "Datos enviados");
                            else
                                Log.e("SendEstudiante: ", "Ocurrio un error en la base de datos!");
                        }

                        @Override
                        public void OnError(String error) {
                            Log.e("SendEstudiante: ", "Ocurrio un error en la conexion " + error);
                        }
                    });
                    api.get("http://consultas.utec.edu.sv/servicios_movil/ServiciosAlumnos.asmx/Notas?carnet=" + Util.Load(act,"carnet"), new ApiCallback() {
                        @Override
                        public void OnSuccess(String obj) {
                            Util.Save(act,"materias",obj);
                            act.recreate();
                        }

                        @Override
                        public void OnError(String error) {
                            Log.e("API: ", "No se obtuvieron las materias");
                            act.recreate();
                        }
                    });
                }else
                    Toast.makeText(view.getRootView().getContext(),"Datos incorrectos", Toast.LENGTH_LONG).show();

            }

            @Override
            public void OnError(String error) {
                Toast.makeText(view.getRootView().getContext(),"Ocurrio un error!\n" + error, Toast.LENGTH_LONG).show();
            }
        });
    }
    public void onLogout(View view) {
        Util.Clear(act);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        navController.navigate(R.id.nav_login);
        act.recreate();

    }
    public static JSONObject parseJson(String req)
    {
        try{
            return new JSONObject(req.substring(req.indexOf("{"),req.lastIndexOf("}")+1));
        }catch (Exception ex)
        {
            return null;
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        boolean ret = super.onOptionsItemSelected(item);
        if(item.getItemId() == R.id.action_settings)
            startActivity(new Intent(this.getApplicationContext(), Informacion.class));
        return ret;
    }
}
