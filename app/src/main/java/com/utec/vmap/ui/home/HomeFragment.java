package com.utec.vmap.ui.home;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextPaint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.utec.vmap.MainActivity;
import com.utec.vmap.R;
import com.utec.vmap.Util;
import com.utec.vmap.api.RestfulApi;
import com.utec.vmap.ui.Edificios.MSV;
import com.utec.vmap.ui.Edificios.SLoader;

import org.andresoviedo.android_3d_model_engine.model.Object3D;
import org.andresoviedo.android_3d_model_engine.model.Object3DData;
import org.andresoviedo.android_3d_model_engine.services.Object3DBuilder;

import java.util.Random;

public class HomeFragment extends Fragment {
    private MSV gLView;

    private SLoader scene;

    private Handler handler;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler = new Handler(this.getActivity().getMainLooper());
        scene = new SLoader(this.getActivity(), Uri.parse("assets://assets/models/Mapa.obj"));
        Util.SyncPInteres(new RestfulApi(getActivity()), MainActivity.API_PINTERES,getActivity());
        if(Util.getSyncing())scene.addObject(Object3DBuilder.buildAxis());
        scene.setCameraAnimation(false);
        if(Util.getSyncing())
        {
            scene.init(new Thread(new Runnable() {
                @Override
                public void run() {
                    while(scene != null)
                    {
                        try{
                            Util.LoadLocations(scene,false);
                            Thread.sleep(20000);
                        }catch (Exception ex)
                        {
                        }
                    }
                }
            }));
        }else
            scene.init(new Thread(new Runnable() {
                @Override
                public void run() {
                    Util.LoadLocations(scene,true);
                }
            }));
        scene.getCamera().xPos = 0.9263429f;
        scene.getCamera().yPos = 1.9843282f;
        scene.getCamera().zPos = 1.7422535f;
        scene.getCamera().xUp = -0.053720906f;
        scene.getCamera().yUp = 0.6728751f;
        scene.getCamera().zUp = -0.73780364f;
        try{
            gLView = new MSV(this.getActivity(),scene);
            scene.setGLView(gLView);
            Util.setTitle("Lugares de Interes");
            String txt="<br>";
            Util.setText("");
            Random rnd = new Random();
            for (Util.Locacion l:
                 Util.getLocacions(true)) {
                float red=rnd.nextInt(255),green=rnd.nextInt(255),blue=rnd.nextInt(255);
                Util.getLocacion(l.getID()).setColor(new float[]{red/255,green/255,blue/255});
                String r=Integer.toHexString((int)red),g=Integer.toHexString((int)green),b=Integer.toHexString((int)blue);
                if(r.length()==1)
                    r = "0"+r;
                if(g.length()==1)
                    g="0"+g;
                if(b.length()==1)
                    b="0"+b;
                String hex="0x" + r + g + b;
                txt += "-<font size=16 color=" + hex + ">" + l.get_nombre() + "</font><br>";
            }
            Util.setText(txt);
        }catch (Exception ex)
        {
            Log.println(Log.ERROR,"",ex.getMessage()) ;
            return;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(paused)
        {
            try{
                gLView = new MSV(this.getActivity(),scene);
            }catch (Exception ex)
            {

            }
            paused=false;
        }
    }
    private boolean paused=false;
    @Override
    public void onPause() {
        super.onPause();
        paused=true;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return gLView;
    }

}