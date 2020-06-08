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
import java.util.LinkedList;
import java.util.Random;

import static android.content.Context.MODE_PRIVATE;

public class Util {
    private static String txt;
    private static String tlt;
    private static LinkedList<Locacion> locacions;
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
    public static void SyncLocations(RestfulApi api, String url)
    {
        ap=api;
        ul=url;
        api.get(url, new ApiCallback() {
            @Override
            public void OnSuccess(String obj) {
                locacions = new LinkedList<>();
                try{
                    JSONObject o = new JSONObject(obj);
                    int length = o.getInt("size");
                    for(int i=0;i<length;i++)
                        Util.addLocacion(o.getJSONObject(String.valueOf(i)));
                }catch (Exception e)
                {
                    Log.e("GetLocaciones: ", "Error en la conversion");
                }
            }

            @Override
            public void OnError(String error) {
                Log.e("GetLocaciones: ", "Error en la peticion");
            }
        });
    }
    public static void LoadLocations(SLoader scene)
    {
        scene.clearSubObjects();
        Random r = new Random();
        for (Util.Locacion l:
                Util.getLocacions()) {
            Object3DData line = Object3DBuilder.buildLine(new float[]{l.getX(),l.getY(),l.getZ(),l.getX(),l.getY()+10,l.getZ()});
            Object3DData o = Object3DBuilder.buildSquareV2();
            if(l.get_nombre().equals("test"))
                o.setPosition(new float[]{l.getX(),l.getY(),l.getZ()});
            else
                o.setPosition(new float[]{l.getX(),l.getY()+10,l.getZ()});
            float red=r.nextFloat(),green=r.nextFloat(),blue=r.nextFloat();
            o.setColor(new float[]{red,green,blue,1.0f});
            line.setColor(new float[]{red,green,blue,1.0f});
            o.setScale(new float[]{0.05f,0.05f,0.05f});
            line.setScale(new float[]{0.05f,0.05f,0.05f});
            o.setId(l.get_nombre());
            scene.addObject(o);
            if(!l.get_nombre().equals("test"))
            {
                scene.addObject(line);
                Object3DData sc = Object3DBuilder.buildSquareV2();
                sc.setPosition(new float[]{l.getX(),l.getY(),l.getZ()});
                sc.setColor(new float[]{red,green,blue,1.0f});
                sc.setScale(new float[]{0.05f,0.05f,0.05f});
                scene.addObject(sc);
            }
        }
    }
    public static void addLocacion(JSONObject data)
    {
        if(locacions==null) locacions=new LinkedList<>();
        try{
            locacions.add(new Locacion(data.getString("NOMBRE"),
                    Float.parseFloat(data.getString("X")),Float.parseFloat(data.getString("Y")),Float.parseFloat(data.getString("Z"))));
        }catch (Exception ex)
        {

        }
    }
    public static Locacion[] getLocacions()
    {
        SyncLocations(ap,ul);
        if(locacions == null) locacions = new LinkedList<>();
        return locacions.toArray(new Locacion[locacions.size()]);
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
        private String _nombre;
        private float x;
        private float y;
        private float z;
        public Locacion()
        {

        }
        public Locacion(String nombre, float x, float y, float z)
        {
            _nombre = nombre;
            this.x=x;
            this.y=y;
            this.z=z;
        }

        public String get_nombre() {
            return _nombre;
        }

        public void set_nombre(String _nombre) {
            this._nombre = _nombre;
        }

        public float getX() {
            return x;
        }

        public void setX(float x) {
            this.x = x;
        }

        public float getY() {
            return y;
        }

        public void setY(float y) {
            this.y = y;
        }

        public float getZ() {
            return z;
        }

        public void setZ(float z) {
            this.z = z;
        }
    }
}
