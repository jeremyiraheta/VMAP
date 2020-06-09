package com.utec.vmap;

import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.utec.vmap.api.ApiCallback;
import com.utec.vmap.api.RestfulApi;
import com.utec.vmap.ui.Edificios.SLoader;

import org.andresoviedo.android_3d_model_engine.model.Object3DData;
import org.andresoviedo.android_3d_model_engine.services.Object3DBuilder;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

import static android.content.Context.MODE_PRIVATE;
import static com.utec.vmap.MainActivity.parseJson;

public class Util {
    private static String txt;
    private static String tlt;
    private static HashMap<Integer, Locacion> locacions;
    private static HashSet<Integer> pinteres;
    private static LinkedList<String> aulas;
    public static void Save(Activity activity, String name, String value)
    {
        SharedPreferences pref = activity.getSharedPreferences(activity.getPackageName(), MODE_PRIVATE);
        SharedPreferences.Editor edit = pref.edit();
        edit.putString(name,value);
        edit.commit();
    }
    public static String Load(Activity activity, String name)
    {
        SharedPreferences pref = activity.getSharedPreferences(activity.getPackageName(), MODE_PRIVATE);
        return pref.getString(name,"");
    }
    private static RestfulApi ap;
    private static String ul;
    private static Activity act;
    public static void SyncLocations(RestfulApi api, String url, Activity activity)
    {
        ap=api;
        ul=url;
        act=activity;
        api.get(url, new ApiCallback() {
            @Override
            public void OnSuccess(String obj) {
                locacions = new HashMap<>();
                try{
                    if(Util.getSyncing())Util.addLocacion(new JSONObject("{NOMBRE:test,X:1,Y:1,Z:1}"));
                    JSONObject o = new JSONObject(obj);
                    int length = o.getInt("size");
                    for(int i=0;i<length;i++)
                        Util.addLocacion(o.getJSONObject(String.valueOf(i)));
                }catch (Exception e)
                {
                    Log.e("GetLocaciones: ", "Error en la conversion");
                }
                Log.i("GetAulas:", "Leyendo aulas");
                for (String s:
                     Util.getAulas()) {
                    Util.addPinteres(s);
                }
            }

            @Override
            public void OnError(String error) {
                Log.e("GetLocaciones: ", "Error en la peticion");
            }
        });
    }
    public static int getPinteres(String name)
    {
        for (Locacion l:
             getLocacions(false)) {
            if(name.toLowerCase().equals(l.get_nombre().toLowerCase()))
                return l.getID();
        }
        return -1;
    }
    public static boolean containsPinteres(int key)
    {
        return pinteres.contains(key);
    }
    public static void removePintere(int key)
    {
        try{
            pinteres.remove(key);
            HashMap<String, String> data = new HashMap<>();
            data.put("id",String.valueOf(key));
            data.put("carnet",Load(act,"carnet"));
            ap.post(MainActivity.API_DELPINTERES, data, new ApiCallback() {
                @Override
                public void OnSuccess(String obj) {
                    if(obj.indexOf("5")>-1)
                        Log.i("DelPinteres: ", "Eliminacion exitosa");
                    else
                        Log.i("DelPinteres: ", "Eliminacion fracasada");
                }

                @Override
                public void OnError(String error) {
                    Log.e("DelPinteres:", "Ocurrio un error en la conexion");
                }
            });
        }catch (Exception ex)
        {
            Log.e("DeletePintere:", "No fue posible eliminar " + key);
        }
    }
    public static void addPinteres(String name)
    {
        for (Locacion l:
             getLocacions(false)) {
            if(l.get_nombre().toLowerCase().equals(name.toLowerCase()))
            {
                pinteres.add(l.getID());
                if(ap!=null)
                {
                    HashMap<String,String> s=new HashMap<>();
                    s.put("carnet", Load(act,"carnet"));
                    s.put("id",String.valueOf(l.getID()));
                    ap.post(MainActivity.API_SEDPINTERES, s, new ApiCallback() {
                        @Override
                        public void OnSuccess(String obj) {
                            if(obj.indexOf("5")>-1)
                                Log.i("SendInteres: ", "Enviado");
                            else
                                Log.i("SendInteres: ", "Algo fallo");
                        }

                        @Override
                        public void OnError(String error) {
                            Log.i("SendInteres: ", "Algo fallo");
                        }
                    });
                }
            }
        }
    }
    public static void SyncPInteres(RestfulApi api, String url)
    {
        api.get("http://consultas.utec.edu.sv/servicios_movil/ServiciosAlumnos.asmx/Notas?carnet=" + Util.Load(act, "carnet"), new ApiCallback() {
            @Override
            public void OnSuccess(String obj) {
                Util.Save(act,"materias",obj);
                api.get(url + "/" + Util.Load(act, "carnet"), new ApiCallback() {
                    @Override
                    public void OnSuccess(String obj) {
                        Log.i("GetPinteres:", "Exito");
                        try{
                            JSONObject json = parseJson(obj);
                            int length = json.getInt("size");
                            for(int i =0 ;i<length;i++)
                                addPinteres(json.getJSONObject(String.valueOf(i)).getString("NOMBRE"));
                        }catch (Exception ex)
                        {
                            Log.e("GetPinteres:", "Error parsing " + ex.getMessage());
                        }
                    }

                    @Override
                    public void OnError(String error) {
                        Log.e("GetPinteres:","Fracaso, " + error);
                    }
                });
            }

            @Override
            public void OnError(String error) {

            }
        });
    }
    public static void LoadLocations(SLoader scene, boolean pinteres)
    {
        scene.clearSubObjects();
        for (Util.Locacion l:
                Util.getLocacions(pinteres)) {
            Object3DData line = Object3DBuilder.buildLine(new float[]{l.getX(),l.getY(),l.getZ(),l.getX(),l.getY()+10,l.getZ()});
            Object3DData o = Object3DBuilder.buildSquareV2();
            if(l.get_nombre().equals("test"))
                o.setPosition(new float[]{l.getX(),l.getY(),l.getZ()});
            else
                o.setPosition(new float[]{l.getX(),l.getY()+10,l.getZ()});
            o.setColor(new float[]{l.getColor()[0],l.getColor()[1],l.getColor()[2],1.0f});
            line.setColor(new float[]{l.getColor()[0],l.getColor()[1],l.getColor()[2],1.0f});
            o.setScale(new float[]{0.05f,0.05f,0.05f});
            line.setScale(new float[]{0.05f,0.05f,0.05f});
            o.setId(l.get_nombre());
            scene.addObject(o);
            if(!l.get_nombre().equals("test"))
            {
                scene.addObject(line);
                Object3DData sc = Object3DBuilder.buildSquareV2();
                sc.setPosition(new float[]{l.getX(),l.getY(),l.getZ()});
                sc.setColor(new float[]{l.getColor()[0],l.getColor()[1],l.getColor()[2],1.0f});
                sc.setScale(new float[]{0.05f,0.05f,0.05f});
                scene.addObject(sc);
            }
        }
    }
    public static void addLocacion(JSONObject data)
    {
        if(locacions==null) locacions=new HashMap<>();
        try{
            locacions.put(data.getInt("ID"),new Locacion(data.getInt("ID"),data.getString("NOMBRE"),
                    Float.parseFloat(data.getString("X")),Float.parseFloat(data.getString("Y")),Float.parseFloat(data.getString("Z"))));
        }catch (Exception ex)
        {

        }
    }
    public static void setSyncing(boolean sync)
    {
        snc=sync;
    }
    public static boolean getSyncing(){return snc;}
    private static boolean snc = false;
    public static Locacion[] getLocacions(boolean pi)
    {
        if(snc)SyncLocations(ap,ul,act);
        if(pinteres==null)pinteres=new HashSet<>();
        if(pi)
        {
            LinkedList<Locacion> tmp = new LinkedList<>();
            for (int i:
                 pinteres.toArray(new Integer[pinteres.size()])) {
                tmp.add(locacions.get(i));
            }
            return tmp.toArray(new Locacion[tmp.size()]);
        }else
            return locacions.values().toArray(new Locacion[locacions.size()]);
    }
    public  static Locacion getLocacion(int id)
    {
        return locacions.get(id);
    }
    public static void clearPInteres()
    {
        pinteres.clear();
    }
    public static void addAula(String nombre)
    {
        if(aulas == null)
            aulas = new LinkedList<>();
        for (String a:
             aulas) {
            if(a.equals(nombre))
                return;
        }
        aulas.add(nombre);
    }
    public static String[] getAulas()
    {
        if(aulas == null) aulas = new LinkedList<>();
        return aulas.toArray(new String[aulas.size()]);
    }
    public static void Clear(Activity activity)
    {
        SharedPreferences pref = activity.getSharedPreferences(activity.getPackageName(), MODE_PRIVATE);
        SharedPreferences.Editor edit = pref.edit();
        edit.clear();
        edit.commit();
    }
    public  static HashMap<String, String> getAllValues(Activity activity, String[] vals)
    {
        HashMap<String,String> ret = new HashMap<>();
        SharedPreferences pref = activity.getSharedPreferences(activity.getPackageName(), MODE_PRIVATE);
        for (String v:
             vals) {
            ret.put(v,pref.getString(v,""));
        }
        return ret;
    }
    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    public static String getTitle(){return tlt;}
    public static String getText(){return txt;}
    public static void setTitle(String t)
    {
        tlt=t;
    }
    public static void setText(String t)
    {
        txt=t;
    }
    public static class Locacion
    {
        private int id;
        private String _nombre;
        private float x;
        private float y;
        private float z;
        private float[] color;
        public Locacion()
        {

        }
        public Locacion(int id, String nombre, float x, float y, float z)
        {
            this.id =id;
            _nombre = nombre;
            this.x=x;
            this.y=y;
            this.z=z;
        }

        public int getID(){return id;}
        public String get_nombre() {
            return _nombre;
        }
        public float getX() {
            return x;
        }
        public float getY() {
            return y;
        }
        public float getZ() {
            return z;
        }

        public void setColor(float[] color) {
            this.color = color;
        }
        public float[] getColor()
        {
            return this.color;
        }
    }
}
