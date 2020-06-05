package com.utec.vmap;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.util.JsonReader;
import android.util.Log;
import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.utec.vmap.api.ApiCallback;
import com.utec.vmap.api.RestfulApi;

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

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private RestfulApi api;
    private Activity act;
    private final String HOST = "http://192.168.1.10:8081";
    private final String API_SENDESTUDIANTE = HOST +"/sendestudiante";

    static {
        System.setProperty("java.protocol.handler.pkgs", "org.andresoviedo.util.android");
        URL.setURLStreamHandlerFactory(new AndroidURLStreamHandlerFactory());
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        if(!Util.Load(this,"carnet").equals("")) {
            navController.getGraph().setStartDestination(R.id.nav_profile);
            navigationView.getMenu().findItem(R.id.nav_login).setTitle("Perfil");
        }
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        api = new RestfulApi(getApplicationContext());
        act=this;
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
        //http://consultas.utec.edu.sv/servicios_movil/ServiciosAlumnos.asmx/Notas?carnet=1557812014
        //http://consultas.utec.edu.sv/servicios_movil/ServiciosAlumnos.asmx/Login?carnet=2541582018&password=13051988
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
                    Toast.makeText(view.getContext(),"Ocurrio un error!\n" + ex.getMessage(), Toast.LENGTH_LONG);
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
                    act.recreate();
                }else
                    Toast.makeText(view.getContext(),"Datos incorrectos", Toast.LENGTH_LONG);

            }

            @Override
            public void OnError(String error) {
                Toast.makeText(view.getContext(),"Ocurrio un error!\n" + error, Toast.LENGTH_LONG);
            }
        });
    }
    public void onLogout(View view) {
        Util.Save(this,"carnet","");
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        navController.navigate(R.id.nav_login);
        act.recreate();

    }
    public JSONObject parseJson(String req)
    {
        try{
            return new JSONObject(req.substring(req.indexOf("{"),req.lastIndexOf("}")+1));
        }catch (Exception ex)
        {
            return null;
        }
    }

}
